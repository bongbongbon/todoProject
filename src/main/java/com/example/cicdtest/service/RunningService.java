package com.example.cicdtest.service;

import com.example.cicdtest.common.exception.CustomException;
import com.example.cicdtest.controller.running.request.RunningRequest;
import com.example.cicdtest.controller.running.response.RunningResponse;
import com.example.cicdtest.domain.running.Running;
import com.example.cicdtest.domain.running.common.RunningStatus;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.repository.RunningRepository;
import com.example.cicdtest.repository.RunningUserRepository;
import com.example.cicdtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RunningService {

    private final RunningRepository runningRepository;
    private final UserRepository userRepository;
    private final RunningUserRepository runningUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RUNNING_CACHE_PREFIX = "running:";


    // 런닝 만들기
    public RunningResponse createRunning(User user, RunningRequest runningRequest) {

        User hostUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);



        Running running = Running.builder()
                .user(hostUser)
                .title(runningRequest.getTitle())
                .content(runningRequest.getContent())
                .distance(runningRequest.getDistance())
                .startLocation(runningRequest.getStartLocation())
                .startDetailLocation(runningRequest.getStartDetailLocation())
                .finishLocation(runningRequest.getFinishLocation())
                .finishDetailLocation(runningRequest.getFinishDetailLocation())
                .limitedPeople(runningRequest.getLimitedPeople())
                .date(runningRequest.getDate())
                .time(runningRequest.getTime())
                .runningStatus(RunningStatus.INCOMPLETE)
                .build();

        updateRedisCache();
        return RunningResponse.fromEntity(runningRepository.save(running));
    }

    // 런닝 리스트 만들고 필터로 조회하기
    public List<RunningResponse> getRunningList(String dateStr, String startLocation, String minDistanceStr, String maxDistanceStr) {


        // LocalDate로 파싱
        LocalDate date = LocalDate.parse(dateStr);

        Double minDistance = null;
        Double maxDistance = null;


        //  minDistanceStr, maxDistanceStr Double로 파싱
        try {
            if (minDistanceStr != null && !minDistanceStr.isEmpty()) {
                minDistance = Double.parseDouble(minDistanceStr);
            }
            if (maxDistanceStr != null && !maxDistanceStr.isEmpty()) {
                maxDistance = Double.parseDouble(maxDistanceStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid distance value");
        }

        String cacheKey = generateCacheKey(dateStr, startLocation, minDistanceStr, maxDistanceStr);
        List<RunningResponse> runningResponses = (List<RunningResponse>) redisTemplate.opsForValue().get(cacheKey);



        if (runningResponses == null) {
            List<Running> runningList = runningRepository.findRunnings(date, startLocation, minDistance, maxDistance);
            runningResponses = RunningResponse.fromEntity(runningList);
            redisTemplate.opsForValue().set(cacheKey, runningResponses, 10, TimeUnit.MINUTES); // 10분 동안 캐시유지
        }


        return runningResponses;
    }


    public RunningResponse getRunningById(Long id) {

        return RunningResponse.fromEntity(runningRepository.findById(id)
                .orElseThrow(() -> CustomException.RUNNING_NOT_FOUND));
    }

    public RunningResponse updateRunning(User user, Long id, RunningRequest runningRequest) {

        User hostUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        Running currentRunning =  runningRepository.findById(id)
                .orElseThrow(() -> CustomException.RUNNING_NOT_FOUND);

        if (!currentRunning.getUser().getEmail().equals(hostUser.getEmail())) {
            throw CustomException.USER_INFO_NOT_MATCH;
        }


        currentRunning.setTitle(runningRequest.getTitle());
        currentRunning.setContent(runningRequest.getContent());
        currentRunning.setDistance(runningRequest.getDistance());
        currentRunning.setStartLocation(runningRequest.getStartLocation());
        currentRunning.setStartDetailLocation(runningRequest.getStartDetailLocation());
        currentRunning.setFinishLocation(runningRequest.getFinishLocation());
        currentRunning.setFinishDetailLocation(runningRequest.getFinishDetailLocation());
        currentRunning.setLimitedPeople(runningRequest.getLimitedPeople());
        currentRunning.setDate(runningRequest.getDate());
        currentRunning.setTime(runningRequest.getTime());

        updateRedisCache();

        return RunningResponse.fromEntity(runningRepository.save(currentRunning));
    }

    public void deleteRunningById(User user, Long id) {

        Running running =  runningRepository.findById(id)
                .orElseThrow(() -> CustomException.RUNNING_NOT_FOUND);

        if (!running.getUser().getEmail().equals(user.getEmail())) {
            throw CustomException.YOU_ARE_NOT_RUNNING_LEADER;
        }


        runningRepository.deleteById(id);
        updateRedisCache();
    }


    private String generateCacheKey(String date, String startLocation, String minDistance, String maxDistance) {
        return RUNNING_CACHE_PREFIX + date + ":" + startLocation + ":" + minDistance + ":" + maxDistance;
    }


    private void updateRedisCache() {

        Set<String> keysToDelete = redisTemplate.keys("running:*");
        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

    }
}
