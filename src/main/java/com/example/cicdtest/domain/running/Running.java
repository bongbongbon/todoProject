package com.example.cicdtest.domain.running;

import com.example.cicdtest.domain.runninguser.RunningUser;
import com.example.cicdtest.domain.users.User;
import com.example.cicdtest.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "runnings")
@Getter
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Running extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "running_id")
    private Long id;

    private String title;

    private String content;

    private Double distance;

    private String startLocation;

    private String startDetailLocation;

    private String finishLocation;

    private String finishDetailLocation;

    private Integer limitedPeople;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "running", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<RunningUser> runningUserList = new ArrayList<>();


}
