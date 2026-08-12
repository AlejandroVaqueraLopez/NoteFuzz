package com.example.notefuzz.model;

public class Note {
    private long id;
    private String createdAt;
    private String title;
    private String description;
    private String editedAt;
    private int status;

    public Note() {
    }

    //constructor para clase Note
    public Note(long id, String createdAt, String title, String description, String editedAt, int status) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.editedAt = editedAt;
        this.status = status;
    }

    //setters y getters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(String editedAt) {
        this.editedAt = editedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
