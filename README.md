# CICDTEST


## 테스트 목적

nginx와 Docker 그리고 github action을 통해서 CICD를 만들고 blue green 무중단 배포를 위한 목적으로 테스트를 진행하였다.



## CICD.yml (github action)


    name: CI
    
    on:
      push:
        branches: [ "main" ]
      pull_request:
        branches: [ "main" ]
    
    permissions:
      contents: read
    
    jobs:
      build:
        runs-on: ubuntu-latest
        steps:
          - uses: actions/checkout@v3

      - name: Install JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Build with Gradle
        run: |
          chmod +x ./gradlew
          ./gradlew clean build -x test

      - name: Login to DockerHub
        uses: docker/login-action@v1
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}

      - name: Build Docker
        run: docker build --platform linux/amd64 -t ${{ secrets.DOCKERHUB_USERNAME }}/live_server .

      - name: Push Docker
        run: docker push ${{ secrets.DOCKERHUB_USERNAME }}/live_server:latest

    deploy:
      needs: build
      runs-on: ubuntu-latest
      steps:
        - name: Set target IP
          run: |
            STATUS=$(curl -o /dev/null -w "%{http_code}" "http://${{ secrets.LIVE_SERVER_IP }}/env")
            echo $STATUS
            if [ $STATUS = 200 ]; then
              CURRENT_UPSTREAM=$(curl -s "http://${{ secrets.LIVE_SERVER_IP }}/env")
            else
              CURRENT_UPSTREAM=green
            fi
            echo CURRENT_UPSTREAM=$CURRENT_UPSTREAM >> $GITHUB_ENV
            if [ $CURRENT_UPSTREAM = blue ]; then
              echo "CURRENT_PORT=8080" >> $GITHUB_ENV
              echo "STOPPED_PORT=8081" >> $GITHUB_ENV
              echo "TARGET_UPSTREAM=green" >> $GITHUB_ENV
            elif [ $CURRENT_UPSTREAM = green ]; then
              echo "CURRENT_PORT=8081" >> $GITHUB_ENV
              echo "STOPPED_PORT=8080" >> $GITHUB_ENV
              echo "TARGET_UPSTREAM=blue" >> $GITHUB_ENV
            else
              echo "error"
              exit 1
            fi

      - name: Docker compose
        uses: appleboy/ssh-action@master
        with:
          username: ubuntu
          host: ${{ secrets.LIVE_SERVER_IP }}
          key: ${{ secrets.EC2_SSH_KEY }}
          script_stop: true
          script: |
            sudo docker pull ${{ secrets.DOCKERHUB_USERNAME }}/live_server:latest
            sudo docker-compose -f docker-compose-${{env.TARGET_UPSTREAM}}.yml up -d

      - name: Check deploy server URL
        uses: jtalk/url-health-check-action@v3
        with:
          url: http://${{ secrets.LIVE_SERVER_IP }}:${{env.STOPPED_PORT}}/env
          max-attempts: 5
          retry-delay: 10s

      - name: Change nginx upstream
        uses: appleboy/ssh-action@master
        with:
          username: ubuntu
          host: ${{ secrets.LIVE_SERVER_IP }}
          key: ${{ secrets.EC2_SSH_KEY }}
          script_stop: true
          script: |
            sudo docker exec -i nginx-server bash -c 'echo "set \$service_url ${{ env.TARGET_UPSTREAM }};" > /etc/nginx/conf.d/service-env.inc && nginx -s reload'
            
      - name: Stop current server
        uses: appleboy/ssh-action@master
        with:
          username: ubuntu
          host: ${{ secrets.LIVE_SERVER_IP }}
          key: ${{ secrets.EC2_SSH_KEY }}
          script_stop: true
          script: |
            sudo docker stop ${{env.CURRENT_UPSTREAM}}
            sudo docker rm ${{env.CURRENT_UPSTREAM}}
