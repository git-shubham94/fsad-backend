package com.klu.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.klu.model.Achievement;
import com.klu.repository.AchievementRepository;

@Service
public class AchievementService {

    private final AchievementRepository repo;

    public AchievementService(AchievementRepository repo) {
        this.repo = repo;
    }

    public Achievement save(Achievement a) {
        return repo.save(a);
    }

    public List<Achievement> getAll() {
        return repo.findAll();
    }

    public List<Achievement> getByStudentId(Long id) {
        return repo.findByStudentId(id);
    }

    // ✅ UPDATE: partial update by id
    public Achievement update(Long id, Achievement updated) {
        Optional<Achievement> existing = repo.findById(id);
        if (existing.isEmpty()) return null;

        Achievement a = existing.get();
        if (updated.getTitle()            != null) a.setTitle(updated.getTitle());
        if (updated.getDescription()      != null) a.setDescription(updated.getDescription());
        if (updated.getCategory()         != null) a.setCategory(updated.getCategory());
        if (updated.getActivityCategory() != null) a.setActivityCategory(updated.getActivityCategory());
        if (updated.getDate()             != null) a.setDate(updated.getDate());

        return repo.save(a);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
