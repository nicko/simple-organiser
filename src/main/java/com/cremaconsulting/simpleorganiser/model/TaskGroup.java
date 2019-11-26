package com.cremaconsulting.simpleorganiser.model;

import javax.persistence.*;

@Entity
@Table(name = "task_group")
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer position;
    private String status;

    protected TaskGroup() {
    }

    public TaskGroup(String name, Integer position, String status) {
        this.name = name;
        this.position = position;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", status='" + status + '\'' +
                '}';
    }
}
