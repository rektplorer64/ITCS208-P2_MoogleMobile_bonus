package tanawinwichitcom.android.mooglemobile;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.moviefetcher.TagsArrayAdapter;

/**
 * Created by tanaw on 3/21/2018.
 */

public class MoviesArrayAdapter extends BaseAdapter{
    private final ArrayList<Movie> movieArrayList;
    private final Context context;

    public MoviesArrayAdapter(Context context, Map<Integer, Movie> movieMap){
        movieArrayList = new ArrayList<>(movieMap.values());
        this.context = context;
    }

    public MoviesArrayAdapter(Context context, List<Movie> movieList){
        movieArrayList = new ArrayList<>(movieList);
        this.context = context;
    }

    @Override
    public int getCount(){
        return movieArrayList.size();
    }

    @Override
    public Movie getItem(int i){
        return movieArrayList.get(i);
    }

    @Override
    public long getItemId(int i){
        return movieArrayList.get(i).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rootView = convertView;
        if(rootView == null){
            rootView = LayoutInflater.from(context).inflate(R.layout.moviecard_layout, parent, false);
        }

        Movie movieEntry = getItem(position);
        if(movieEntry != null){
            TextView movieTitle = rootView.findViewById(R.id.movieTitle);
            movieTitle.setText(movieEntry.getTitle());

            if(movieEntry.getTags().size() != 0){
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.Adapter ratingArrayAdapter = new TagsArrayAdapter(context, movieEntry.getTags());
                RecyclerView horizontalRecyclerView = rootView.findViewById(R.id.tagRecycleView);
                horizontalRecyclerView.setHasFixedSize(true);
                horizontalRecyclerView.setLayoutManager(layoutManager);
                horizontalRecyclerView.setAdapter(ratingArrayAdapter);
            }

            TextView reviewCount = rootView.findViewById(R.id.review);
            reviewCount.setText(movieEntry.getRating().size() + " Reviews");
            //System.out.println("rating size " + movieEntry.getRating().size());

            TextView score = rootView.findViewById(R.id.score);
            score.setText(String.format("%.1f / 5.0", movieEntry.getMeanRating()));
        }

        return rootView;
    }
}
