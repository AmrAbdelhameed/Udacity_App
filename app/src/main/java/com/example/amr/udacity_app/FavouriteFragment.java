package com.example.amr.udacity_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.amr.udacity_app.Adapters.FavouriteGridViewAdapter;
import com.example.amr.udacity_app.DataBase.DBHelper;

public class FavouriteFragment extends Fragment {
    private GridView mGridView;
    private FavouriteGridViewAdapter mGridAdapter;
    private ProgressDialog dialog;
    DBHelper databaseHelperClass;
    private Favourite_Tablet_Mode mListener;

    void setNameListener(Favourite_Tablet_Mode nameListener) {
        this.mListener = nameListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        databaseHelperClass = new DBHelper(getActivity());

        mGridView = (GridView) view.findViewById(R.id.FavouriteGrideView);
        mGridAdapter = new FavouriteGridViewAdapter(getActivity(), R.layout.grid_item_layout2, databaseHelperClass.getAllData_in_Arraylist());
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