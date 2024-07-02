package com.example.cicdtest.controller.running;

import com.example.cicdtest.common.response.ApiSuccessResponse;
import com.example.cicdtest.controller.running.request.RunningRequest;
import com.example.cicdtest.controller.running.response.RunningResponse;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.dto.BoardCreateRequest;
import com.example.cicdtest.dto.BoardResponse;
import com.example.cicdtest.service.RunningService;
import com.example.cicdtest.service.RunningUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/runnings")
public class RunningController {

    private final RunningService runningService;
    private final RunningUserService runningUserService;

    @PostMapping("/create")
    public ResponseEntity<?> createRunning(@AuthenticationPrincipal User user,
                                           @RequestBody RunningRequest runningRequest,
                                           BindingResult bindingResult) {

        RunningResponse runningResponse = runningService.createRunning(user, runningRequest);

        return ResponseEntity.status(HttpStatus.OK).body(runningResponse);
    }

    @GetMapping("/get")
    public ApiSuccessResponse<?> getRunningList(@RequestParam(name = "date") String dateString,
                                                @RequestParam(name = "startLocation") String startLocation,
                                                @RequestParam(name = "minDistance") String minDistanceStr,
                                                @RequestParam(name = "maxDistance") String maxDistanceStr) {

        LocalDate date = LocalDate.parse(dateString);


        return ApiSuccessResponse.from(runningService.getRunningList(dateString, startLocation, minDistanceStr, maxDistanceStr));
    }

    @GetMapping("/get/{id}")
    public ApiSuccessResponse<?> getRunningList(@PathVariable(name = "id") Long id) {

        return ApiSuccessResponse.from(runningService.getRunningById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ApiSuccessResponse<?> deleteRunning(@AuthenticationPrincipal User user,  @PathVariable(name = "id") Long id) {

        runningService.deleteRunningById(user, id);

        return ApiSuccessResponse.NO_DATA_RESPONSE;
    }


}
