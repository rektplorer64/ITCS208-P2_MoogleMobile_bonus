package tanawinwichitcom.android.mooglemobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter.MoviesArrayAdapter;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.SimpleMovieSearchEngine;

public class MainActivity extends AppCompatActivity{

    public static Map<Integer, Movie> movieMap;
    public static SimpleMovieSearchEngine globalSimpleSearchEngine;
    static RecyclerView movieRecyclerView = null;
    static RecyclerView.Adapter movieArrayAdapter = null;
    static RecyclerView.LayoutManager layoutManager;
    static FloatingActionButton floatingActionButton;
    private static ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onResume(){
        //toolbar.setVisibility(View.VISIBLE);
        // Animation toolbar_down = AnimationUtils.loadAnimation(this, R.anim.toolbar_down);
        // toolbar.startAnimation(toolbar_down);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.AppFullScreenTheme);
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mainToolbar);       /* Binds toolbar by ID */
        setSupportActionBar(toolbar);       /* Sets toolbar as the support Action Bar for the Activity */
        getSupportActionBar().setDisplayShowTitleEnabled(false);        /* Hides the default Title Text on Action Bar */

        floatingActionButton = findViewById(R.id.fab);      /* Binds FAB by ID */
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);     /* When FAB is clicked, it will launch SearchActivity */
                startActivity(intent);
            }
        });

        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setExitTransition(new Explode());       /* Sets the exit animation */
        getWindow().setEnterTransition(null);
        getWindow().setReenterTransition(null);
        getWindow().setReturnTransition(null);

        toolbar.setLogo(R.drawable.logo_moogle);        /* Sets the Logo from drawable */

        movieRecyclerView = findViewById(R.id.movieRecyclerView);       /* Binds RecycleView by ID */
        progressBar = findViewById(R.id.loadProgress);      /* Binds ProgressBar by ID */

        SimpleMovieSearchEngine simpleMovieSearchEngine = new SimpleMovieSearchEngine(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){     /* If the Android Version is 7.0 or Higher, sort the Movie list by default */
            new AsyncTaskRunner(this, true, true, this).execute(simpleMovieSearchEngine);
        }else{      /* If the Android Version is lower than 7.0, the Movie list will not be sorted (because of Performance Issue) */
            new AsyncTaskRunner(this, false, false, this).execute(simpleMovieSearchEngine);
        }
    }

    /**
     * Custom AsyncTask Class for loading CSV files in the background
     */
    private static class AsyncTaskRunner extends AsyncTask<SimpleMovieSearchEngine, String, Map<Integer, Movie>>{

        private final Activity activity;
        private final Context context;
        private boolean wantSorted;
        private boolean inAscendingOrder;
        private SimpleMovieSearchEngine movieSearchEngine;

        /**
         * Constructor for AsyncTaskRunner
         *
         * @param context  Context of an Activity (Will be used in ArrayAdapter)
         * @param activity An Activity (Will be used in makeSceneTransition() in ArrayAdapter)
         */
        public AsyncTaskRunner(Context context, Activity activity){
            this.context = context;
            this.activity = activity;

        }

        /**
         * Constructor for AsyncTaskRunner
         *
         * @param context          Context of an Activity (Will be used in ArrayAdapter)
         * @param wantSorted       Boolean will be true if sorting is wanted
         * @param inAscendingOrder Boolean will be true if sorting is wanted in Ascending Order
         * @param activity         An Activity (Will be used in makeSceneTransition() in ArrayAdapter)
         */
        public AsyncTaskRunner(Context context, boolean wantSorted, boolean inAscendingOrder, Activity activity){
            this.context = context;
            this.wantSorted = wantSorted;
            this.inAscendingOrder = inAscendingOrder;
            this.activity = activity;
        }

        /**
         * Loads file in the background by calling loadData()
         *
         * @param simpleMovieSearchEngines An array of SimpleMovieSearchEngine
         *
         * @return The Map filled with loaded data
         */
        @Override
        protected Map<Integer, Movie> doInBackground(SimpleMovieSearchEngine... simpleMovieSearchEngines){
            movieSearchEngine = simpleMovieSearchEngines[0];
            globalSimpleSearchEngine = movieSearchEngine;
            simpleMovieSearchEngines[0].loadData("movies.csv", "ratings.csv");
            return simpleMovieSearchEngines[0].getAllMovies();
        }

        /**
         * Specifies what to do before the background task
         */
        @Override
        protected void onPreExecute(){
            //super.onPreExecute();
            movieRecyclerView.setVisibility(View.GONE);     /* Hides the RecycleView */
            progressBar.setVisibility(View.VISIBLE);        /* Shows the Progress Indicator */
            floatingActionButton.setVisibility(View.GONE);      /* Hides the FAB */
        }

        /**
         * Specifies what to do after the background task
         *
         * @param movieMap the loaded Map from doInBackground()
         */
        @Override
        protected void onPostExecute(Map<Integer, Movie> movieMap){
            //super.onPostExecute(movieMap);
            progressBar.setVisibility(View.GONE);       /* Hides the Progress Indicator */
            MainActivity.movieMap = movieMap;

            if(wantSorted){     /* If sorting is wanted, call sortByTitle then pass ArrayList to MovieArrayAdapter (It has to be ArrayList */
                movieArrayAdapter = new MoviesArrayAdapter(context, movieSearchEngine.sortByTitle(new ArrayList<>(movieMap.values()), inAscendingOrder), activity);
            }else{      /* If sorting is not wanted, just pass the loaded Map right away */
                movieArrayAdapter = new MoviesArrayAdapter(context, movieMap, activity);
            }
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);      /* Sets the RecycleView to vertical */
            movieRecyclerView.setLayoutManager(layoutManager);
            movieRecyclerView.setHasFixedSize(true);        /* For a better Performance */

            /* Animations for RecycleView */
            AnimationSet animationSet = new AnimationSet(true);
            Animation animation = new AlphaAnimation(0.0f, 1.0f);       /* Set Fading Effect */
            animation.setDuration(500);     /* Sets duration to 0.5 seconds */
            animationSet.addAnimation(animation);

            animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            animation.setDuration(100);
            animationSet.addAnimation(animation);

            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
            movieRecyclerView.setLayoutAnimation(controller);       /* Sets animation to the RecycleView */

            /*Set up RecycleView*/
            movieRecyclerView.setAdapter(movieArrayAdapter);

            /* Shows Hidden Views */
            floatingActionButton.setVisibility(View.VISIBLE);
            movieRecyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(context, "There are " + movieMap.size() + " entries.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
        }
    }

}
