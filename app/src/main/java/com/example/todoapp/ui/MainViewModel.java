package com.example.todoapp.ui;

import androidx.lifecycle.ViewModel;
import com.example.todoapp.data.DataManager;
import com.example.todoapp.data.model.Task;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends ViewModel {

    private DataManager dataManager = new DataManager();

    public Observable<List<Task>> getTasks() {
        return Observable.just(dataManager.getTasks());
    }

    public void setTasks(List<Task> tasks) {
        dataManager.setTasks(tasks);
    }

    public void addToTasks(Task task) {
        dataManager.addToTasks(task);
    }

    public Observable<List<Task>> removeFromTasks(int position) {
        dataManager.removeFromTasks(position);
        return getTasks();
    }

    public void setTaskIsComplete(int id, boolean isComplete) {
        dataManager.setTaskIsComplete(id, isComplete);
    }

    public void setTaskTitle(int id, String title) {
        dataManager.setTaskTitle(id, title);
    }
}
