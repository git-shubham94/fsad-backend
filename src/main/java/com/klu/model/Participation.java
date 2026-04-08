package com.klu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// ✅ @NotBlank removed — manual validation is done in ParticipationController
// Hibernate's entity-level validation was firing even without @Valid in the controller
@Entity
@Table(name = "participations")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityName;

    private String activityCategory;

    private String role;

    private String duration;

    // Stored as comma-separated string in DB
    private String skills;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long studentId;

    // ✅ No-arg constructor (required by JPA)
    public Participation() {}

    // ✅ Getters
    public Long getId()                  { return id; }
    public String getActivityName()      { return activityName; }
    public String getActivityCategory()  { return activityCategory; }
    public String getRole()              { return role; }
    public String getDuration()          { return duration; }
    public String getSkills()            { return skills; }
    public LocalDate getStartDate()      { return startDate; }
    public LocalDate getEndDate()        { return endDate; }
    public Long getStudentId()           { return studentId; }

    // ✅ Setters
    public void setId(Long id)                               { this.id = id; }
    public void setActivityName(String activityName)         { this.activityName = activityName; }
    public void setActivityCategory(String activityCategory) { this.activityCategory = activityCategory; }
    public void setRole(String role)                         { this.role = role; }
    public void setDuration(String duration)                 { this.duration = duration; }
    public void setSkills(String skills)                     { this.skills = skills; }
    public void setStartDate(LocalDate startDate)            { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)                { this.endDate = endDate; }
    public void setStudentId(Long studentId)                 { this.studentId = studentId; }
}
