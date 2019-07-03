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
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    public int addToTasks(Task task) {
        tasks.add(task);
        return tasks.size();
    }

    public Task getTask(int position) {
        return tasks.get(position);
    }

    public void removeFromTasks(int position) {
        tasks.remove(position);
    }

    public void setTaskIsComplete(int position, boolean complete) {
        tasks.get(position).setIsComplete(complete);
    }

    public void renameTask(int position, String title) {
        tasks.get(position).setTitle(title);
    }
}
