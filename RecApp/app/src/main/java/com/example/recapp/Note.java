package com.example.recapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "imgReference")
    private String imgReference;

    public Note() {
    }

    public Note(long id, String name, String description, String imgReference) {
        this.id = id;
        this.title = name;
        this.description = description;
        this.imgReference = imgReference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImgReference() {
        return imgReference;
    }

    public void setImgReference(String imgReference) {
        this.imgReference = imgReference;
    }
}
