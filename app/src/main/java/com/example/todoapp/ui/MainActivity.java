package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    FragmentManager fragmentManager = getSupportFragmentManager();
    TaskRenameFragment taskRenameFragment;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        taskAdapter = new TaskAdapter(mainViewModel, taskList, new TodoItemActionsListener() {
            @Override
            public void onItemRename(int position) {
                renameTask(position);
            }

            @Override
            public void onItemDelete(int position) {
                deleteTask(position);
            }
        });

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        addButton.setOnClickListener(view -> addTask());

    }

    @Override
    protected void onPause() {
        super.onPause();
        //setTasks();
        removeTaskRenameFragment();
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
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<Task> tasks) {
                if (tasks == null) {
                    return;
                }
                taskList = tasks;
                taskAdapter.setTaskList(tasks);
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

    public void addTask() {
        String newTaskTitle = taskTitle.getText().toString();
        if (newTaskTitle.equals("")) {
            return;
        }
        Task task = new Task(newTaskTitle);
        taskList.add(0, task);
        taskAdapter.notifyItemInserted(0);
        taskRecyclerView.scrollToPosition(0);
        clearNewTaskTitle();
    }

    public void clearNewTaskTitle() {
        taskTitle.setText("");
    }

    public void deleteTask(int position) {
        Log.d("taskActions", "taskList.size before delete : " + taskList.size());

        mainViewModel.removeFromTasks(position);
        getTasks();
        Log.d("removeItem", "Notify of removed item on: " + position);
        taskAdapter.notifyItemRemoved(position);
    }

    public void renameTask(int position) {
        String taskOldTitle = mainViewModel.getTask(position).getTitle();
        taskRenameFragment = new TaskRenameFragment(taskOldTitle, new TaskRenameActionsListener() {
            @Override
            public void onCancel() {
                removeTaskRenameFragment();
            }

            @Override
            public void onRename(String taskNewTitle) {
                mainViewModel.renameTask(position, taskNewTitle);
                taskAdapter.notifyDataSetChanged();
                removeTaskRenameFragment();
            }
        });
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.taskRenameFragmentContainer, taskRenameFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void removeTaskRenameFragment() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

}
