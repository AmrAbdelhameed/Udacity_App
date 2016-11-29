package com.example.amr.udacity_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amr.udacity_app.Adapters.ListTrailerAdapter;
import com.example.amr.udacity_app.Models.ListTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Trailers extends AppCompatActivity {

    private ArrayList<ListTrailer> TrailerList;
    private ListTrailerAdapter adapter;
    private ListView listview;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        dialog = new ProgressDialog(Trailers.this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String id = b.getString("mmid");

        TrailerList = new ArrayList<ListTrailer>();

        AsyncVideoTask videoTask = new AsyncVideoTask();
        videoTask.execute(id);
        listview = (ListView) findViewById(R.id.list_of_trailers);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + TrailerList.get(position).getName())));
            }
        });
    }

    class AsyncVideoTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = AsyncVideoTask.class.getSimpleName();
        String appKey = "0378f9d1be05009430ec0b03b4f3b3e8";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        private String getVideoDataFromJson(String videoJsonStr) throws JSONException {

            JSONObject objec = new JSONObject(videoJsonStr);
            JSONArray movieJsonArray = objec.getJSONArray("results");
            try {
                for (int i = 0; i < movieJsonArray.length(); i++) {
                    JSONObject obj = movieJsonArray.getJSONObject(i);
                    ListTrailer Trai = new ListTrailer();
                    String Videokey = obj.getString("key");
                    String Videotype = obj.getString("type");
                    Trai.setType(Videotype);
                    Trai.setName(Videokey);
                    TrailerList.add(Trai);
                }
                return null;
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0)
                return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String videoJsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie";
                final String api_key = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath("videos")
                        .appendQueryParameter(api_key, appKey).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI : " + url);

                // Open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                if (buffer.length() == 0) {
                    return null;
                }
                videoJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Video Jason String: " + videoJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getVideoDataFromJson(videoJsonStr);
            } catch (JSONException e) {
                Log.v(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String videoJsonStr) {
            dialog.dismiss();

            adapter = new ListTrailerAdapter(getApplicationContext(), R.layout.row, TrailerList);

            listview.setAdapter(adapter);

            if (TrailerList.isEmpty())
            {
                finish();
                Toast.makeText(Trailers.this, "Trailers is Empty", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
