package com.example.todoapp.data;

import com.example.todoapp.data.model.Task;

import java.util.List;

public interface DataManagerInterface {
    List<Task> getTasks();
    void setTasks(List<Task> taskList);
    Task getTask(int taskId);
    Task addToTasks(String newTaskTitle);
    void removeFromTasks(int taskId);
    void setTaskIsComplete(int taskId, boolean complete);
    void renameTask(int taskId, String title);

    static DataManagerInterface instance = new DataManager();
}
