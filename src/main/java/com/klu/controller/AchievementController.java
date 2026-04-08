package com.klu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.klu.model.Achievement;
import com.klu.service.AchievementService;
import com.klu.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService service;
    private final StudentService studentService;

    public AchievementController(AchievementService service, StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
    }

    // ✅ POST /achievements — Add a new achievement
    @PostMapping
    public ResponseEntity<Achievement> add(@Valid @RequestBody Achievement a) {
        try {
            Achievement saved = service.save(a);
            System.out.println("[ACHIEVEMENT] Saved: " + saved.getTitle() + " for studentId=" + saved.getStudentId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("[ACHIEVEMENT ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ GET /achievements — Get all achievements
    @GetMapping
    public List<Achievement> getAll() {
        return service.getAll();
    }

    // ✅ GET /achievements/student/{id} — Get achievements for a specific student
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getByStudent(@PathVariable Long id) {
        try {
            if (studentService.getStudentById(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Student not found with ID: " + id));
            }
            return ResponseEntity.ok(service.getByStudentId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ PUT /achievements/{id} — Update an achievement
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Achievement a) {
        try {
            Achievement updated = service.update(id, a);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Achievement not found with id: " + id));
            }
            System.out.println("[ACHIEVEMENT] Updated id=" + id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("[ACHIEVEMENT UPDATE ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ DELETE /achievements/{id} — Delete an achievement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            System.out.println("[ACHIEVEMENT] Deleted id=" + id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("[ACHIEVEMENT DELETE ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
