package com.example.cicdtest.service;

import com.example.cicdtest.common.exception.CustomException;
import com.example.cicdtest.domain.boards.Board;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.dto.BoardCreateRequest;
import com.example.cicdtest.dto.BoardResponse;
import com.example.cicdtest.repository.BoardRepository;
import com.example.cicdtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponse createBoard(User user, BoardCreateRequest request) {

        User hostUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> CustomException.USER_NOT_FOUND);


        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();


        return BoardResponse.fromEntity(boardRepository.save(board));
    }

    public List<BoardResponse> getMainBoardList( Integer page, Integer pageSize) {

        // Pagination을 위한 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, pageSize);

        // 데이터베이스에서 페이징된 결과를 가져옴
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // List<Board>를 List<BoardResponse>로 변환
        List<BoardResponse> boardResponseList = BoardResponse.fromEntity(boardPage.getContent());

        return boardResponseList;
    }
}
