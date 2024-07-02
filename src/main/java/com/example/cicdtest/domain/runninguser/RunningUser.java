package com.example.cicdtest.domain.runninguser;

import com.example.cicdtest.domain.running.Running;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.util.List;

@Table(name = "running_users")
@Getter
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RunningUser extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "running_user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "running_id")
    @ToString.Exclude
    private Running running;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    private String role;
}
