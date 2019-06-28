package com.example.todoapp.data.model;

public class Task {
    private int id;
    private String title;
    private boolean isComplete;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        isComplete = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsComplete() {
        return isComplete;
    }
}
