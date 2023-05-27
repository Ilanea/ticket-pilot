package com.example.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Status extends AbstractEntity {
    private String statusName;

    public Status() {

    }

    public Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String name) {
        this.statusName = name;
    }

}
