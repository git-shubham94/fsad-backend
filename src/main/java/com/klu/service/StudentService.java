package com.klu.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.klu.model.Student;
import com.klu.repository.AchievementRepository;
import com.klu.repository.ParticipationRepository;
import com.klu.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final AchievementRepository achievementRepo;
    private final ParticipationRepository participationRepo;

    public StudentService(StudentRepository studentRepo,
                          AchievementRepository achievementRepo,
                          ParticipationRepository participationRepo) {
        this.studentRepo      = studentRepo;
        this.achievementRepo  = achievementRepo;
        this.participationRepo = participationRepo;
    }

    public Student register(Student s) {
        return studentRepo.save(s);
    }

    public boolean existsByEmail(String email) {
        return studentRepo.existsByEmail(email);
    }

    public Student login(String email, String password) {
        Student s = studentRepo.findByEmail(email);
        if (s != null && s.getPassword().equals(password)) {
            return s;
        }
        return null;
    }

    public List<Student> getAll() {
        return studentRepo.findAll();
    }

    /**
     * ✅ DELETE STUDENT + cascade delete all their achievements and participations.
     * @Transactional ensures all 3 deletes succeed or all roll back.
     */
    @Transactional
    public void deleteStudent(Long studentId) {
        // 1. Delete all achievements for this student
        achievementRepo.deleteByStudentId(studentId);
        System.out.println("[DELETE] Deleted achievements for studentId=" + studentId);

        // 2. Delete all participations for this student
        participationRepo.deleteByStudentId(studentId);
        System.out.println("[DELETE] Deleted participations for studentId=" + studentId);

        // 3. Delete the student
        studentRepo.deleteById(studentId);
        System.out.println("[DELETE] Deleted student id=" + studentId);
    }

    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElse(null);
    }

    public Student findByRollNumber(String rollNumber) {
        return studentRepo.findByRollNumber(rollNumber);
    }

    public Student updateStudent(Student s) {
        return studentRepo.save(s);
    }
}
