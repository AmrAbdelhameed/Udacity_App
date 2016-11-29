package com.example.amr.udacity_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amr.udacity_app.Models.ListReviews;
import com.example.amr.udacity_app.R;

import java.util.ArrayList;


public class ListReviewsAdapter extends ArrayAdapter<ListReviews> {
    ArrayList<ListReviews> Reviewlist;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ListReviewsAdapter(Context context, int resource, ArrayList<ListReviews> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        Reviewlist = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.author_review = (TextView) v.findViewById(R.id.reviews_);
            holder.content_review = (TextView) v.findViewById(R.id.tvName3);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

            holder.author_review.setText("A movie review by " + Reviewlist.get(position).getauthor());
            holder.content_review.setText(Reviewlist.get(position).getcontent());

        return v;

    }

    static class ViewHolder {
        public TextView author_review;
        public TextView content_review;
    }
}