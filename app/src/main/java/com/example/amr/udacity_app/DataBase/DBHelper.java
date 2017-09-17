package com.example.amr.udacity_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import com.example.amr.udacity_app.Models.MainGridItem;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Favourite";

    public static final String TableName = "FavouriteTable";
    public static final String Table_ID = "id";
    public static final String Movie_ID = "idd";
    public static final String Movie_Image = "imageposter";
    public static final String Movie_Image2 = "imageback";
    public static final String Movie_Title = "title";
    public static final String Movie_Rate = "rate";
    public static final String Movie_Year = "year";
    public static final String Movie_Overview = "overview";


    public static final String CREATE_TABLE_Movie = "create table " + TableName +
            "( " + Table_ID + " integer primary key autoincrement ," +
            Movie_ID + " text ," + Movie_Image + " text , " +
            Movie_Image2 + " text ," +
            Movie_Title + " text , " + Movie_Rate + " text , " +
            Movie_Year + " text , " + Movie_Overview + " text ) ;";
    
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase mSQLiteDatabase;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Movie);
    }

    public void addMovie(MainGridItem mainGridItem) {
        mSQLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Movie_ID, mainGridItem.getID());
        contentValues.put(Movie_Image, mainGridItem.getImage());
        contentValues.put(Movie_Image2, mainGridItem.getImage2());
        contentValues.put(Movie_Title, mainGridItem.getTitle());
        contentValues.put(Movie_Rate, mainGridItem.getrating());
        contentValues.put(Movie_Year, mainGridItem.getyear());
        contentValues.put(Movie_Overview, mainGridItem.getyear());
        mSQLiteDatabase.insert(TableName, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TableName);
        onCreate(db);
    }

    public Cursor checkmovie(String s) {
        mSQLiteDatabase = getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + TableName + " where " + Movie_Title + " = " + "'" + s + "'", null);
        return cursor;
    }

    public void deleterow(MainGridItem mainGridItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TableName, Movie_Title + " = ?", new String[]{mainGridItem.getTitle()});
    }
    public ArrayList<MainGridItem> getAllData_in_Arraylist() {

        ArrayList<MainGridItem> array_list = new ArrayList<MainGridItem>();
        mSQLiteDatabase = getReadableDatabase();
        Cursor c = mSQLiteDatabase.rawQuery("select * from " + TableName, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            MainGridItem item;
            String mmid = c.getString((c.getColumnIndex(Movie_ID)));
            String imagemovie1 = c.getString((c.getColumnIndex(Movie_Image)));
            String imagemovie2 = c.getString((c.getColumnIndex(Movie_Image2)));
            String movietitle = c.getString((c.getColumnIndex(Movie_Title)));
            String movierate = c.getString((c.getColumnIndex(Movie_Rate)));
            String movieyear = c.getString((c.getColumnIndex(Movie_Year)));
            String mmoverview = c.getString((c.getColumnIndex(Movie_Overview)));

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