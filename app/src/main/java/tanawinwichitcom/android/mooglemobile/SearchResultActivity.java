package tanawinwichitcom.android.mooglemobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;

import static tanawinwichitcom.android.mooglemobile.MainActivity.movieMap;

/**
 * Created by tanaw on 3/23/2018.
 */

public class SearchResultActivity extends AppCompatActivity{

    RecyclerView researchListRecycleView;
    ProgressBar progressBar;
    MoviesArrayAdapter moviesArrayAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView progressDetails;

    /*Variables for Search options*/
    static boolean wantSort, wantAscendingOrder, wantTitle, wantTag, wantYear, wantRatings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        researchListRecycleView = findViewById(R.id.movieRecyclerView);
        progressBar = findViewById(R.id.loadProgress);
        progressDetails = findViewById(R.id.loadDetail);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            new AsyncTaskSearch(getApplicationContext(), wantSort, wantAscendingOrder).execute(query);
        }
    }

    public static void setSearchOptions(boolean _wantSort, boolean _wantAscendingOrder
            , boolean _wantTitle, boolean _wantTag, boolean _wantYear, boolean _wantRatings){
        // Variable for Radio
        wantSort = _wantSort;
        wantAscendingOrder = _wantAscendingOrder;

        // Variables for CheckBox
        wantTitle = _wantTitle;
        wantTag = _wantTag;
        wantYear = _wantYear;
        wantRatings = _wantRatings;
    }

    private class AsyncTaskSearch extends AsyncTask<String, Integer, List<Movie>>{

        private final Context context;
        private final boolean inAscendingOrder;
        private final boolean wantSorted;
        private int mapSize;

        public AsyncTaskSearch(Context context, boolean wantSorted, boolean inAscendingOrder){
            this.context = context;
            this.wantSorted = wantSorted;
            this.inAscendingOrder = inAscendingOrder;
        }

        @Override
        protected List<Movie> doInBackground(String... strings){
            ArrayList<Movie> moviesSearchResults = new ArrayList<>();
            String query = strings[0].toLowerCase();
            String[] queries = query.split(" ");

            int j = 0;
            mapSize = movieMap.size();
            for(int movieID : movieMap.keySet()){
                Movie currentMovie = movieMap.get(movieID);
                for(int i = 0; i < queries.length; i++){
                    if(currentMovie.toQueryString(wantTitle, wantTag, wantYear, wantRatings).toLowerCase().contains(queries[i])){
                        System.out.println(currentMovie.toString().toLowerCase());
                        moviesSearchResults.add(currentMovie);
                    }
                }
                //System.out.println("MovieID = " + movieID);
                j++;
                publishProgress(j);
            }
            Set<Movie> movieSet = new HashSet<>();
            movieSet.addAll(moviesSearchResults);
            return new ArrayList<>(movieSet);
        }

        @Override
        protected void onPreExecute(){
            researchListRecycleView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            progressDetails.setText(values[0] + " out of " + mapSize);
        }

        @Override
        protected void onPostExecute(List<Movie> movieList){
            researchListRecycleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressDetails.setVisibility(View.GONE);

            if(wantSorted){
                moviesArrayAdapter = new MoviesArrayAdapter(context, MainActivity.globalSimpleSearchEngine.sortByTitle(movieList, inAscendingOrder));
            }else{
                moviesArrayAdapter = new MoviesArrayAdapter(context, movieList);
            }

            /*Animations*/
            AnimationSet animationSet = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            animationSet.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            animation.setDuration(100);
            animationSet.addAnimation(animation);

            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
            researchListRecycleView.setLayoutAnimation(controller);

            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            researchListRecycleView.setLayoutManager(layoutManager);
            researchListRecycleView.setHasFixedSize(true);
            researchListRecycleView.setAdapter(moviesArrayAdapter);

        }
    }
}
