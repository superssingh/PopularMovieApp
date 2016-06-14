package com.santossingh.popularmovieapp.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.santossingh.popularmovieapp.Activities.FavoriteActivity;
import com.santossingh.popularmovieapp.Utilities.AnimationUtil;
import com.santossingh.popularmovieapp.Database.Constant;
import com.santossingh.popularmovieapp.Database.FavoriteMovies;
import com.santossingh.popularmovieapp.R;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Stark on 18/04/2016.
 */
public class FavoriteRecycleAdapter extends RecyclerView.Adapter<FavoriteRecycleAdapter.Holder>{
    
    private View rcView;
    private Context context;
    private Realm realm;
    private RealmResults<FavoriteMovies> results;
    int preposition;
    FavoriteActivity favoriteActivity=new FavoriteActivity();
    public FavoriteRecycleAdapter(Context context, RealmResults<FavoriteMovies> FM) {
        this.context = context;
        results=FM;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        rcView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_favorite_fragment, parent, false);
        return new Holder(rcView);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
            final FavoriteMovies currentMovie = results.get(position);
            final int movie_id = currentMovie.getMovie_id();
            String poster_path = currentMovie.getBackdrop_path();
            String movie_name = currentMovie.getTitle();

            Picasso.with(holder.itemView.getContext())
                    .load(Constant.HTTP.IMAGE_BASE_URL + poster_path)
                    .resize(480, 220)
                    .placeholder(R.drawable.placeholder_back)
                    .error(R.drawable.error_back)
                    .into(holder.imageView);
            holder.textView.setText(movie_name);
            // Delete Data from Realm Database
            holder.BTN_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(position);
                    results.size();
                    if (results.size()==0) {
                        Snackbar.make(rcView,R.string.Favorite_empty_list,Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });
            // animation part ----------------
            if (position > preposition) {
                AnimationUtil.animate(holder, true);
            } else {
                AnimationUtil.animate(holder, false);
            }
            preposition = position;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        private Button BTN_Delete;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.imageicon);
            textView = (TextView) itemView.findViewById(R.id.TXTmovieName);
            BTN_Delete = (Button) itemView.findViewById(R.id.BTN_DELETE);
        }

        @Override
        public void onClick(View v) {
        }
    }

    private void delete(int position){
        realm = Realm.getDefaultInstance();
        results = realm.where(FavoriteMovies.class).findAll();
        realm.beginTransaction();
        FavoriteMovies fm = results.get(position);
        fm.removeFromRealm();
        realm.commitTransaction();
        notifyDataSetChanged();
    }
}
