package com.santossingh.popularmovieapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.santossingh.popularmovieapp.Adapters.RecycleAdapter;
import com.santossingh.popularmovieapp.Models.MovieResponse;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.DataManager;
import com.santossingh.popularmovieapp.Utilities.AutoFitGridLayout;
import com.santossingh.popularmovieapp.Utilities.Utils;

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
    @Bind(R.id.Progressbar)
    ProgressBar progressBar;
    @Bind(R.id.NoView)
    TextView NoView;
    @Bind(R.id.swipeRefreshLayout)
    PullRefreshLayout layout;
    private View rootView;
    private DataManager dataManager;
    private RecycleAdapter recyclerAdapter;
    private List<Results> resultsList;
    private Results firstResult;
    private OnFragmentInteractionListener mListener;
    private int menuItemPosition;
    private Handler handler;

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

        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CountDownTimer(4000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        layout.setRefreshing(false);
                        makeService("POPULAR");
                    }
                }.start();
            }
        });

// refresh complete

        if (savedInstanceState == null) {
                makeService("POPULAR");
                menuItemPosition = R.id.most_Popular;
            }else{
            progressBar.setVisibility(View.GONE);
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
                        setUI(true);
                        recyclerAdapter.addMovieList(resultsList);
                        firstResult = resultsList.get(0);
                        mListener.onTabletListener(firstResult);
                    } else {
                        Toast.makeText(getActivity(), "Null Value", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                setUI(false);
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

    public void setUI(boolean b) {
        if (b) {
            progressBar.setVisibility(View.GONE);
            NoView.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            NoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void runUI(boolean b) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                makeService("POPULAR");
                Toast.makeText(getActivity(), "Searching Network...", Toast.LENGTH_SHORT).show();
            }
        };

        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(6000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }
            }
        }).start();
    }

    public void searchLoopForNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i == 0) {
                    int status = Utils.getConnectionKey(getActivity());
                    if (status == 1) {
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                makeService("POPULAR");
                            }
                        }.start();
                        i = 1;
                    }
                }
            }
        }).start();
    }

    public interface OnFragmentInteractionListener {
        void onTabletListener(Results result);
        void onFragmentInteraction(Results result);
    }
}