package com.santossingh.popularmovieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.santossingh.popularmovieapp.Adapters.Utilities.AnimationUtil;
import com.santossingh.popularmovieapp.Models.ReviewsModels.ReviewsResult;
import com.santossingh.popularmovieapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stark on 22/04/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.Holder> {
    private Context context;
    private View rcView;
    private List<ReviewsResult> results = new ArrayList<ReviewsResult>();
    private int preposition;

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReviewsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        rcView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_reviews_fragment, parent, false);
        return new Holder(rcView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.Holder holder, int position) {
        ReviewsResult reviewsResult=results.get(position);
        holder.review.setText(reviewsResult.getContent());
        holder.author.setText(reviewsResult.getAuthor());

        // animation part ----------------
        if (position>preposition){
            AnimationUtil.animate2(holder, true);
        }else {
            AnimationUtil.animate2(holder,false);
        }
        preposition=position;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void addReviewsList(List<ReviewsResult> reviews) {
        results = reviews;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView review;
        private TextView author;
        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            review=(TextView)itemView.findViewById(R.id.review_content);
            author=(TextView)itemView.findViewById(R.id.review_author);
        }

        @Override
        public void onClick(View v) {

        }
    }
}