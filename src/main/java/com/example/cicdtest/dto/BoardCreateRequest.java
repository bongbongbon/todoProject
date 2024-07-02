package com.example.cicdtest.dto;

import com.example.cicdtest.domain.users.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateRequest {

    private String title;
    private String content;



}
