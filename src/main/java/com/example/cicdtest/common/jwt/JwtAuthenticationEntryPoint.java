package com.example.cicdtest.common.jwt;

import com.example.cicdtest.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, ErrorCode.ACCESS_DENIED);
        } else if (exception.equals(ErrorCode.EXPIRED_ACCESS_TOKEN.getCode())) {
            setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
        } else if (exception.equals(ErrorCode.INVALID_TOKEN.getCode())) {
            setResponse(response, ErrorCode.INVALID_TOKEN);
        } else {
            setResponse(response, ErrorCode.ACCESS_DENIED);
        }

    }

    // HTTP 응답을 설정하는 메서드
    private void setResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // JSON 형식의 응답 본문을 생성한다.
        JSONObject responseJson = new JSONObject();
        responseJson.put("errorCode", errorCode.getCode());
        responseJson.put("errorMessage", errorCode.getMessage());

        // 응답 본문을 작성한다.
        response.getWriter().print(responseJson);
    }
}
