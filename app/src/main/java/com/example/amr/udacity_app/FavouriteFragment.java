package com.example.amr.udacity_app;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.amr.udacity_app.Adapters.FavouriteGridViewAdapter;
import com.example.amr.udacity_app.DataBase.MovieProvider;
import com.example.amr.udacity_app.Models.MainGridItem;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    private GridView mGridView;
    private FavouriteGridViewAdapter mGridAdapter;
//    DBHelper databaseHelperClass;
    private Favourite_Tablet_Mode mListener;
    ArrayList<MainGridItem> mainGridItems;

    void setNameListener(Favourite_Tablet_Mode nameListener) {
        this.mListener = nameListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        mainGridItems = new ArrayList<>();

        Uri bookUri = MovieProvider.MOVIE_CONTENT_URI;
        Cursor movieCursor = getActivity().getContentResolver().query(bookUri,new String[] {"id","imageposter","imageback","title","rate","year","overview"},null,null,null);
        while (movieCursor.moveToNext()) {
            MainGridItem item;
            String mmid = movieCursor.getString(0);
            String imagemovie1 = movieCursor.getString(1);
            String imagemovie2 = movieCursor.getString(2);
            String movietitle = movieCursor.getString(3);
            String movierate = movieCursor.getString(4);
            String movieyear = movieCursor.getString(5);
            String mmoverview = movieCursor.getString(6);

            item = new MainGridItem();

            item.setImage(imagemovie1);
            item.setID(mmid);
            item.setTitle(movietitle);
            item.setOverview(mmoverview);
            item.setyear(movieyear);
            item.setrating(movierate);
            item.setImage2(imagemovie2);

            mainGridItems.add(item);
        }
        movieCursor.close();

//        databaseHelperClass = new DBHelper(getActivity());

        mGridView = (GridView) view.findViewById(R.id.FavouriteGrideView);
        mGridAdapter = new FavouriteGridViewAdapter(getActivity(), R.layout.grid_item_layout2,mainGridItems);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String Title = mGridAdapter.getItem(position).getTitle();
                String Year = mGridAdapter.getItem(position).getyear();
                String ID = mGridAdapter.getItem(position).getID();
                String Rate = mGridAdapter.getItem(position).getrating();
                String Overview = mGridAdapter.getItem(position).getOverview();
                String Image1 = mGridAdapter.getItem(position).getImage();
                String Image2 = mGridAdapter.getItem(position).getImage2();
                mListener.setSelectedMovie(Title, ID, Rate, Year, Overview, Image1, Image2);
            }
        });
        return view;

    }
}