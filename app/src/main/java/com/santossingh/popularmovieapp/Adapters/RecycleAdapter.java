package com.santossingh.popularmovieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.santossingh.popularmovieapp.Adapters.Utilities.AnimationUtil;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Database.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stark on 18/04/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Holder>{
    private List<Results> resultsList=new ArrayList<Results>();
    private View rcView;
    private Context context;
    int preposition;

    private GetDataFromAdapter callback;
    private Results currentMovie;
    public interface GetDataFromAdapter{
        void onCurrentMovie(Results currentMovie);
    }

    public RecycleAdapter(Context context, GetDataFromAdapter callback) {
        this.context=context;
        this.callback = callback;
    }

    @Override
    // create View object and pass it on Holder class constructor
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        rcView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_image_fragment, parent, false);
        return new Holder(rcView, callback);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public void addMovieList(List<Results> movie){
        resultsList=movie;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Results currentMovie = resultsList.get(position);
        Picasso.with(holder.imageView.getContext())
                .load(Constant.HTTP.IMAGE_BASE_URL + currentMovie.getPoster_path())
                .resize(190, 240)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.imageView);

// animation part ----------------
        if (position>preposition){
            AnimationUtil.animate(holder, true);
        }else {
            AnimationUtil.animate(holder,false);
        }
        preposition=position;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private GetDataFromAdapter callback;
        public Holder(View itemView, GetDataFromAdapter callback) {
            super(itemView);
            this.callback=callback;
            itemView.setOnClickListener(this);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            currentMovie=resultsList.get(position);
            callback.onCurrentMovie(currentMovie);
        }
    }

}

