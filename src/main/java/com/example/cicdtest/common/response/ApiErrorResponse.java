package com.example.cicdtest.common.response;

import com.example.cicdtest.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse {

    @JsonProperty("errorCode")
    private String code;

    @JsonProperty("errorMessage")
    private String message;

    public static ApiErrorResponse from(ErrorCode errorCode) {
        return ApiErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

}
