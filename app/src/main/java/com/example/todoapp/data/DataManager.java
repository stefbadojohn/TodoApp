package com.example.todoapp.data;

import com.example.todoapp.data.model.Task;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DataManager {
    private Realm realm = Realm.getDefaultInstance();

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

    public List<Task> getTasksFromDb() {
        RealmResults<Task> realmResults = realm.where(Task.class).sort("id", Sort.DESCENDING).findAll();
        return realm.copyFromRealm(realmResults);
    }

    // No need to use this since tasks are stored in the DB.
    public void setTasks(List<Task> tasks) {
        //this.tasks = tasks;
    }

    public void addToTasks(Task task) {
        realm.executeTransaction(realm -> {
            task.setId(getNextTaskId());
            realm.insertOrUpdate(task);
        });
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
        // TODO: 08-Jul-19 Fix rename (on MainActivity?)
        //  to update RecyclerView's Item with the new name
        realm.executeTransaction(realm -> {
            Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
            if (task != null && title != null) {
                task.setTitle(title);
            }
        });
    }

}
