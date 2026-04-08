package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    boolean existsByEmail(String email); // ✅ ADDED: for duplicate email check
    Student findByRollNumber(String rollNumber); // ✅ ADDED: for duplicate roll number check
}
