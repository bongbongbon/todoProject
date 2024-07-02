package com.example.cicdtest.repository;

import com.example.cicdtest.domain.crew.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
}
