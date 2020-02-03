package com.cremaconsulting.simpleorganiser.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String status;
    private Integer position;
    // TODO: Refactor these out to be configurable phases
    private LocalDateTime startedOn;
    private LocalDateTime endedOn;

    @OneToMany
    @JoinColumn(name = "task_id")
    private List<Comment> comments;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "NEW";
        this.position = 0;
        this.startedOn = null;
        this.endedOn = null;
    }

    public Task(String title, String status, Integer position) {
        this(title, null);
        this.status = status;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LocalDateTime getStartedOn() {
        return startedOn;
    }

    public LocalDateTime getEndedOn() {
        return endedOn;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", position=" + position +
                ", startedOn=" + startedOn +
                ", endedOn=" + endedOn +
                '}';
    }
}
