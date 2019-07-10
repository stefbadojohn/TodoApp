package com.example.todoapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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

        taskAdapter = new TaskAdapter(taskList, new TodoItemActionsListener() {
            @Override
            public void onItemRename(int position, int taskId) {
                renameTask(position, taskId);
            }

            @Override
            public void onItemDelete(int position, int taskId) {
                deleteTask(position, taskId);
            }

            @Override
            public void onItemCheckedChange(int position, int taskId, boolean checked) {
                changeTaskIsComplete(position, taskId, checked);
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
        taskAdapter.notifyDataSetChanged();
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
                if (e.getMessage() != null) {
                    Log.e("getTasks", e.getMessage());
                }
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

        Task task = mainViewModel.addToTasks(newTaskTitle);
        taskList.add(0, task);
        taskAdapter.notifyItemInserted(0);
        taskRecyclerView.scrollToPosition(0);
        clearNewTaskTitle();
    }

    public void clearNewTaskTitle() {
        taskTitle.setText("");
    }

    public void deleteTask(int position, int taskId) {
        mainViewModel.removeFromTasks(taskId);
        getTasks();
        taskAdapter.notifyItemRemoved(position);
    }

    public void renameTask(int position, int taskId) {
        String taskOldTitle = mainViewModel.getTask(taskId).getTitle();
        taskRenameFragment = new TaskRenameFragment(taskOldTitle, new TaskRenameActionsListener() {
            @Override
            public void onCancel() {
                removeTaskRenameFragment();
            }

            @Override
            public void onRename(String taskNewTitle) {
                mainViewModel.renameTask(taskId, taskNewTitle);
                removeTaskRenameFragment();
                getTasks();
                taskAdapter.notifyItemChanged(position);
            }
        });

        createTaskRenameFragment();
    }

    public void changeTaskIsComplete(int position, int taskId, boolean checked) {
        mainViewModel.setTaskIsComplete(taskId, checked);
        getTasks();
        // Do not use notifyItemChanged instead setPaintFlags in
        // TaskAdapter's onCheckedChangeListener to avoid task expansion/contraction
        //taskAdapter.notifyItemChanged(position);
    }

    public void createTaskRenameFragment() {
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
