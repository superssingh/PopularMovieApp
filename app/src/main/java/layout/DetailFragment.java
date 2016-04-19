package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.santossingh.popularmovieapp.Activities.VideoActivity;
import com.santossingh.popularmovieapp.Models.MovieTrailer;
import com.santossingh.popularmovieapp.Models.Results;
import com.santossingh.popularmovieapp.R;
import com.santossingh.popularmovieapp.Database.ContentProvider.ContentProvider;
import com.santossingh.popularmovieapp.Services.ServiceManager;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends android.app.Fragment {

    TextView movie_Title;
    ImageView movie_Poster;
    TextView movie_ReleaseDate;
    TextView movie_UsersRating;
    RatingBar movie_RatingStars;
    TextView movie_Overview;
    Button favorite;
    Button play;
    TextView trailerStatus;
    LinearLayout linearLayout;

    private String videoKey=null;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail,container,false);

        linearLayout=(LinearLayout)view.findViewById(R.id.details);
            movie_Title=(TextView)view.findViewById(R.id.movieTitle);
            movie_Poster=(ImageView)view.findViewById(R.id.moviePoster);
            movie_ReleaseDate=(TextView)view.findViewById(R.id.releaseDate);
            movie_UsersRating=(TextView)view.findViewById(R.id.rating_Percent);
            movie_RatingStars=(RatingBar)view.findViewById(R.id.ratingBar);
            movie_Overview=(TextView)view.findViewById(R.id.overViewDetail);
            favorite=(Button)view.findViewById(R.id.BTN_favorite);
            play=(Button)view.findViewById(R.id.BTN_play);
            trailerStatus=(TextView)view.findViewById(R.id.TXT_trailerstatus);

            linearLayout.setVisibility(View.INVISIBLE);
        return view;
    }

    // Method for Tablet screen--------------
    public void UpdateTabletUI(final Results result){
        linearLayout.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + result.getPoster_path())
                .resize(250, 320)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movie_Poster);

        movie_Title.setText(result.getTitle());
        movie_ReleaseDate.setText(result.getRelease_date().substring(0,4));
        movie_UsersRating.setText(result.getVote_average() + "/10");
        movie_RatingStars.setRating(result.getVote_average() * 5 / 10);
        movie_Overview.setText(result.getOverview());

        getTrailer(Integer.toString(result.getId())); // getting trailer key into videoKey varialbe

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentProvider contentProvider = new ContentProvider();
                contentProvider.addMovieResult(v.getContext(), result);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoActivity.class)
                        .putExtra("videoKey",videoKey);
                startActivity(intent);
            }
        });
    }

    // Method for Handset screen--------------
    public void setDataforHandsetUI(final Intent intent){
        linearLayout.setVisibility(View.VISIBLE);

        int movie_id=intent.getIntExtra("movie_Id", 0);
        String movie_name=intent.getStringExtra("movie_Name");
        String movie_poster=intent.getStringExtra("poster_Path");
        String movie_backposter=intent.getStringExtra("back_poster_Path");
        String release_date=intent.getStringExtra("release_Date");
        Float users_rating=intent.getFloatExtra("users_Rating", 0);
        String overview=intent.getStringExtra("overview");

        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + movie_poster)
                .resize(250, 320)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movie_Poster);

        movie_Title.setText(movie_name);
        movie_ReleaseDate.setText(release_date.substring(0,4));
        movie_UsersRating.setText(users_rating + "/10");
        movie_RatingStars.setRating(users_rating * 5 / 10);
        movie_Overview.setText(overview);

        getTrailer(Integer.toString(movie_id)); // getting trailer key into videoKey varialbe

        //add data in Favorite List -----------------
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentProvider contentProvider = new ContentProvider();
                contentProvider.addMovieIntent(v.getContext(), intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoActivity.class)
                        .putExtra("videoKey",videoKey);
                startActivity(intent);
            }
        });
    }


    // getting trailer key ----------------------------------
    public void getTrailer(String id){
        Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();
        ServiceManager serviceManager= new ServiceManager();
        Call<MovieTrailer> trailerCall= serviceManager.getJSONData().getMovieTrailer(id);
        trailerCall.enqueue(new Callback<MovieTrailer>() {
            @Override
            public void onResponse(Call<MovieTrailer> call, Response<MovieTrailer> response) {
                if (response.isSuccessful()) {
                    if (response.body().getTrailerResults().length == 0) {
                        Toast.makeText(getActivity(), "Trailer unavailable",
                                Toast.LENGTH_SHORT).show();
                        trailerStatus.setText("Unavailable");
                        play.setVisibility(View.INVISIBLE);
                    } else {
                        videoKey = response.body().getTrailerResults()[0].getKey();
                        Toast.makeText(getActivity(), videoKey,
                                Toast.LENGTH_SHORT).show();
                        trailerStatus.setText("Available");
                        play.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieTrailer> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
