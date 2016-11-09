package com.sd.rafael.sweetdreams.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rafae on 22/10/2016.
 */

public class Dream implements Serializable{
    private Long id;
    private String title;
    private String description;
    private Double grade;
    private String tags;
    private int day;
    private int month;
    private int year;

    private String separator = ", ";

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags.toLowerCase();
    }

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

    public String[] tagConvertStringToArray() {
        String[] array = getTags().split(separator);
        return array;
    }

    public String tagConvertArrayToString(String[] array) {;
        String s = "";

        for(int i = 0; i< array.length; i++) {
            if(array[i] != null && array[i] != "null") {
                if(array[i].endsWith(" "))
                    array[i] = array[i].substring(0, array[i].length() -1);

                s += array[i];
                if(i < array.length -1)
                    s += separator;
            }
        }

        return s;
    }
}
