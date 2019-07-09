package com.example.todoapp.ui;

import androidx.lifecycle.ViewModel;
import com.example.todoapp.data.DataManager;
import com.example.todoapp.data.model.Task;
import java.util.List;
import io.reactivex.Observable;

public class MainViewModel extends ViewModel {

    private DataManager dataManager = new DataManager();

    public Observable<List<Task>> getTasks() {
        return Observable.just(dataManager.getTasksFromDb());
    }

    public Task getTask(int position) {
        return dataManager.getTask(position);
    }

    public void setTasks(List<Task> tasks) {
        dataManager.setTasks(tasks);
    }

    public void addToTasks(Task task) {
        dataManager.addToTasks(task);
    }

    public void removeFromTasks(int position) {
        dataManager.removeFromTasks(position);
    }

    public void setTaskIsComplete(int id, boolean isComplete) {
        dataManager.setTaskIsComplete(id, isComplete);
    }

    public void renameTask(int taskId, String title) {
        dataManager.renameTask(taskId, title);
    }
}
