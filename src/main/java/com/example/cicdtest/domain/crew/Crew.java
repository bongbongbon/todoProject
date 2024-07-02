package com.example.cicdtest.domain.crew;


import com.example.cicdtest.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Table(name = "crews")
@Getter
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
@NoArgsConstructor
@AllArgsConstructor
public class Crew extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    private String title;

    private String content;

    private Long like;

}
