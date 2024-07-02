package com.example.cicdtest.controller.runninguser;

import com.example.cicdtest.common.response.ApiSuccessResponse;
import com.example.cicdtest.controller.running.response.RunningResponse;
import com.example.cicdtest.controller.runninguser.response.RunningUserResponse;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.service.RunningUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/runnings/users")
@RequiredArgsConstructor
public class RunningUserController {

    private final RunningUserService runningUserService;

    @PostMapping("/join/{id}")
    public ApiSuccessResponse<RunningUserResponse> joinRunningUser(@AuthenticationPrincipal User user,
                                                                   @PathVariable(name = "id") Long runningId) {


        return ApiSuccessResponse.from(runningUserService.join(user ,runningId));
    }

    @PostMapping("/get/{id}")
    public ApiSuccessResponse<List<RunningUserResponse>> getUsers(@PathVariable(name = "id") Long runningId) {

        List<RunningUserResponse> users = runningUserService.getUsers(runningId);

        return ApiSuccessResponse.from(users);
    }


}
