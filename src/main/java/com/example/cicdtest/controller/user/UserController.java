package com.example.cicdtest.controller.user;

import com.example.cicdtest.common.jwt.JwtProperties;
import com.example.cicdtest.dto.UserResponse;
import com.example.cicdtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    /** 회원정보 조회 API */
    @GetMapping("/api/user/profile")
    public ResponseEntity<?> findUser(@RequestHeader("Authorization") String token) {
        UserResponse userResponse = null;

        if (token != null) {
            String email = jwtProperties.getEmailFromJwt(token);
             userResponse = userService.getUser(email);
        }

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
