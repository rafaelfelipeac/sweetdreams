package com.rafaelfelipeac.sweetdreams.models;

import java.io.Serializable;

/**
 * Created by Rafael Cordeiro on 22/10/2016.
 */

public class Dream implements Serializable{
    private Long id;
    private String title;
    private String description;
    private String tags;
    private int day;
    private int month;
    private int year;
    private boolean favorite;
    private String audioPath;

    private String separator = ",";

    public Dream() {
        title = "";
        description = "";
        tags = "";
        audioPath = "";
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

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
}
