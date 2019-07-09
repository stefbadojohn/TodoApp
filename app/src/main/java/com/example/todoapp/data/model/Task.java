package com.example.todoapp.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private boolean isComplete;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
        isComplete = false;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsComplete(boolean complete) {
        isComplete = complete;
    }
}
