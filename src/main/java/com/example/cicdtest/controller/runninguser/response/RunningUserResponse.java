package com.example.cicdtest.controller.runninguser.response;


import com.example.cicdtest.controller.running.response.RunningResponse;
import com.example.cicdtest.domain.running.Running;
import com.example.cicdtest.domain.runninguser.RunningUser;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.dto.UserResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningUserResponse {

    private Long id;

    private UserResponse user;

    public static RunningUserResponse fromEntity(RunningUser runningUser) {
        return RunningUserResponse.builder()
                .id(runningUser.getId())
                .user(UserResponse.fromEntity(runningUser.getUser()))
                .build();

    }

    public static List<RunningUserResponse> fromEntity(List<RunningUser> runningUserList) {
        return runningUserList.stream()
                .map(RunningUserResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
