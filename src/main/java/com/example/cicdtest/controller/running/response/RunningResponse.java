package com.example.cicdtest.controller.running.response;

import com.example.cicdtest.domain.boards.Board;
import com.example.cicdtest.domain.running.Running;
import com.example.cicdtest.domain.running.common.RunningStatus;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.dto.BoardResponse;
import com.example.cicdtest.dto.UserResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningResponse {

    private Long id;

    private String title;

    private String content;

    private Double distance;

    private String startLocation;

    private String startDetailLocation;

    private String finishLocation;

    private String finishDetailLocation;

    private Integer limitedPeople;

    private RunningStatus runningStatus;

    private Integer runningUserCount;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

    private UserResponse user;



    public static RunningResponse fromEntity(Running running) {
        return RunningResponse.builder()
                .id(running.getId())
                .title(running.getTitle())
                .content(running.getContent())
                .distance(running.getDistance())
                .startLocation(running.getStartLocation())
                .startDetailLocation(running.getStartDetailLocation())
                .finishLocation(running.getFinishLocation())
                .finishDetailLocation(running.getFinishDetailLocation())
                .limitedPeople(running.getLimitedPeople())
                .date(running.getDate())
                .time(running.getTime())
                .runningStatus(running.getRunningStatus())
                .runningUserCount(getRunningCount(running))
                .user(UserResponse.fromEntity(running.getUser()))
                .build();
    }

    // List<Running>를 List<BoardResponse>로 변환
    public static List<RunningResponse> fromEntity(List<Running> runningList) {
        return runningList.stream()
                .map(RunningResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public static Integer getRunningCount(Running running) {
        if (running.getRunningUserList() == null) {
            return 0;
        }
        System.out.println(running.getRunningUserList().size());
        return running.getRunningUserList().size();

    }


}
