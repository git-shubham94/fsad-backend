package com.klu.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.klu.model.Participation;
import com.klu.repository.ParticipationRepository;

@Service
public class ParticipationService {

    private final ParticipationRepository repo;

    public ParticipationService(ParticipationRepository repo) {
        this.repo = repo;
    }

    public Participation save(Participation p) {
        return repo.save(p);
    }

    public List<Participation> getAll() {
        return repo.findAll();
    }

    public List<Participation> getByStudentId(Long id) {
        return repo.findByStudentId(id);
    }

    // ✅ UPDATE: find by id, apply new field values, save
    public Participation update(Long id, Participation updated) {
        Optional<Participation> existing = repo.findById(id);
        if (existing.isEmpty()) return null;

        Participation p = existing.get();
        // Only update fields that are provided (non-null)
        if (updated.getActivityName()     != null) p.setActivityName(updated.getActivityName());
        if (updated.getActivityCategory() != null) p.setActivityCategory(updated.getActivityCategory());
        if (updated.getRole()             != null) p.setRole(updated.getRole());
        if (updated.getDuration()         != null) p.setDuration(updated.getDuration());
        if (updated.getSkills()           != null) p.setSkills(updated.getSkills());
        if (updated.getStartDate()        != null) p.setStartDate(updated.getStartDate());
        if (updated.getEndDate()          != null) p.setEndDate(updated.getEndDate());
        // studentId intentionally not updated (ownership stays the same)

        return repo.save(p);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
