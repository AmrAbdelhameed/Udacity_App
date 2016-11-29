package com.example.amr.udacity_app.DataBase;

public class DBCreate {
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
}
