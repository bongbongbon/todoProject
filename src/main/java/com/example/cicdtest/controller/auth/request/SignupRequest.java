package com.example.cicdtest.controller.auth.request;

import com.example.cicdtest.domain.users.User;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    private String nickname;
    private String password;
    private String passwordCheck;
    private String email;
    private MultipartFile profileImg;


    public User ofEntity() {
        return User.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .nickname(this.getNickname())
                .build();
    }
}
