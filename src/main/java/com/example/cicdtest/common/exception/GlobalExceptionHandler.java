package com.example.cicdtest.common.exception;

import com.example.cicdtest.common.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiErrorResponse.from(e.getErrorCode()));
    }

    // @Valid에서 binding error 발생
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> params = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            params.add(error.getField() + ": " + error.getDefaultMessage());
        }
        String errorMessage = String.join(", ", params);

        ApiErrorResponse response = ApiErrorResponse.from(ErrorCode.VALIDATION_FAILED);
        response.changeMessage(errorMessage);

        return response;
    }

}
