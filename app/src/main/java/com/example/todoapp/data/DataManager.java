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
        //task.setId(tasks.size()-1);
        return tasks.size();
    }

    public void removeFromTasks(int position) {
        tasks.remove(position);
    }

    public void setTaskIsComplete(int id, boolean complete) {
        tasks.get(id).setIsComplete(complete);
    }

    public void setTaskTitle(int id, String title) {
        tasks.get(id).setTitle(title);
    }
}
