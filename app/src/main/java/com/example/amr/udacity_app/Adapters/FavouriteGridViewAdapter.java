package com.example.amr.udacity_app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.amr.udacity_app.Models.MainGridItem;
import com.example.amr.udacity_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteGridViewAdapter extends ArrayAdapter<MainGridItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<MainGridItem> mGridData = new ArrayList<MainGridItem>();

    public FavouriteGridViewAdapter(Context mContext, int layoutResourceId, ArrayList<MainGridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<MainGridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image_in_Favourite);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        MainGridItem item = mGridData.get(position);

        Picasso.with(mContext).load(item.getImage()).placeholder(R.drawable.refresh).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}