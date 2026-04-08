package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Participation;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByStudentId(Long studentId);
    // ✅ ADDED: bulk delete all participations for a student (cascade on student delete)
    void deleteByStudentId(Long studentId);
}