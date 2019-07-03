package com.example.todoapp.ui;

public interface TodoItemActionsListener {
    void onItemRename(int position);
    void onItemDelete(int position);
}
