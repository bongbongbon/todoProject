package com.example.cicdtest.domain.boards;

import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Table(name = "boards")
@Getter
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;


}
