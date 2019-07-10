package com.example.todoapp.data;

import com.example.todoapp.data.model.Task;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DataManager implements DataManagerInterface {
    private Realm realm = Realm.getDefaultInstance();

    // TODO: 10-Jul-19 Find a way to close realm

    private int getNextTaskId() {
        int nextId;
        Number currentId = realm.where(Task.class).max("id");

        if (currentId == null) {
            nextId = 0;
        } else {
            nextId = currentId.intValue() + 1;
        }

        return nextId;
    }

    public List<Task> getTasks() {
        RealmResults<Task> realmResults = realm.where(Task.class).sort("id", Sort.DESCENDING).findAll();
        return realm.copyFromRealm(realmResults);
    }

    // No need to use this since tasks are stored in the DB.
    public void setTasks(List<Task> tasks) {
        //this.tasks = tasks;
    }

    public Task addToTasks(String newTaskTitle) {
        realm.beginTransaction();
        Task task = realm.createObject(Task.class, getNextTaskId());
        task.setTitle(newTaskTitle);
        realm.commitTransaction();

        return task;
    }

    public Task getTask(int taskId) {
        return realm.where(Task.class).equalTo("id", taskId).findFirst();
    }

    public void removeFromTasks(int taskId) {
        realm.executeTransaction(realm -> {
            Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
            if (task != null) {
                task.deleteFromRealm();
            }
        });
    }

    public void setTaskIsComplete(int taskId, boolean complete) {
        realm.executeTransaction(realm -> {
            Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
            if (task != null) {
                task.setIsComplete(complete);
            }
        });
    }

    public void renameTask(int taskId, String title) {
        realm.executeTransaction(realm -> {
            Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
            if (task != null && title != null) {
                task.setTitle(title);
            }
        });
    }

}
