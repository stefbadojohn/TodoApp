package com.example.todoapp.ui;

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

    public TaskAdapter(MainViewModel viewModel, List<Task> taskList) {
        mainViewModel = viewModel;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        if (task == null) {
            return;
        }

        holder.taskTitle.setText(task.getTitle());

        holder.bind(position);

        // Is this (setOnCheckedChangeListener) gets triggered on RecyclerView's scroll?
        /*holder.checkedTask.setOnCheckedChangeListener((compoundButton, b) -> {
            mainViewModel.setTaskIsComplete(task.getId(), b);
        });*/

        holder.checkedTask.setOnClickListener(view -> {
            mainViewModel.setTaskIsComplete(position, !task.getIsComplete());
            if (holder.checkedTask.isChecked()) {
                holder.taskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.taskTitle.setPaintFlags(0);
            }
        });

        // TODO: Implement Task renaming (using a Fragment?)
        holder.taskTitle.setOnLongClickListener(view -> {
            Toast.makeText(view.getContext(), "Rename Task", Toast.LENGTH_SHORT).show();
            return false;
        });

        holder.removeButton.setOnClickListener(view -> {
            mainViewModel.removeFromTasks(position).subscribe(new Observer<List<Task>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onNext(List<Task> tasks) {
                    taskList = tasks;
                    notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("removeFromTasks", e.getMessage());
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete() {

                }
            });

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
