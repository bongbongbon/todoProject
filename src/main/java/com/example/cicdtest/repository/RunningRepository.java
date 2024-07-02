package com.example.cicdtest.repository;

import com.example.cicdtest.domain.running.Running;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunningRepository extends JpaRepository<Running, Long> {

    //필터 조회쿼리 (더 수정해야함)
    @Query("SELECT r FROM Running r WHERE r.date = :date " +
            "AND (:startLocation IS NULL OR r.startLocation LIKE %:startLocation%) " +
            "AND (:minDistance IS NULL OR r.distance >= :minDistance) " +
            "AND (:maxDistance IS NULL OR r.distance < :maxDistance)")
    List<Running> findRunnings(@Param("date") LocalDate date, @Param("startLocation") String startLocation,
                                                        @Param("minDistance") Double minDistance,
                                                        @Param("maxDistance") Double maxDistance);
}
