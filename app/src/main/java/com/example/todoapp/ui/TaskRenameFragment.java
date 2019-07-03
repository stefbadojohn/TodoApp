package com.example.todoapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskRenameFragment extends Fragment {

    @BindView(R.id.renameButton)
    Button renameButton;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.taskNewTitle)
    EditText taskNewTitle;

    private String taskOldTitle;

    private TaskRenameActionsListener taskRenameActionsListener;

    public TaskRenameFragment(String taskOldTitle, TaskRenameActionsListener taskRenameActionsListener) {
        this.taskOldTitle = taskOldTitle;
        this.taskRenameActionsListener = taskRenameActionsListener;
    }

    public TaskRenameActionsListener getTaskRenameActionsListener() {
        return taskRenameActionsListener;
    }

    public void setTaskRenameActionsListener(TaskRenameActionsListener taskRenameActionsListener) {
        this.taskRenameActionsListener = taskRenameActionsListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_rename, container, false);
        ButterKnife.bind(this, view);

        taskNewTitle.setText(taskOldTitle);

        renameButton.setOnClickListener(v ->
                //HideKeyboard
                taskRenameActionsListener.onRename(taskNewTitle.getText().toString()));
        cancelButton.setOnClickListener(v ->
                //HideKeyboard
                taskRenameActionsListener.onCancel());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskNewTitle.requestFocus();
        //ShowKeyboard

    }
    // TODO: 03-Jul-19 Implement Show/Hide Keyboard
}
