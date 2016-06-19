package com.example.muditboss.loktra;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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

/**
 * It fetches the commit data on a separate thread
 */
public class FetchCommits extends AsyncTask<String,Void,String[]> {

    private Context mContext;
    private CustomAdapter customAdapter;
    private ListView listView;
    private ArrayList<String> dataList;

    public FetchCommits(CustomAdapter customAdapter , Context mContext , ListView listview) {
        this.customAdapter = customAdapter;
        this.mContext = mContext;
        this.listView = listview;
    }

    private String[] getCommitDataFromJson(String commitJsonString)
    throws JSONException{

        String[] resultStrs = null;
        dataList = new ArrayList<>();

        JSONArray jso = new JSONArray(commitJsonString);
        resultStrs = new String[jso.length()];
        for(int i=0;i<jso.length();i++){

            JSONObject commitObject = jso.getJSONObject(i);
            JSONObject commitInsideObject =  commitObject.getJSONObject("commit");

            String commitMsg = commitInsideObject.getString("message").trim();

            JSONObject authorObject = commitInsideObject.getJSONObject("author");
            JSONObject commiterObject = commitObject.getJSONObject("committer");
            JSONObject authorObj  = commitObject.getJSONObject("author");
            String author = authorObject.getString("name");
            String login = authorObj.getString("login");

            String url_avatar = authorObj.getString("avatar_url");

            resultStrs[i] = login+"///"+ commitMsg +"///"+ url_avatar;
            dataList.add(resultStrs[i]);

            Log.d("author", author +i +" "+commitMsg + " "+ url_avatar);
        }

        return resultStrs;
    }


    protected String[] doInBackground(String... params) {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;



        String commitJsonStr = null;
        int number = 25;

        try {
            // Construct the URL for the github

            final String forecastBaseUrl = "https://api.github.com/repos/rails/rails/commits";

            Uri builturi = Uri.parse(forecastBaseUrl).buildUpon().build();
            URL url = new URL(builturi.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                commitJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                commitJsonStr = null;
            }
            commitJsonStr = buffer.toString();


            Log.d("API DATA", commitJsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);

            commitJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        try {
            return getCommitDataFromJson(commitJsonStr);

        } catch (Exception e) {

            Log.v("Parsing Data", e.toString());
        }
        return null;
    }

    protected void onPostExecute(String[] data){

        if(data!=null&&data.length!=0) {

            customAdapter.resultStrs = data;
            customAdapter.originalData = dataList;
            customAdapter.filteredData = dataList;
            listView.setAdapter(customAdapter);
            MainActivityFragment.progress.dismiss();
        }
        else {
            Log.d("Data ","Empty");
        }
    }
}
