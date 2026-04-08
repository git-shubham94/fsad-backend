package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Achievement;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByStudentId(Long studentId);
    // ✅ ADDED: bulk delete all achievements for a student (cascade on student delete)
    void deleteByStudentId(Long studentId);
}