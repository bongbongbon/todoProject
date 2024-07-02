package com.example.cicdtest.dto;

import com.example.cicdtest.domain.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTokenDto {

    private String email;
    private String token;
    private String nickname;

    @Builder
    public UserTokenDto(String email, String nickname, String token) {
        this.email = email;
        this.nickname = nickname;
        this.token = token;
    }

    // Entity -> DTO
    public static UserTokenDto fromEntity(User user, String token) {
        return UserTokenDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .token(token)
                .build();
    }
}
