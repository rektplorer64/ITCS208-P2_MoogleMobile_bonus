package tanawinwichitcom.android.mooglemobile;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
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

    private TextView reviewCount;
    private TextView score;
    private TextView movieTitle;
    private RecyclerView horizontalRecyclerView;
    private RatingBar ratingBar;
    private TextView movieYear;

    public MoviesArrayAdapter(Context context, Map<Integer, Movie> movieMap){
        movieArrayList = new ArrayList<>(movieMap.values());
        this.context = context;
    }

    public MoviesArrayAdapter(Context context, List<Movie> movieList){
        movieArrayList = new ArrayList<>(movieList);
        this.context = context;
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
            movieTitle.setText(movieEntry.getTitle());
            if(movieEntry.getTags().size() != 0){
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.Adapter tagsArrayAdapter = new TagsArrayAdapter(context, movieEntry.getTags());
                horizontalRecyclerView.setHasFixedSize(true);
                horizontalRecyclerView.setLayoutManager(layoutManager);
                horizontalRecyclerView.setAdapter(tagsArrayAdapter);
            }else{
                horizontalRecyclerView.setVisibility(View.GONE);
            }
            reviewCount.setText("(" + movieEntry.getRating().size() + ")");
            //System.out.println("rating size " + movieEntry.getRating().size());
            score.setText(String.format("%.1f ", movieEntry.getMeanRating()));
            movieYear.setText(" • " + movieEntry.getYear());
            ratingBar.setRating(movieEntry.getMeanRating().floatValue());
            ratingBar.setIsIndicator(true);
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

        ViewHolder(View itemView){
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            reviewCount = itemView.findViewById(R.id.review);
            score = itemView.findViewById(R.id.score);
            horizontalRecyclerView = itemView.findViewById(R.id.tagRecycleView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            movieYear = itemView.findViewById(R.id.movieYear);
        }
    }
}
