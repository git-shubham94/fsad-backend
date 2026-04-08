package com.klu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.klu.model.Participation;
import com.klu.service.ParticipationService;
import com.klu.service.StudentService;

@RestController
@RequestMapping("/participations")
public class ParticipationController {

    private final ParticipationService service;
    private final StudentService studentService;

    public ParticipationController(ParticipationService service, StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
    }

    // ✅ POST /participations — Add a new participation record
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Participation p) {
        try {
            if (p.getActivityName() == null || p.getActivityName().isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Activity name is required"));
            }
            if (p.getStudentId() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student ID is required"));
            }
            Participation saved = service.save(p);
            System.out.println("[PARTICIPATION] Saved: " + saved.getActivityName() + " for studentId=" + saved.getStudentId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("[PARTICIPATION ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ GET /participations — Get all participations
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ GET /participations/student/{id} — Get participations for a specific student
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

    // ✅ PUT /participations/{id} — Update a participation record
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Participation p) {
        try {
            Participation updated = service.update(id, p);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Participation not found with id: " + id));
            }
            System.out.println("[PARTICIPATION] Updated id=" + id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("[PARTICIPATION UPDATE ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }

    // ✅ DELETE /participations/{id} — Delete a participation record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            System.out.println("[PARTICIPATION] Deleted id=" + id);
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } catch (Exception e) {
            System.err.println("[PARTICIPATION DELETE ERROR] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Not found: " + e.getMessage()));
        }
    }
}
