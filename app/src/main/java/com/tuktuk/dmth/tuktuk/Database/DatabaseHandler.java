package com.tuktuk.dmth.tuktuk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nrv on 9/10/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, "tuktuk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initDB(SQLiteDatabase db){
         final String MetadataTable = "CREATE TABLE \"metadata\" (\"key\" VARCHAR PRIMARY KEY  NOT NULL , \"value\" VARCHAR, \"lastupdate\" DATETIME, \"lastsync\" VARCHAR)";
        final String Tripdata = "CREATE TABLE \"tripdata\" (\"tripid\" VARCHAR PRIMARY KEY  NOT NULL , \"driverid\" VARCHAR, \"requesttimw\" DATETIME, \"accepttime\" DATETIME, \"starttime\" DATETIME, \"startlan\" TEXT, \"startlon\" VARCHAR, \"endtime\" VARCHAR, \"endlat\" VARCHAR, \"endlon\" VARCHAR, \"lastsync\" DATETIM";
        db.execSQL(MetadataTable);
        db.execSQL(Tripdata);
        initData(db);

    }

    private void initData(SQLiteDatabase db){
        ContentValues values = new ContentValues();

        values.put("keyvalue", "akm_morning");
        db.insert("metadata", null, values);
        values.put("keyvalue", "akm_night");
        db.insert("metadata", null, values);
        values.put("keyvalue", "wait_morning");
        db.insert("metadata", null, values);
        values.put("keyvalue", "wait_night");
        db.insert("metadata", null, values);
        values.put("keyvalue", "start_morning");
        db.insert("metadata", null, values);
        values.put("keyvalue", "start_night");
        db.insert("metadata", null, values);
        values.put("keyvalue", "locked");
        values.put("value", "false");
        db.insert("metadata", null, values);
        values.put("keyvalue", "uname");
        db.insert("metadata", null, values);
        values.put("keyvalue", "logged");
        values.put("value", "false");
        db.insert("metadata", null, values);




    }








}
