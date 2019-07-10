package com.example.todoapp.ui;

public interface TodoItemActionsListener {
    void onItemRename(int position, int taskId);
    void onItemDelete(int position, int taskId);
    void onItemCheckedChange(int position, int taskId, boolean checked);
}
