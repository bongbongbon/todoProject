package com.example.cicdtest.dto;

import com.example.cicdtest.domain.users.User;
import jakarta.persistence.Column;
import lombok.*;

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


    public User ofEntity() {
        return User.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .nickname(this.getNickname())
                .build();
    }
}
