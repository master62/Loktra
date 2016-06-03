package com.example.muditboss.loktra;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 *
 */
public class MainActivityFragment extends Fragment{

    ListView listview;
    CustomAdapter customAdapter;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    public void onStart() {
        super.onStart();
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            updateCommits();
        } else {
            String connect = "No Network Connectin";
            Toast.makeText(getActivity(),connect, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCommits(){

        new FetchCommits(customAdapter,getActivity(),listview).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        listview = (ListView) root.findViewById(R.id.listview_forecast);
        customAdapter = new CustomAdapter(getActivity(),null);

        return root;
    }
}
