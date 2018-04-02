package tanawinwichitcom.android.mooglemobile.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter.MoviesArrayAdapter;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.R;

import static tanawinwichitcom.android.mooglemobile.Activities.BrowseMovieActivity.movieMap;

/**
 * Created by tanaw on 3/23/2018.
 */

public class SearchResultActivity extends AppCompatActivity{

    /*Variables for Search options*/
    static boolean wantSort, wantAscendingOrder, wantTitle, wantTag, wantYear, wantRatings;
    static int sortType;
    RecyclerView resultListRecycleView;
    ProgressBar progressBar;
    MoviesArrayAdapter moviesArrayAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView progressDetails;

    /**
     * Receives the Search Option booleans
     *
     * @param _wantSort           Boolean for Sorting, True is want to Sort
     * @param _wantAscendingOrder Boolean for the Order of sorting, True is want to Sort in Ascending Order
     * @param _wantTitle          Boolean for Searching, True is want to include Title in Searching
     * @param _wantTag            Boolean for Searching, True is want to include Tag in Searching
     * @param _wantYear           Boolean for Searching, True is want to include Year in Searching
     * @param _wantRatings        Boolean for Searching, True is want to include Ratings in Searching
     * @param _sortType           Boolean for Searching, True is want to include Type in Searching
     */
    public static void setSearchOptions(boolean _wantSort, boolean _wantAscendingOrder
            , boolean _wantTitle, boolean _wantTag, boolean _wantYear, boolean _wantRatings, int _sortType){
        // Variable for Radio
        wantSort = _wantSort;
        wantAscendingOrder = _wantAscendingOrder;

        // Variables for CheckBox
        wantTitle = _wantTitle;
        wantTag = _wantTag;
        wantYear = _wantYear;
        wantRatings = _wantRatings;
        sortType = _sortType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        /* Binds FAB by ID (Because this activity borrows XML layout from BrowseMovieActivity) */
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.GONE);      /* Hides FAB */

        resultListRecycleView = findViewById(R.id.movieRecyclerView);        /* Binds RecycleView by ID */
        progressBar = findViewById(R.id.loadProgress);      /* Binds ProgressBar by ID */
        progressDetails = findViewById(R.id.loadDetail);        /* Binds progress TextView by ID */

        Toolbar toolbar = findViewById(R.id.mainToolbar);       /* Binds Toolbar by ID */
        setSupportActionBar(toolbar);       /* Sets toolbar as the support Action Bar for the Activity */
        getSupportActionBar().setDisplayShowTitleEnabled(true);        /* Hides the Title in the Action Bar */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");

        // ImageButton backImageButton = findViewById(R.id.backArrow);
        // backImageButton.setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     public void onClick(View v){
        //         onBackPressed();        /* When clicking back arrow, it will return to the previous activity */
        //     }
        // });

        handleIntent(getIntent());
    }

    /**
     * Handles every intent called to this Activity by other Activity
     *
     * @param intent Intent from other Activity
     */
    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);      /* Receives String (Query String) attached with the intent */
            new AsyncTaskSearch(this, wantSort, wantAscendingOrder, this).execute(query);
        }
    }

    /**
     * Custom AsyncTask for searching
     */
    private class AsyncTaskSearch extends AsyncTask<String, Integer, List<Movie>>{

        private final Context context;
        private final boolean inAscendingOrder;
        private final boolean wantSorted;
        private final Activity activity;
        private int mapSize;

        /**
         * Constructor for AsyncTaskSearch class
         *
         * @param context          Context of an Activity (Will be used in ArrayAdapter)
         * @param wantSorted       Boolean will be true if sorting is wanted
         * @param inAscendingOrder Boolean will be true if sorting is wanted in Ascending Order
         * @param activity         An Activity (Will be used in makeSceneTransition() in ArrayAdapter)
         */
        public AsyncTaskSearch(Context context, boolean wantSorted, boolean inAscendingOrder, Activity activity){
            this.context = context;
            this.wantSorted = wantSorted;
            this.inAscendingOrder = inAscendingOrder;
            this.activity = activity;
        }

        /**
         * Searches the query in the background
         *
         * @param strings Array of Keywords
         *
         * @return List of the Result
         */
        @Override
        protected List<Movie> doInBackground(String... strings){
            ArrayList<Movie> moviesSearchResults = new ArrayList<>();
            String query = strings[0].trim().toLowerCase();    /* Change to lower case first */
            String[] queries = query.split(" ");    /* Split String by whitespaces */

            int j = 0;
            mapSize = movieMap.size();
            for(int movieID : movieMap.keySet()){       /* Iterates through the Movie Map  */
                Movie currentMovie = movieMap.get(movieID);     /* Gets the Entry */
                for(int i = 0; i < queries.length; i++){        /* Iterates through the Array of Keywords */
                    if(currentMovie.toQueryString(wantTitle, wantTag, wantYear, wantRatings).toLowerCase().contains(queries[i])){       /* If the criteria is met */
                        System.out.println(currentMovie.toString().toLowerCase());
                        moviesSearchResults.add(currentMovie);      /* Adds it to the List of Result */
                    }
                }
                //System.out.println("MovieID = " + movieID);
                j++;
                publishProgress(j);     /* Publishes Progress so it can be updated via UI on screen */
            }
            Set<Movie> movieSet = new HashSet<>();
            movieSet.addAll(moviesSearchResults);       /* Convert to set so duplicate item will get merged */
            //getSupportActionBar().setTitle((movieSet.size() != 0)? ("There are " + movieSet.size() + " movies.") : "There is no result");
            return new ArrayList<>(movieSet);       /* Converts Set to ArrayList then returns */
        }

        /**
         * Specifies what to do before the background task
         */
        @Override
        protected void onPreExecute(){
            resultListRecycleView.setVisibility(View.GONE);       /* Hides the RecycleView */
            progressBar.setVisibility(View.VISIBLE);        /* Show the progress indicator */
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            progressDetails.setText(values[0] + " out of " + mapSize);      /* Updates the Progression via TextView */
        }

        /**
         * Specifies what to do after the background task
         *
         * @param movieList the search result List from doInBackground()
         */
        @Override
        protected void onPostExecute(List<Movie> movieList){
            resultListRecycleView.setVisibility(View.VISIBLE);      /* Shows the result */
            progressBar.setVisibility(View.GONE);       /* Hides the progress bar */

            if(movieList.size() == 0){      /* If the list is empty */
                progressDetails.setText("No Data to be shown, please re-enter your criteria. :D");
                return;     /* Stop doing something else */
            }

            progressDetails.setVisibility(View.GONE);       /* Hides the progress String */

            /* Assigns movieArrayAdapter based on the sorting boolean */
            if(wantSorted){         /* If sorting is wanted */
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    moviesArrayAdapter = new MoviesArrayAdapter(context, BrowseMovieActivity.globalSimpleSearchEngine.sortByGivenType(movieList, inAscendingOrder, sortType), activity);
                }else{      /* If sorting is not wanted */
                    moviesArrayAdapter = new MoviesArrayAdapter(context, BrowseMovieActivity.globalSimpleSearchEngine.sortByTitle(movieList, inAscendingOrder), activity);
                }
            }else{
                moviesArrayAdapter = new MoviesArrayAdapter(context, movieList, activity);
            }

            /* Animations for the RecycleView */
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
            resultListRecycleView.setLayoutAnimation(controller);

            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            resultListRecycleView.setLayoutManager(layoutManager);      /* Sets layout manager to RecycleView */
            resultListRecycleView.setHasFixedSize(true);        /* Increases Performance */
            resultListRecycleView.setAdapter(moviesArrayAdapter);       /* Sets ArrayAdapter to RecycleView */
        }
    }
}
