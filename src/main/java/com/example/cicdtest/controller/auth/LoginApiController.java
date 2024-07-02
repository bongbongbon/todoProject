package com.example.cicdtest.controller.auth;

import com.example.cicdtest.common.response.ApiSuccessResponse;
import com.example.cicdtest.dto.LoginRequest;
import com.example.cicdtest.dto.SignupRequest;
import com.example.cicdtest.dto.UserResponse;
import com.example.cicdtest.dto.UserTokenDto;
import com.example.cicdtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginApiController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody SignupRequest request) {
        UserResponse successMember = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.OK).body(successMember);
    }


    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody LoginRequest request) {
        UserTokenDto loginDTO = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(loginDTO);
    }


}