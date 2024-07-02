package com.example.cicdtest.controller.running.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content;

    private Double distance;

    private String startLocation;

    private String startDetailLocation;

    private String finishLocation;

    private String finishDetailLocation;

    private Integer limitedPeople;

    private LocalDate date;

    private LocalTime time;
}
