package com.arturola.graphql.datasource.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="problemz")
public class Problemz {
    @Id
    private String id;
    @CreationTimestamp
    private LocalDateTime creationTimestamp;
    private String title;
    private String content;
    private String tags;

    @OneToMany(mappedBy = "problemz")
    @OrderBy("creationTimestamp desc")
    private List<Solutionz> solutions;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Userz createdBy;

    public List<Solutionz> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solutionz> solutions) {
        this.solutions = solutions;
    }

    public Userz getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Userz createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
