package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.todoapp.R;
import com.example.todoapp.data.model.Task;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.addButton01)
    Button addButton;

    @BindView(R.id.taskTitle)
    EditText taskTitle;

    @BindView(R.id.tasksRecyclerView)
    RecyclerView taskRecyclerView;

    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    MainViewModel mainViewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        taskAdapter = new TaskAdapter(mainViewModel,taskList);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        addButton.setOnClickListener(view -> addTask());

    }

    @Override
    protected void onPause() {
        super.onPause();
        //setTasks();
        compositeDisposable.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }

    public void setTasks() {
        mainViewModel.setTasks(taskList);
    }

    public void getTasks() {

        mainViewModel.getTasks().subscribe(new Observer<List<Task>>() {
            @Override
            public void onSubscribe(Disposable d) {
/*
                compositeDisposable.add(d);
*/
            }

            @Override
            public void onNext(List<Task> tasks) {
                if (tasks == null) {
                    return;
                }
                addAllToTaskList(tasks);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("getTasks", e.getMessage());
                Toast.makeText(MainActivity.this, "getTasks -> onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void addAllToTaskList(List<Task> tasks) {
        taskList.addAll(tasks);
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        taskAdapter.notifyDataSetChanged();
    }

    public void notifyItemInserted() {
        int countTasks = taskAdapter.getItemCount();
        taskAdapter.notifyItemInserted(countTasks);
        taskRecyclerView.scrollToPosition(countTasks-1);
    }

    public void addTask() {
        String newTaskTitle = taskTitle.getText().toString();
        if (newTaskTitle.equals("")) {
            return;
        }
        Task task = new Task(newTaskTitle);
        mainViewModel.addToTasks(task);
        taskList.add(task);
        clearNewTaskTitle();
        notifyItemInserted();
    }

    public void clearNewTaskTitle() {
        taskTitle.setText("");
    }

}
