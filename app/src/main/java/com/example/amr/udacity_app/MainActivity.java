package com.example.amr.udacity_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Main_Tablet_Mode {

    boolean mIsTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mMainFragment = new MainFragment();

        mMainFragment.setNameListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.flMain, mMainFragment, "").commit();

        if (null != findViewById(R.id.flDetails)) {
            mIsTwoPane = true;
        }

    }

    @Override
    public void setSelectedMovie(String Title, String ID, String Rate, String Year, String Overview, String Image1, String Image2) {
        if (!mIsTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            Bundle b = new Bundle();
            b.putString("mytitle", Title);
            b.putString("myid", ID);
            b.putString("rate", Rate);
            b.putString("myyear", Year);
            b.putString("myoverview", Overview);
            b.putString("myimage", Image1);
            b.putString("myimage2", Image2);
            intent.putExtras(b);
            startActivity(intent);
        } else {
            DetailFragment mDetailsFragment = new DetailFragment();
            Bundle b = new Bundle();
            b.putString("mytitle", Title);
            b.putString("myid", ID);
            b.putString("rate", Rate);
            b.putString("myyear", Year);
            b.putString("myoverview", Overview);
            b.putString("myimage", Image1);
            b.putString("myimage2", Image2);
            mDetailsFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.flDetails, mDetailsFragment, "").commit();
        }
    }
}