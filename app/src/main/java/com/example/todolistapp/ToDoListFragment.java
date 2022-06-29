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
    private long taskId;

    static interface Listener{
        void itemClicked(long id);
    };
//
    private Listener listener;
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            taskId = savedInstanceState.getLong("taskId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_tasks, container, false);
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
             listView = (ListView) view.findViewById(R.id.listView);
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
                listView.setAdapter(listAdapter);
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
                        SQLiteDatabase db_ = todoListDatabaseHelper.getWritableDatabase();
                        db_.update("TASKS", taskValues, "_id = ?", new String[]{Long.toString(taskId)});
                        db_.close();
                    } catch(SQLiteException e){
                        Toast toast = Toast.makeText(getContext(), "Database unavailable", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    Toast db_update_done = Toast.makeText(getContext(), "database added new task", Toast.LENGTH_SHORT);
                    db_update_done.show();
//
//                    TextView text = new TextView(getContext());
//                    text.setText(taskText.getText().toString());
//                    listView.addView(text);
                }
            });

//            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

//    public void addItem(View view){
//        List<String> tasks = new ArrayList<String>();
//        ArrayAdapter<String> adapter;
//        EditText newTask = (EditText) view.findViewById(R.id.new_task_text);
//        String taskText = newTask.getText().toString();
//        if(!(taskText.equals(""))){
//            tasks.add(taskText);
//            adapter = new ArrayAdapter<String>(getContext(),R.id.listView, tasks);
//            listView.setAdapter(adapter);
//        }
//    }

    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("taskId", taskId);
    }

//    @Override
//    public void onRestart(){
//
//    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.listener = (Listener) context;
    }

//    @Override
//    public void onListItemClick(ListView listView, View itemView, int position, long id){
//        if (listener != null){
//            listener.itemClicked(id);
//        }
//    }

}
