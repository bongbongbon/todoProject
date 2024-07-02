package com.example.cicdtest.repository;

import com.example.cicdtest.domain.runninguser.RunningUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningUserRepository extends JpaRepository<RunningUser, Long> {
    List<RunningUser> findAllByRunningId(Long runningId);
}
