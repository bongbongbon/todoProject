package com.example.cicdtest.common.jwt;

import com.example.cicdtest.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService; // 사용자 정보를 제공하는 서비스
    private final JwtProperties jwtProperties;

    @Value("${spring.jwt.header}") private String HEADER_STRING; // HTTP 요청 헤더에서 JWT를 찾을 헤더 이름 -> "Authorization"
    @Value("${spring.jwt.prefix}") private String TOKEN_PREFIX; // JWT가 시작하는 접두사 -> "Bearer"

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Thread currentThread = Thread.currentThread();
        log.info("현재 실행 중인 스레드: " + currentThread.getName());

        // 토큰을 가져와 저장할 변수 선언
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;

        System.out.println(header);
        // 1. JWT 토큰을 가지고 있는 경우, 토큰을 추출한다.
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX," ");
            try {
                // JWT에서 사용자 이름 추출
                username = this.jwtProperties.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException ex) {
                log.info("사용자 ID 가져오기 실패");
                request.setAttribute("exception", ErrorCode.USER_NOT_FOUND.getCode());
            } catch (ExpiredJwtException ex) {
                log.info("토큰 만료");
                request.setAttribute("exception", ErrorCode.EXPIRED_ACCESS_TOKEN.getCode());
            } catch (MalformedJwtException ex) {
                log.info("Invalid JWT Token");
                request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getCode());
            } catch (Exception e) {
                log.info("Invalid JWT Token");
                request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getCode());
            }
        }
        // 2. JWT 토큰이 없는 경우,  "인증 실패" 로그를 남긴다.
        else {
            log.info("JWT가 Bearer로 시작하지 않습니다 !!");
        }

        //3. 요청에는 사용자의 식별 정보가 포함되어 있으면서, 현재 요청이 인증되지 않았을 때
        // -> (사용자가 인증되지 않은 상태에서 인증된 상태로 전환하는 과정이다.)
        if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // JWT 토큰 유효성 검사
            if (this.jwtProperties.validateToken(authToken, userDetails)) {
                // 인증 정보 생성
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 인증된 사용자 정보 설정
                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("인증된 사용자 " + username + ", 보안 컨텍스트 설정");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            //  4. 인증되지 않은 JWT 토큰임을 로그로 알린다.
            else {
                log.info("잘못된 JWT 토큰 !!");
            }
        }
        // "요청 정보가 없다."
        else {
            log.info("사용자 이름이 null이거나 컨텍스트가 null입니다 !!");
        }
        //  5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }



}
