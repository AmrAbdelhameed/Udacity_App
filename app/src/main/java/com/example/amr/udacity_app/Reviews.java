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

import com.example.amr.udacity_app.Adapters.ListReviewsAdapter;
import com.example.amr.udacity_app.Models.ListReviews;

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

public class Reviews extends AppCompatActivity {

    private ArrayList<ListReviews> Reviewlist;
    private ListReviewsAdapter adapter;
    private ListView listview;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        dialog = new ProgressDialog(Reviews.this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String id = b.getString("mmid");

        Reviewlist = new ArrayList<ListReviews>();
        AsyncReviewsTask ReviewTask = new AsyncReviewsTask();
        ReviewTask.execute(id);
        listview = (ListView) findViewById(R.id.list_of_reviews);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                Toast.makeText(Reviews.this, "A Review by " + adapter.getItem(position).getauthor(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class AsyncReviewsTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = AsyncReviewsTask.class.getSimpleName();
        String appKey = "0378f9d1be05009430ec0b03b4f3b3e8";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        private String getReviewDataFromJson(String ReviewJsonStr) throws JSONException {

            JSONObject objec = new JSONObject(ReviewJsonStr);
            JSONArray movieJsonArray = objec.getJSONArray("results");
            try {
                for (int i = 0; i < movieJsonArray.length(); i++) {
                    JSONObject obj = movieJsonArray.getJSONObject(i);
                    ListReviews Revi = new ListReviews();
                    String Reviewcontent = obj.getString("content");
                    String Reviewauthor = obj.getString("author");
                    Revi.setauthor(Reviewauthor);
                    Revi.setcontent(Reviewcontent);
                    Reviewlist.add(Revi);
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

            String ReviewJsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie";
                final String api_key = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath("reviews")
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
                ReviewJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Review Jason String: " + ReviewJsonStr);
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
                return getReviewDataFromJson(ReviewJsonStr);
            } catch (JSONException e) {
                Log.v(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String Reviewauthor) {

            dialog.dismiss();

            adapter = new ListReviewsAdapter(getApplicationContext(), R.layout.row2, Reviewlist);

            listview.setAdapter(adapter);

            if (Reviewlist.isEmpty())
            {
                finish();
                Toast.makeText(Reviews.this, "Reviews is Empty", Toast.LENGTH_SHORT).show();
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
