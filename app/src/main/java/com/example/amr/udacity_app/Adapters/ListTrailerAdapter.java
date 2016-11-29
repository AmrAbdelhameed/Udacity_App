package com.example.amr.udacity_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amr.udacity_app.Models.ListTrailer;
import com.example.amr.udacity_app.R;

import java.util.ArrayList;


public class ListTrailerAdapter extends ArrayAdapter<ListTrailer> {
    ArrayList<ListTrailer> Trailerlist;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ListTrailerAdapter(Context context, int resource, ArrayList<ListTrailer> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        Trailerlist = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.Trailer_Movie = (TextView) v.findViewById(R.id.trailers_);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.Trailer_Movie.setText("Tariler " + (position + 1));

        return v;

    }

    static class ViewHolder {
        public TextView Trailer_Movie;
    }
}