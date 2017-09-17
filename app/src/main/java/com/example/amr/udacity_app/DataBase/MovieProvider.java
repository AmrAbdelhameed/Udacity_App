package com.example.amr.udacity_app.DataBase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.amr.udacity_app.Models.MainGridItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MovieProvider extends ContentProvider {
    private static final String TAG = "MovieProvider";
    public static final String AUTHORITY = "com.example.amr.udacity_app.DataBase.MovieProvider";
    public static final Uri MOVIE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/FavouriteTable");
    public static final int MOVIE_URI_CODE = 0;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,"FavouriteTable",MOVIE_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    private static class SafeHandler extends Handler {
        private WeakReference<Context> mRef;
        public SafeHandler(Context context) {
            mRef = new WeakReference<Context>(context);
        }
    }

    private SafeHandler mHandler = new SafeHandler(getContext());

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.d(TAG,"onCreate,current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initDB();
        return true;
    }

    private void initDB() {
        mDb = new DBHelper(mContext).getWritableDatabase();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG,"delete,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        int count = mDb.delete(table,selection,selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG,"insert,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        mDb.insert(table,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG,"query,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG,"update,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        Log.d(TAG,"table name:" + table);
        int row = mDb.update(table,values,selection,selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG,"getType");
        return null;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_URI_CODE:
                tableName = DBHelper.TableName;
                break;
            default:break;
        }
        return tableName;
    }
}
