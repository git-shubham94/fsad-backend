package com.klu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    // ✅ RESTORED: 'award' | 'recognition' | 'participation'
    private String category;

    // ✅ RESTORED: activity type e.g. 'technical', 'sports', etc.
    private String activityCategory;

    private LocalDate date;

    private Long studentId;

    // ✅ No-arg constructor (required by JPA)
    public Achievement() {}

    // ✅ Getters
    public Long getId()                  { return id; }
    public String getTitle()             { return title; }
    public String getDescription()       { return description; }
    public String getCategory()          { return category; }
    public String getActivityCategory()  { return activityCategory; }
    public LocalDate getDate()           { return date; }
    public Long getStudentId()           { return studentId; }

    // ✅ Setters
    public void setId(Long id)                               { this.id = id; }
    public void setTitle(String title)                       { this.title = title; }
    public void setDescription(String description)           { this.description = description; }
    public void setCategory(String category)                 { this.category = category; }
    public void setActivityCategory(String activityCategory) { this.activityCategory = activityCategory; }
    public void setDate(LocalDate date)                      { this.date = date; }
    public void setStudentId(Long studentId)                 { this.studentId = studentId; }
}
