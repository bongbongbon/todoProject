package com.example.cicdtest.controller.auth;

import com.example.cicdtest.controller.auth.request.LoginRequest;
import com.example.cicdtest.controller.auth.request.SignupRequest;
import com.example.cicdtest.dto.UserResponse;
import com.example.cicdtest.dto.UserTokenDto;
import com.example.cicdtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginApiController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(      @RequestPart("email") String email,
                                                       @RequestPart("password") String password,
                                                       @RequestPart("passwordCheck") String passwordCheck,
                                                       @RequestPart("nickname") String nickname,
                                                       @RequestPart("profileImage") MultipartFile profileImage) {
        UserResponse successMember = userService.signUp(email,password, passwordCheck, nickname, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(successMember);
    }


    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody LoginRequest request) {
        UserTokenDto loginDTO = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(loginDTO);
    }


}