package com.example.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Priority extends AbstractEntity {
    private String priorityName;

    public Priority() {

    }

    public Priority(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String name) {
        this.priorityName = name;
    }

}
