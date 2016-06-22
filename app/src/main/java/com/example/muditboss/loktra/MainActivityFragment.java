package com.example.muditboss.loktra;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 *
 */
public class MainActivityFragment extends Fragment{

    ListView listview;
    CustomAdapter customAdapter;
    EditText inputSearch;
    static ProgressDialog progress;

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
            String connect = "No Network Connection";
            Toast.makeText(getActivity(),connect, Toast.LENGTH_SHORT).show();
        }
    }


    private void updateCommits(){
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
        new FetchCommits(customAdapter,getActivity(),listview).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        listview = (ListView) root.findViewById(R.id.listview_forecast);
        customAdapter = new CustomAdapter(getActivity(),null);
        inputSearch = (EditText) root.findViewById(R.id.searchInput);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivityFragment.this.customAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivityFragment.this.customAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0)
                    updateCommits();
            }
        });
        return root;
    }
}
