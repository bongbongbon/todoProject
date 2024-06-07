package com.example.cicdtest.controller;

import com.example.cicdtest.entity.TestEntity;
import com.example.cicdtest.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping("/")
    public ResponseEntity<?> start() {

        TestEntity entity = TestEntity.builder()
                .title("타이틀")
                .content("내용")
                .build();

        return ResponseEntity.ok(testRepository.save(entity));
    }
}
