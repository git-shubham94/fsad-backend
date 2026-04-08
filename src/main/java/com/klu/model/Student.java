package com.klu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    // ✅ ADDED: Additional fields required by frontend Register form
    private String rollNumber;
    private String cohort;
    private String department;
    private String phone;
    
    // ✅ JWT Role-Based Security
    private String role = "STUDENT";

    // ✅ REQUIRED (no-arg constructor for JPA)
    public Student() {}

    // ✅ GETTERS
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRollNumber() { return rollNumber; }
    public String getCohort() { return cohort; }
    public String getDepartment() { return department; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }

    // ✅ SETTERS
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public void setCohort(String cohort) { this.cohort = cohort; }
    public void setDepartment(String department) { this.department = department; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(String role) { this.role = role; }
}