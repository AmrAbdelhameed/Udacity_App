package com.example.amr.udacity_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amr.udacity_app.Models.MainGridItem;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static DBCreate mDBMovies = new DBCreate();
    public static final int DATABASE_VERSION = 3;
    SQLiteDatabase mSQLiteDatabase;

    public DBHelper(Context context) {
        super(context, mDBMovies.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mDBMovies.CREATE_TABLE_Movie);
    }

    public void addMovie(MainGridItem mainGridItem) {
        mSQLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDBMovies.Movie_ID, mainGridItem.getID());
        contentValues.put(mDBMovies.Movie_Image, mainGridItem.getImage());
        contentValues.put(mDBMovies.Movie_Image2, mainGridItem.getImage2());
        contentValues.put(mDBMovies.Movie_Title, mainGridItem.getTitle());
        contentValues.put(mDBMovies.Movie_Rate, mainGridItem.getrating());
        contentValues.put(mDBMovies.Movie_Year, mainGridItem.getyear());
        contentValues.put(mDBMovies.Movie_Overview, mainGridItem.getyear());
        mSQLiteDatabase.insert(mDBMovies.TableName, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + mDBMovies.TableName);
        onCreate(db);
    }

    public Cursor checkmovie(String s) {
        mSQLiteDatabase = getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + mDBMovies.TableName + " where " + mDBMovies.Movie_Title + " = " + "'" + s + "'", null);
        return cursor;
    }

    public void deleterow(MainGridItem mainGridItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(mDBMovies.TableName, mDBMovies.Movie_Title + " = ?", new String[]{mainGridItem.getTitle()});
    }
    public ArrayList<MainGridItem> getAllData_in_Arraylist() {

        ArrayList<MainGridItem> array_list = new ArrayList<MainGridItem>();
        mSQLiteDatabase = getReadableDatabase();
        Cursor c = mSQLiteDatabase.rawQuery("select * from " + mDBMovies.TableName, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            MainGridItem item;
            String mmid = c.getString((c.getColumnIndex(mDBMovies.Movie_ID)));
            String imagemovie1 = c.getString((c.getColumnIndex(mDBMovies.Movie_Image)));
            String imagemovie2 = c.getString((c.getColumnIndex(mDBMovies.Movie_Image2)));
            String movietitle = c.getString((c.getColumnIndex(mDBMovies.Movie_Title)));
            String movierate = c.getString((c.getColumnIndex(mDBMovies.Movie_Rate)));
            String movieyear = c.getString((c.getColumnIndex(mDBMovies.Movie_Year)));
            String mmoverview = c.getString((c.getColumnIndex(mDBMovies.Movie_Overview)));

            item = new MainGridItem();

            item.setImage(imagemovie1);
            item.setID(mmid);
            item.setTitle(movietitle);
            item.setOverview(mmoverview);
            item.setyear(movieyear);
            item.setrating(movierate);
            item.setImage2(imagemovie2);
            array_list.add(item);
            c.moveToNext();
        }
        return array_list;
    }
}