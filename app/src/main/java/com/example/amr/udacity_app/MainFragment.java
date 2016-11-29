package com.example.amr.udacity_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.amr.udacity_app.Adapters.MainGridViewAdapter;
import com.example.amr.udacity_app.Models.MainGridItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    private GridView mGridView;
    private ArrayList<MainGridItem> mGridData;
    private MainGridViewAdapter mGridAdapter;
    private ProgressDialog dialog;
    private Main_Tablet_Mode mListener;

    void setNameListener(Main_Tablet_Mode mainTabletMode) {
        this.mListener = mainTabletMode;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");


        mGridView = (GridView) view.findViewById(R.id.MainGriidView);

        mGridData = new ArrayList<>();
        mGridAdapter = new MainGridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
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
        new MoviesAsyncTask().execute();
        return view;

    }

    public class MoviesAsyncTask extends AsyncTask<Void, Void, Boolean> {

        String appKey = "0378f9d1be05009430ec0b03b4f3b3e8";
        String movieJson;

        private final String LOG_TAG = MoviesAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        private boolean parseResult(String result) {
            if (result == null)
                return false;
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray res = obj.optJSONArray("results");
                MainGridItem item;
                for (int i = 0; i < res.length(); i++) {
                    JSONObject jsonObject = res.optJSONObject(i);
                    String Titlee = jsonObject.optString("title");
                    String IDD = jsonObject.optString("id");
                    String Ratee = jsonObject.optString("vote_average");
                    String Yearr = jsonObject.optString("release_date");
                    String Overvieww = jsonObject.optString("overview");
                    item = new MainGridItem();
                    item.setTitle(Titlee);
                    item.setID(IDD);
                    item.setOverview(Overvieww);
                    item.setyear(Yearr);
                    item.setrating(Ratee);
                    item.setImage("https://image.tmdb.org/t/p/w320/" + (jsonObject.getString("poster_path")));
                    item.setImage2("https://image.tmdb.org/t/p/w500/" + (jsonObject.getString("backdrop_path")));

                    mGridData.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected Boolean doInBackground(Void... strings) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String choose = sharedPreferences.getString(("example_list"), ("popular"));
            final String baseURL = "https://api.themoviedb.org/3/movie/";
            final String api_key = "api_key";

            Uri built = Uri.parse(baseURL).buildUpon()
                    .appendPath(choose)
                    .appendQueryParameter(api_key, appKey).build();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(built.toString());
                Log.v(LOG_TAG, "built uri " + built.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return false;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return false;
                }
                movieJson = buffer.toString();
                Log.v(LOG_TAG, "Forecast JSON String: " + movieJson);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            return parseResult(movieJson);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean) {
                SharedPreferences.Editor e = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                e.putString("json", movieJson).commit();
                MainGridViewAdapter adapter = new MainGridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
                mGridView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_LONG).show();
                SharedPreferences e = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                movieJson = e.getString("json", null);
                if (parseResult(movieJson)) {
                    MainGridViewAdapter adapter = new MainGridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
                    mGridView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mGridData.clear();
            new MoviesAsyncTask().execute();
            return true;
        }
        if (id == R.id.title_activity_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.title_activity_favourite) {
            if (null != getActivity().findViewById(R.id.flDetails)) {

                Intent intent = new Intent(getActivity(), FavouriteActivity.class);
                startActivity(intent);
            }
            else
            {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), FavouriteActivity.class);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}