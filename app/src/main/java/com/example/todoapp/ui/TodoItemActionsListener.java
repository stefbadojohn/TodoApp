package com.example.todoapp.ui;

public interface TodoItemActionsListener {
    void onItemRename(int taskId);
    void onItemDelete(int taskId);
}
