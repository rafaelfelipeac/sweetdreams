package com.sd.rafael.sweetdreams.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafae on 22/10/2016.
 */

public class DreamDAO extends SQLiteOpenHelper {

    public DreamDAO(Context context) {
        super(context, "SweetDreams", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table dreams (id integer primary key, title text, description text, favorite integer, tags text, day integer, month integer, year integer);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Insert(Dream dream) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = getContentValuesDream(dream);
        db.insert("dreams", null, data);
    }

    public List<Dream> Read() {
        List<Dream> dreams = new ArrayList<>();
        String sql = "select * from dreams";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        while(c.moveToNext()) {
            Dream dream = new Dream();

            dream.setId(c.getLong(c.getColumnIndex("id")));
            dream.setTitle(c.getString(c.getColumnIndex("title")));
            dream.setDescription(c.getString(c.getColumnIndex("description")));

            int favorite = c.getInt(c.getColumnIndex("favorite"));
            if(favorite == 1)
                dream.setFavorite(true);
            else
                dream.setFavorite(false);

            dream.setTags(c.getString(c.getColumnIndex("tags")));
            dream.setDay(c.getInt(c.getColumnIndex("day")));
            dream.setMonth(c.getInt(c.getColumnIndex("month")));
            dream.setYear(c.getInt(c.getColumnIndex("year")));

            dreams.add(dream);
        }
        c.close();

        return dreams;
    }

    public void Remove(Dream dream) {
        SQLiteDatabase db = getWritableDatabase();

        if(dream.getId() == null) {
            String sql = "select * from dreams";
            SQLiteDatabase dbR = getReadableDatabase();
            Cursor c = dbR.rawQuery(sql, null);

            while(c.moveToNext())
                dream.setId(c.getLong(c.getColumnIndex("id")));
        }

        String[] params = {dream.getId().toString()};

        db.delete("dreams", "id = ?", params);
    }

    public void Update(Dream dream) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = getContentValuesDream(dream);
        String[] params = {dream.getId().toString()};
        db.update("dreams", data, "id = ?", params);
    }

    private ContentValues getContentValuesDream(Dream dream) {
        ContentValues data = new ContentValues();
        data.put("title", dream.getTitle());
        data.put("description", dream.getDescription());
        data.put("favorite", dream.getFavorite());
        data.put("tags", dream.getTags());
        data.put("day", dream.getDay());
        data.put("month", dream.getMonth());
        data.put("year", dream.getYear());

        return data;
    }

}
