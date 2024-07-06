package com.example.cicdtest.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    public static final CustomException USER_NOT_FOUND = new CustomException(ErrorCode.USER_NOT_FOUND);
    public static final CustomException PASSWORD_NOT_MATCH = new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
    public static final CustomException ALREADY_USER_EXISTS = new CustomException(ErrorCode.ALREADY_USER_EXISTS);
    public static final CustomException RUNNING_NOT_FOUND= new CustomException(ErrorCode.RUNNING_NOT_FOUND);
    public static final CustomException ALREADY_JOIN_THIS_TEAM = new CustomException(ErrorCode.ALREADY_JOIN_THIS_TEAM);
    public static final CustomException TEAM_USER_IS_FULL = new CustomException(ErrorCode.TEAM_USER_IS_FULL);
    public static final CustomException YOU_ARE_RUNNING_LEADER = new CustomException(ErrorCode.YOU_ARE_RUNNING_LEADER);
    public static final CustomException YOU_ARE_NOT_RUNNING_LEADER = new CustomException(ErrorCode.YOU_ARE_NOT_RUNNING_LEADER);
    public static final CustomException USER_INFO_NOT_MATCH = new CustomException(ErrorCode.USER_INFO_NOT_MATCH);
    public static final CustomException FAILED_FILE_UPLOAD = new CustomException(ErrorCode.FAILED_FILE_UPLOAD);
    public static final CustomException INTERNAL_SERVER_ERROR = new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    public static final CustomException INVALID_IMAGE_EXTENSION = new CustomException(ErrorCode.INVALID_IMAGE_EXTENSION);


    private final ErrorCode errorCode;

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

}
