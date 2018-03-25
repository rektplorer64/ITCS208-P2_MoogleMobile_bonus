package tanawinwichitcom.android.mooglemobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;

/**
 * Created by tanaw on 3/21/2018.
 */

public class MoviesArrayAdapter extends RecyclerView.Adapter<MoviesArrayAdapter.ViewHolder>{
    private final ArrayList<Movie> movieArrayList;
    private final Context context;
    private final Activity activity;

    private RecyclerView horizontalRecyclerView;

    public MoviesArrayAdapter(Context context, Map<Integer, Movie> movieMap, Activity activity){
        movieArrayList = new ArrayList<>(movieMap.values());
        this.context = context;
        this.activity = activity;
    }

    public MoviesArrayAdapter(Context context, List<Movie> movieList, Activity activity){
        movieArrayList = new ArrayList<>(movieList);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View rootView = LayoutInflater.from(context).inflate(R.layout.moviecard_layout, parent, false);
        return new ViewHolder(rootView);
    }

    /**
     * This method fixes the issue while scrolling some content mess up randomly
     *
     * @param position Position
     *
     * @return Position
     */
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Movie movieEntry = movieArrayList.get(position);
        if(movieEntry != null){
            holder.movieTitle.setText(movieEntry.getTitle());
            if(movieEntry.getTags().size() != 0){
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.Adapter tagsArrayAdapter = new TagsArrayAdapter(context, movieEntry.getTags());
                horizontalRecyclerView.setHasFixedSize(true);
                horizontalRecyclerView.setLayoutManager(layoutManager);
                horizontalRecyclerView.setAdapter(tagsArrayAdapter);
            }else{
                horizontalRecyclerView.setVisibility(View.GONE);
            }
            holder.reviewCount.setText("(" + movieEntry.getRating().size() + ")");
            //System.out.println("rating size " + movieEntry.getRating().size());
            holder.score.setText(String.format("%.1f ", movieEntry.getMeanRating()));
            holder.movieYear.setText(" • " + movieEntry.getYear());
            holder.ratingBar.setRating(movieEntry.getMeanRating().floatValue());
            holder.ratingBar.setIsIndicator(true);

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("movieID", movieEntry.getID());

                    Pair<View, String> heroImagePair = Pair.create(holder.heroImageView, "movieImage");
                    Pair<View, String> movieTitlePair = Pair.create(holder.movieTitle, "movieTitle");
                    Pair<View, String> reviewCountPair = Pair.create(holder.reviewCount, "reviewCount");
                    Pair<View, String> ratingBarPair = Pair.create(holder.ratingBar, "ratingBar");
                    Pair<View, String> yearPair = Pair.create(holder.movieYear, "year");
                    Pair<View, String> scorePair = Pair.create(holder.score, "score");
                    Pair<View, String> tagRecycleViewPair = Pair.create(holder.tagRecycle, "tagRecycler");
                    Pair<View, String> cardPair = Pair.create(holder.cardView, "movieCard");


                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(activity, heroImagePair, movieTitlePair
                                    , reviewCountPair, reviewCountPair, reviewCountPair
                                    , yearPair, ratingBarPair, scorePair
                                    , tagRecycleViewPair, cardPair);
                    System.out.println(movieEntry.getID());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
        }
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public int getItemCount(){
        return movieArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView reviewCount;
        private TextView score;
        private TextView movieTitle;
        private RatingBar ratingBar;
        private TextView movieYear;
        private View itemView;
        private View heroImageView;
        private RecyclerView tagRecycle;
        private CardView cardView;

        ViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.movieCard);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            reviewCount = itemView.findViewById(R.id.review);
            score = itemView.findViewById(R.id.score);
            horizontalRecyclerView = itemView.findViewById(R.id.tagRecycleView);
            tagRecycle = horizontalRecyclerView;
            ratingBar = itemView.findViewById(R.id.ratingBar);
            movieYear = itemView.findViewById(R.id.movieYear);
            heroImageView = itemView.findViewById(R.id.heroimage);

            this.itemView = itemView;
        }
    }
}
