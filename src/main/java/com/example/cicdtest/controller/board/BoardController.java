package com.example.cicdtest.controller.board;

import com.example.cicdtest.common.response.ApiSuccessResponse;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.dto.BoardCreateRequest;
import com.example.cicdtest.dto.BoardResponse;
import com.example.cicdtest.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
     public ResponseEntity<?> createBoard(@AuthenticationPrincipal User user, @RequestBody BoardCreateRequest request) {

       BoardResponse boardResponse = boardService.createBoard(user, request);

        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @GetMapping("/main")
    public ApiSuccessResponse<List<BoardResponse>> getMainBoardList(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        return ApiSuccessResponse.from(boardService.getMainBoardList(page, pageSize));
    }

}
