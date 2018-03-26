package tanawinwichitcom.android.mooglemobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.moviefetcher.SimpleMovieSearchEngine;

public class MainActivity extends AppCompatActivity{

    static RecyclerView movieRecyclerView = null;
    private static ProgressBar progressBar;
    static RecyclerView.Adapter movieArrayAdapter = null;
    static RecyclerView.LayoutManager layoutManager;

    public static Map<Integer, Movie> movieMap;
    public static SimpleMovieSearchEngine globalSimpleSearchEngine;

    static FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        progressBar = findViewById(R.id.loadProgress);

        SimpleMovieSearchEngine simpleMovieSearchEngine = new SimpleMovieSearchEngine(this);
        new AsyncTaskRunner(this, true, true, this).execute(simpleMovieSearchEngine);
    }

    private static class AsyncTaskRunner extends AsyncTask<SimpleMovieSearchEngine, String, Map<Integer, Movie>>{

        private final Activity activity;
        private final Context context;
        private boolean wantSorted;
        private boolean inAscendingOrder;
        private SimpleMovieSearchEngine movieSearchEngine;

        public AsyncTaskRunner(Context context, Activity activity){
            this.context = context;
            this.activity = activity;

        }

        public AsyncTaskRunner(Context context, boolean wantSorted, boolean inAscendingOrder, Activity activity){
            this.context = context;
            this.wantSorted = wantSorted;
            this.inAscendingOrder = inAscendingOrder;
            this.activity = activity;
        }

        @Override
        protected Map<Integer, Movie> doInBackground(SimpleMovieSearchEngine... simpleMovieSearchEngines){
            movieSearchEngine = simpleMovieSearchEngines[0];
            globalSimpleSearchEngine = movieSearchEngine;
            simpleMovieSearchEngines[0].loadData("movies.csv", "ratings.csv");
            return simpleMovieSearchEngines[0].getAllMovies();
        }

        @Override
        protected void onPreExecute(){
            //super.onPreExecute();
            movieRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Map<Integer, Movie> movieMap){
            //super.onPostExecute(movieMap);
            progressBar.setVisibility(View.GONE);
            MainActivity.movieMap = movieMap;
            if(wantSorted){
                movieArrayAdapter = new MoviesArrayAdapter(context, movieSearchEngine.sortByTitle(new ArrayList<>(movieMap.values()), inAscendingOrder), activity);
            }else{
                movieArrayAdapter = new MoviesArrayAdapter(context, movieMap, activity);
            }
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            movieRecyclerView.setLayoutManager(layoutManager);
            movieRecyclerView.setHasFixedSize(true);

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
            movieRecyclerView.setLayoutAnimation(controller);

            /*Set up RecycleView*/
            movieRecyclerView.setAdapter(movieArrayAdapter);
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
