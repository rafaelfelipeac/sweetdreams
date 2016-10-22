package com.sd.rafael.sweetdreams.models;

import java.io.Serializable;

/**
 * Created by rafae on 22/10/2016.
 */

public class Dream implements Serializable{
    private Long id;
    private String title;
    private String description;
    private Double grade;

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setDescription(String description) {
        this.description = description;
    }
}
