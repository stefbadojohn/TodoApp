package com.example.todoapp.ui;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;
import com.example.todoapp.data.model.Task;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    MainViewModel mainViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TodoItemActionsListener todoItemActionsListener;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public TaskAdapter(MainViewModel viewModel, List<Task> taskList, TodoItemActionsListener todoItemActionsListener) {
        mainViewModel = viewModel;
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

        holder.taskTitle.setText(task.getTitle());

        holder.bind(position);

        // Can be replaced with onCheckedChangeListener
        //  WARNING: onCheckedChangeListener gets triggered on RecyclerView scrolling
        //  so double checking with compoundButton.isPressed() is necessary!
        holder.checkedTask.setOnClickListener(view -> {
            mainViewModel.setTaskIsComplete(position, !task.getIsComplete());
            if (holder.checkedTask.isChecked()) {
                holder.taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.taskTitle.setPaintFlags(0);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (todoItemActionsListener == null ) {
                return false;
            }
            todoItemActionsListener.onItemRename(position);
            return false;
        });

        holder.removeButton.setOnClickListener(view -> {
            if (todoItemActionsListener == null) {
                return;
            }
            Log.d("removeItem", "Position of item to be removed: " + position);
            todoItemActionsListener.onItemDelete(position);
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

        void bind(int position) {
            if (taskList.get(position).getIsComplete()) {
                checkedTask.setChecked(true);
                taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                checkedTask.setChecked(false);
                taskTitle.setPaintFlags(0);
            }
        }
    }
}
