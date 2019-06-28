package com.example.todoapp.data;

import com.example.todoapp.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    public void addToTasks(Task task) {
        tasks.add(task);
    }

    public void removeFromTasks(int position) {
        tasks.remove(position);
    }
}
