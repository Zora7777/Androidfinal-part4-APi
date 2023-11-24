package com.example.musicsearchapi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "name", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists collect(id integer primary key autoincrement" +
                ",json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addCollect(MusicItem musicItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("json", new Gson().toJson(musicItem));
        writableDatabase.insert("collect", null, values);
    }

    public List<MusicItem> getCollect() {
        ArrayList<MusicItem> musicItems = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from collect", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String json = cursor.getString(cursor.getColumnIndex("json"));
            musicItems.add(new Gson().fromJson(json, MusicItem.class));
        }
        return musicItems;
    }

    public void deleteCollect(MusicItem data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        writableDatabase.delete("collect", "json = ?", new String[]{new Gson().toJson(data)});
    }
}
