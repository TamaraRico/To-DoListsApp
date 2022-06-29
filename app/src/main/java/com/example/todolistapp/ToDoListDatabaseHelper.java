package com.example.todolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_TITLE = "toDoTasks"; //the title of our database
    private static final int DB_VERSION = 1; //the version of the database

    ToDoListDatabaseHelper(Context context){
        super(context, DB_TITLE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDataBase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDataBase(db, oldVersion, newVersion);
    }

    private static void insertTask(SQLiteDatabase db,
                                      String title){
        ContentValues taskValues = new ContentValues();
        taskValues.put("TASK", title);
        taskValues.put("STATUS", 0);
        db.insert("TASKS", null, taskValues);
    }

    private void updateMyDataBase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE TASKS ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TASK TEXT, "
                    + "STATUS INTEGER);");

            insertTask(db, "The Limb Loosener");
            insertTask(db, "Core Agony");
            insertTask(db, "The Wimp Special");
            insertTask(db, "Strength and Length");
        }
    }

}
