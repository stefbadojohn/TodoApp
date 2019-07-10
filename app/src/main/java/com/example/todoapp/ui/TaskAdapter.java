package com.example.todoapp.ui;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;
import com.example.todoapp.data.model.Task;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;

    private TodoItemActionsListener todoItemActionsListener;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        //notifyDataSetChanged();
    }

    public TaskAdapter(List<Task> taskList, TodoItemActionsListener todoItemActionsListener) {
        this.taskList = taskList;
        this.todoItemActionsListener = todoItemActionsListener;
    }

    public TodoItemActionsListener getTodoItemActionsListener() {
        return todoItemActionsListener;
    }

    public void setTodoItemActionsListener(TodoItemActionsListener todoItemActionsListener) {
        this.todoItemActionsListener = todoItemActionsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        if (task == null) {
            return;
        }

        holder.taskTitle.setText(task.getTitle()); // Set title

        holder.setTaskItemChecked(position); // Set checked

        holder.checkedTask.setOnCheckedChangeListener((compoundButton, b) -> { // Checked Change
            // Check if user actually clicked the checkbox or
            // onCheckedChangeListener got triggered just by scrolling
            if (compoundButton.isPressed()) {
                int itemPosition = holder.getAdapterPosition();
                if (todoItemActionsListener == null || itemPosition == -1) {
                    return;
                }
                todoItemActionsListener.onItemCheckedChange(itemPosition, taskList.get(itemPosition).getId(), b);

                if (b) {
                    holder.taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.taskTitle.setPaintFlags(0);
                }
            }
        });

        holder.itemView.setOnClickListener(view -> holder.switchTaskTitleMaxLines()); // Expand Task

        holder.itemView.setOnLongClickListener(view -> { // Rename Task
            int itemPosition = holder.getAdapterPosition();
            if (todoItemActionsListener == null || itemPosition == -1) {
                return false;
            }
            todoItemActionsListener.onItemRename(itemPosition, taskList.get(itemPosition).getId());
            return false;
        });

        holder.removeButton.setOnClickListener(view -> { // Remove Task
            int itemPosition = holder.getAdapterPosition();
            if (todoItemActionsListener == null || itemPosition == -1) {
                return;
            }
            int itemId = taskList.get(itemPosition).getId();

            todoItemActionsListener.onItemDelete(itemPosition, itemId);
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.taskTitle)
        TextView taskTitle;

        @BindView(R.id.checkTask)
        CheckBox checkedTask;

        @BindView(R.id.deleteTaskButton)
        Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setTaskItemChecked(int position) {
            if (taskList.get(position).getIsComplete()) {
                checkedTask.setChecked(true);
                taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                checkedTask.setChecked(false);
                taskTitle.setPaintFlags(0);
            }
        }

        void switchTaskTitleMaxLines() {
            if (taskTitle.getMaxLines() == 1) {
                taskTitle.setMaxLines(Integer.MAX_VALUE);
                taskTitle.setEllipsize(null);
            } else {
                taskTitle.setMaxLines(1);
                taskTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }
}
