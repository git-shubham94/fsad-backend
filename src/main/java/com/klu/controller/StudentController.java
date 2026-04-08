package com.klu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.klu.model.Student;
import com.klu.service.StudentService;
import com.klu.service.EmailService;
import com.klu.security.JwtUtil;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailService emailService;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // ✅ GET /students/test — ping to verify backend + DB is working
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        try {
            long count = service.getAll().size();
            return ResponseEntity.ok(Map.of(
                "status", "Backend is running ✅",
                "database", "Connected ✅",
                "studentCount", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", "DB Error ❌", "message", e.getMessage()));
        }
    }

    // ✅ POST /students/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Student s) {
        try {
            // Basic validation
            if (s.getEmail() == null || s.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email is required"));
            }
            if (s.getPassword() == null || s.getPassword().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password is required"));
            }
            if (s.getName() == null || s.getName().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Name is required"));
            }

            // Check if email already exists
            if (service.existsByEmail(s.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists"));
            }

            Student saved = service.register(s);
            System.out.println("[REGISTER] Success: " + saved.getEmail());
            
            // 📧 Trigger Welcome Email
            emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
            
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.err.println("[REGISTER ERROR] " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ POST /students/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student s) {
        try {
            System.out.println("[LOGIN] Attempt: " + s.getEmail());
            Student found = service.login(s.getEmail(), s.getPassword());
            if (found != null) {
                System.out.println("[LOGIN] Success: " + found.getEmail());
                String token = jwtUtil.generateToken(found.getEmail());
                
                Map<String, Object> response = new java.util.HashMap<>();
                response.put("token", token);
                response.put("user", found);
                
                return ResponseEntity.ok(response);
            } else {
                System.out.println("[LOGIN] Failed: " + s.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }
        } catch (Exception e) {
            System.err.println("[LOGIN ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ GET /students
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Student> students = service.getAll();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            System.err.println("[GET STUDENTS ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ GET /students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            Student student = service.getStudentById(id);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Student not found"));
            }
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ PUT /students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        try {
            Student existing = service.getStudentById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Student not found"));
            }

            // Validations
            if (updatedStudent.getName() == null || updatedStudent.getName().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Name is required"));
            }
            if (updatedStudent.getEmail() == null || updatedStudent.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }
            if (!updatedStudent.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body(Map.of("message", "Valid email is required"));
            }
            if (updatedStudent.getRollNumber() == null || updatedStudent.getRollNumber().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Roll Number is required"));
            }
            if (updatedStudent.getDepartment() == null || updatedStudent.getDepartment().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Department is required"));
            }
            if (updatedStudent.getCohort() == null || updatedStudent.getCohort().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Cohort is required"));
            }

            // Duplicate Email Check (if email changed)
            if (!existing.getEmail().equalsIgnoreCase(updatedStudent.getEmail())) {
                if (service.existsByEmail(updatedStudent.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Email already in use"));
                }
            }

            // Duplicate Roll Number Check (if roll number changed)
            if (!existing.getRollNumber().equalsIgnoreCase(updatedStudent.getRollNumber())) {
                Student sByRoll = service.findByRollNumber(updatedStudent.getRollNumber());
                if (sByRoll != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Roll number already exists"));
                }
            }

            // Update existing student details
            existing.setName(updatedStudent.getName());
            existing.setEmail(updatedStudent.getEmail());
            existing.setRollNumber(updatedStudent.getRollNumber());
            existing.setDepartment(updatedStudent.getDepartment());
            existing.setCohort(updatedStudent.getCohort());
            
            if (updatedStudent.getPhone() != null) {
                existing.setPhone(updatedStudent.getPhone());
            }
            if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isBlank()) {
                existing.setPassword(updatedStudent.getPassword());
            }

            Student saved = service.updateStudent(existing);
            
            // 📧 Trigger Update Email Notification
            emailService.sendProfileUpdateEmail(saved.getEmail(), saved.getName());
            
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            System.err.println("[UPDATE STUDENT ERROR] " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ DELETE /students/{id} — Remove student + all their achievements & participations
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteStudent(id);
            System.out.println("[DELETE] Student id=" + id + " and all data removed.");
            return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
        } catch (Exception e) {
            System.err.println("[DELETE STUDENT ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }
}

