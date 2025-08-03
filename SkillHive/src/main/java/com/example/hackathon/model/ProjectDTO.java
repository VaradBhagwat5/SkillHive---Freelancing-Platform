package com.example.hackathon.model;

import java.time.LocalDate;

public class ProjectDTO {
    private String title;
    private String description;
    private Double budget;
    private LocalDate deadline;
    private ProjectStatus status;
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectDTO(String title, String description, Double budget, LocalDate deadline, ProjectStatus status) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.status = status;
    }

    public ProjectDTO() {
        super();
    }
}
