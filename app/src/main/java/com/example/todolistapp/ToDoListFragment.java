package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment {
    private SQLiteDatabase db;
    private Listener listener;
    private ListView listView;
    private long taskId;
    private ArrayList<String> listTasksToDo;
    private ArrayAdapter<String> tasksAdapter;
//    View view;

    static interface Listener{
        void itemClicked(long id);
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            taskId = savedInstanceState.getLong("taskId");
        }
//        view = getView();
//        listView = (ListView) view.findViewById(R.id.listView);
//        SQLiteOpenHelper todoListDatabaseHelper = new ToDoListDatabaseHelper(getContext());
//        try {
//            SQLiteDatabase db = todoListDatabaseHelper.getWritableDatabase();
//            Cursor cursor = db.query("TASKS",
//                    new String[]{"_id", "TASK"},
//                    null, null, null, null, null);
//            CursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
//                    android.R.layout.simple_list_item_1,
//                    cursor,
//                    new String[]{"TASK"},
//                    new int[]{android.R.id.text1}, 0);
//            listView.setAdapter(listAdapter);
//        } catch (SQLiteException e) {
//            Toast toast = Toast.makeText(getContext(), "Database unavaible", Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tasks, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        updateListView();
        return view;
    }

    @Override
    public void onStart() {
        View view = getView();
        super.onStart();
//        View view = inflater.inflate(R.layout.fragment_list_tasks,
//                container, false);
        if(view != null) {
//            TextView title = (TextView) view.findViewById(R.id.taskTextView);
//            title.isShown();
//             listView = (ListView) view.findViewById(R.id.listView);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            Button insertButton = (Button) view.findViewById(R.id.new_task_button);
            try {
                SQLiteOpenHelper toDoListDatabaseHelper = new ToDoListDatabaseHelper(getContext());
                db = toDoListDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("TASKS",
                        new String[]{"_id", "TASK"},
                        null, null, null, null, null);
                CursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[]{"TASK"},
                        new int[]{android.R.id.text1}, 0);
//                listView.setAdapter(listAdapter);
//            setListAdapter(listAdapter);
            } catch (SQLiteException e) {
                Toast toast = Toast.makeText(getContext(), "Database unavaible", Toast.LENGTH_SHORT);
                toast.show();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null){
                        listener.itemClicked(id);
                    }
                }
            });

            insertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText taskText = (EditText) view.findViewById(R.id.new_task_text);
                    taskText.setVisibility(View.VISIBLE);
                    ContentValues taskValues = new ContentValues();
                    taskValues.put("TASK", taskText.getText().toString());
                    taskValues.put("STATUS", 0);
                    SQLiteOpenHelper todoListDatabaseHelper = new ToDoListDatabaseHelper(getContext());
                    try{
                        db = todoListDatabaseHelper.getWritableDatabase();
                        db.insert("TASKS", null, taskValues);
                        db.close();
                    } catch(SQLiteException e){
                        Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    updateListView();
                    taskText.getText().clear();
                }
            });
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("taskId", taskId);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    public void updateListView(){
        SQLiteOpenHelper todoListDatabaseHelper = new ToDoListDatabaseHelper(getContext());
        try {
            db = todoListDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("TASKS",
                    new String[]{"_id", "TASK"},
                    null, null, null, null, null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"TASK"},
                    new int[]{android.R.id.text1}, 0);
            listView.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(getContext(), "Database unavaible", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
