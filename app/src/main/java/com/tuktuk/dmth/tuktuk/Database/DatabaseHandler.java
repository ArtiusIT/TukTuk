package com.tuktuk.dmth.tuktuk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nrv on 9/10/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, "tuktuk.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS metadata");
            db.execSQL("DROP TABLE IF EXISTS tripdata");
            initDB(db);
    }

    private void initDB(SQLiteDatabase db){
        try {
            System.out.println("Exec Db Start");
            final String MetadataTable = "CREATE TABLE \"metadata\" (\"keyvalue\" VARCHAR PRIMARY KEY  NOT NULL , \"value\" VARCHAR, \"lastupdate\" DATETIME, \"lastsync\" VARCHAR)";
            final String Tripdata = "CREATE TABLE \"tripdata\" (\"tripid\" VARCHAR PRIMARY KEY  NOT NULL , \"driverid\" VARCHAR, \"requesttimw\" DATETIME, \"accepttime\" DATETIME, \"starttime\" DATETIME, \"startlan\" TEXT, \"startlon\" VARCHAR, \"endtime\" VARCHAR, \"endlat\" VARCHAR, \"endlon\" VARCHAR, \"lastsync\" DATETIME)";
            db.execSQL(MetadataTable);
            db.execSQL(Tripdata);
            initData(db);

            System.out.println("Exec Db Done");
        }catch (Exception e){
            e.printStackTrace();

        }

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
        values.put("keyvalue", "lastauthcode");
        values.put("value", "");
        db.insert("metadata", null, values);






    }

    public void updateMetadata(String key,String value){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("value",value);

        db.update("metadata", cv, "keyvalue='" + key+"'", null);
        db.close();
    }


    public String getMetadata(String key){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM metadata WHERE keyvalue = ?", new String[]{key});

        Log.e("DBMetadata",""+c.getCount());
        if(c.moveToFirst()){
            return c.getString(c.getColumnIndex("value"));
        }
        return null;
    }







}
