package com.example.cicdtest.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "입력값 유효성 검사에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "PASSWORD_NOT_MATCH", "패스워드가 불일치 합니다."),
    ALREADY_USER_EXISTS(HttpStatus.BAD_REQUEST, "ALREADY_USER_EXISTS", "이미 유저가 존재합니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "ACCESS_DENIED", "유효한 인증 정보가 아닙니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN", "Access Token이 만료되었습니다. 토큰을 재발급해주세요"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    RUNNING_NOT_FOUND(HttpStatus.BAD_REQUEST, "RUNNING_NOT_FOUND", "런닝이 없습니다."),
    ALREADY_JOIN_THIS_TEAM(HttpStatus.BAD_REQUEST, "ALREADY_JOIN_THIS_TEAM", "이미 이 팀에 참여하였습니다."),
    TEAM_USER_IS_FULL(HttpStatus.BAD_REQUEST, "TEAM_USER_IS_FULL", "팀 인원이 마감되었습니다."),
    YOU_ARE_RUNNING_LEADER(HttpStatus.BAD_REQUEST, "YOU_ARE_RUNNING_LEADER", "이팀의 리더 입니다."),
    YOU_ARE_NOT_RUNNING_LEADER(HttpStatus.BAD_REQUEST, "YOU_ARE_NOT_RUNNING_LEADER", "팀리더가 아니여서 삭제,수정 불가합니다."),
    USER_INFO_NOT_MATCH(HttpStatus.BAD_REQUEST,"USER_INFO_NOT_MATCH","작성자만 내용을 수정할 수 있습니다."),
    FAILED_FILE_UPLOAD(HttpStatus.BAD_REQUEST, "FAILED_FILE_UPLOAD" , "파일 업로드에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "예상치 못한 서버에러가 발생하였습니다."),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "INVALID_IMAGE_EXTENSION", "처리할 수 없는 이미지 형식입니다.");





    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
