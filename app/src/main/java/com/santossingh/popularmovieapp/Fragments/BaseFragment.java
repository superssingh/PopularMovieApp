package com.santossingh.popularmovieapp.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Adapters.RecycleAdapter;
import com.santossingh.popularmovieapp.Models.MovieResponse;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.DataManager;
import com.santossingh.popularmovieapp.Utilities.AutoFitGridLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stark on 24/04/2016.
 */
public class BaseFragment extends android.app.Fragment implements RecycleAdapter.GetDataFromAdapter {

    private static final String STATE_MOVIES = "state_movies";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    BroadcastReceiver broadcastReceiver = null;
    private View rootView;
    private DataManager dataManager;
    private RecycleAdapter recyclerAdapter;
    private MovieResponse movieResponse;
    private List<Results> resultsList;
    private Results results;
    private OnFragmentInteractionListener mListener;
    private int menuItemPosition;
    private Intent intent;
    private ConnectivityManager cm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, (ArrayList<? extends Parcelable>) resultsList);
        outState.putInt("menu_item", menuItemPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.most_Popular:
                if(item.isChecked()){
                }else{
                    item.setChecked(true);
                    menuItemPosition = item.getItemId();
                    makeService("POPULAR");
                    return true;
                }
            case R.id.high_rated:
                if(item.isChecked()){
                }else{
                    item.setChecked(true);
                    menuItemPosition = item.getItemId();
                    makeService("TOP");
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // this will get called when the activity is re-created on device configuration change
        menu.findItem(menuItemPosition).setChecked(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, rootView);
        resultsList=new ArrayList<Results>();
        configRecycleView();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Toast.makeText(getActivity(), "ON", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "OFF", Toast.LENGTH_SHORT).show();
        }


        if (savedInstanceState == null) {
                makeService("POPULAR");
                menuItemPosition = R.id.most_Popular;
            }else{
                resultsList=savedInstanceState.getParcelableArrayList(STATE_MOVIES);
                menuItemPosition = savedInstanceState.getInt("menu_item");
                recyclerAdapter.addMovieList(resultsList);
            }

        return rootView;
    }

    private void configRecycleView() {
        AutoFitGridLayout layoutManager = new AutoFitGridLayout(getActivity(), 200 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecycleAdapter(getActivity(), BaseFragment.this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void makeService(String query) {
        dataManager = new DataManager();
        Call<MovieResponse> listCall;

        if (query == "TOP") {
            listCall = dataManager.getJSONData().getTopMovies();
        } else {
            listCall = dataManager.getJSONData().getPopMovies();
        }

        listCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    Results[] results = response.body().getResults();
                    resultsList = new ArrayList<Results>(Arrays.asList(results));
                    if (resultsList != null) {
                        recyclerAdapter.addMovieList(resultsList);
                        Results firstResult = resultsList.get(0);
                        mListener.onTabletListener(firstResult);
                    } else {
                        Toast.makeText(getActivity(), "Null Value", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Snackbar.make(rootView, R.string.No_Internet, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onCurrentMovie(Results currentMovie) {
        mListener.onFragmentInteraction(currentMovie);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onTabletListener(Results result);
        void onFragmentInteraction(Results result);
    }

}