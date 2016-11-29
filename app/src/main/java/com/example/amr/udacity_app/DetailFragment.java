package com.example.amr.udacity_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amr.udacity_app.DataBase.DBHelper;
import com.example.amr.udacity_app.Models.MainGridItem;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {
    TextView titleView;
    String ID, Title, Image1, Rate, Year, Description, Image2;
    TextView yearView;
    TextView DescriptionView;
    TextView rate;
    ImageView imageView;
    Button buttonFavourite, buttontrailer, buttonreview, buttontUnFavourite;
    DBHelper dbHelper;
    MainGridItem mainGridItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        dbHelper = new DBHelper(getContext());

        final Bundle b = getBundle();
        Title = b.getString("mytitle");
        ID = b.getString("myid");
        Rate = b.getString("rate");
        Year = b.getString("myyear");
        Description = b.getString("myoverview");
        Image2 = b.getString("myimage2");
        Image1 = b.getString("myimage");

        mainGridItem = new MainGridItem();
        mainGridItem.setID(ID);
        mainGridItem.setImage(Image1);
        mainGridItem.setImage2(Image2);
        mainGridItem.setyear(Year);
        mainGridItem.setrating(Rate);
        mainGridItem.setTitle(Title);
        mainGridItem.setOverview(Description);

        rate = (TextView) view.findViewById(R.id.rate_movie);
        titleView = (TextView) view.findViewById(R.id.title_movie);
        imageView = (ImageView) view.findViewById(R.id.Image_Poster);
        yearView = (TextView) view.findViewById(R.id.year_movie);
        DescriptionView = (TextView) view.findViewById(R.id.description_movie);

        titleView.setText(Title);
        yearView.setText("Year : " + Year);
        rate.setText("Rate : " + Rate + "/10");
        DescriptionView.setText("Description : " + Description);
        Picasso.with(getActivity()).load(Image2).placeholder(R.drawable.refresh).into(imageView);
        buttonFavourite = (Button) view.findViewById(R.id.button_favourite);
        buttontUnFavourite = (Button) view.findViewById(R.id.button_unfavourite);
        //b.setVisibility(View.VISIBLE);
        buttontrailer = (Button) view.findViewById(R.id.button_trailer);
        buttonreview = (Button) view.findViewById(R.id.button_review);

//        imageView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result)));
//            }
//        });
        if (dbHelper.checkmovie(Title).getCount() == 0) {
            buttonFavourite.setVisibility(View.INVISIBLE);
            buttontUnFavourite.setVisibility(View.VISIBLE);
        } else {
            buttonFavourite.setVisibility(View.VISIBLE);
            buttontUnFavourite.setVisibility(View.INVISIBLE);
        }
        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbHelper.checkmovie(Title).getCount() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.DeleteMovie)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dbHelper.deleterow(mainGridItem);
                                    Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    buttonFavourite.setVisibility(View.INVISIBLE);
                                    buttontUnFavourite.setVisibility(View.VISIBLE);
                                    Bundle bundle = getArguments();
                                    if (bundle != null) {
                                        getActivity().finish();
                                        startActivity(getActivity().getIntent());
                                    }
                                    else{
                                        getActivity().finish();
                                        Intent i = new Intent(getActivity(),MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
                    AlertDialog d = builder.create();
                    d.setTitle("Are you sure");
                    d.show();
                }
            }
        });
        buttontUnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.checkmovie(Title).getCount() == 0) {
                    dbHelper.addMovie(mainGridItem);
                    Toast.makeText(getActivity(), "Added to Favourite Successfully", Toast.LENGTH_SHORT).show();
                    buttontUnFavourite.setVisibility(View.INVISIBLE);
                    buttonFavourite.setVisibility(View.VISIBLE);
                }
            }
        });
        buttontrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Trailers.class);
                Bundle b = new Bundle();
                b.putString("mmid", ID);
                i.putExtras(b);
                startActivity(i);
            }
        });
        buttonreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), Reviews.class);
                Bundle b = new Bundle();
                b.putString("mmid", ID);
                i.putExtras(b);
                startActivity(i);
            }
        });
        return view;
    }

    private Bundle getBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            Intent in = getActivity().getIntent();
            bundle = in.getExtras();
        }
        return bundle;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}