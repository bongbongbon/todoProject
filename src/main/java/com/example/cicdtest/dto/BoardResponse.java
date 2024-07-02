package com.example.cicdtest.dto;

import com.example.cicdtest.domain.boards.Board;
import com.example.cicdtest.domain.users.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private String title;
    private String content;

    // Entity -> DTO
    public static BoardResponse fromEntity(Board board) {
        return BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    // List<Board>를 List<BoardResponse>로 변환
    public static List<BoardResponse> fromEntity(List<Board> boards) {
        return boards.stream()
                .map(BoardResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
