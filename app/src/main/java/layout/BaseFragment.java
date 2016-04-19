package layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Activities.FavoriteActivity;
import com.santossingh.popularmovieapp.Adapters.RecycleAdapter;
import com.santossingh.popularmovieapp.Adapters.Utilities.AutofitGridlayout;
import com.santossingh.popularmovieapp.Models.MovieResponse;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Services.ServiceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment extends android.app.Fragment implements RecycleAdapter.GetDataFromAdapter {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private static final String STATE_MOVIES = "state_movies";
    private View rootView = null;
    private ServiceManager serviceManager;
    private RecycleAdapter recyclerAdapter;
    private MovieResponse movieResponse;
    private List<Results> resultsList;

    private Results results;
    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, (ArrayList<? extends Parcelable>) resultsList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.High_rated:
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                    makeService("TOP");
                    return true;
                }
            case R.id.most_Popular:
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                    makeService("POPULAR");
                    return true;
                }
            case R.id.favoriteList:
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                    Intent intent=new Intent(getActivity(), FavoriteActivity.class);
                    startActivity(intent);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this,rootView);
        resultsList=new ArrayList<Results>();
        configRecycleView();

        if (savedInstanceState==null){
            makeService("POPULAR");
        }else{
            resultsList=savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            recyclerAdapter.addMovieList(resultsList);
        }
        return rootView;
    }

    private void configRecycleView() {
        AutofitGridlayout layoutManager = new AutofitGridlayout(getActivity(), 200 );
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecycleAdapter(getActivity(), BaseFragment.this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void makeService(String query){
        serviceManager = new ServiceManager();
        Call<MovieResponse> listCall;
        if (query=="TOP") {
            listCall= serviceManager.getJSONData().getTopMovies();
        }else{
            listCall= serviceManager.getJSONData().getPopMovies();
        }
        listCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    Results[] results = response.body().getResults();
                    resultsList = new ArrayList<Results>(Arrays.asList(results));
                    recyclerAdapter.addMovieList(resultsList);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        void onFragmentInteraction(Results result);
    }


}
