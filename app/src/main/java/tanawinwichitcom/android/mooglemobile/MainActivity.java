package tanawinwichitcom.android.mooglemobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Map;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.moviefetcher.SimpleMovieSearchEngine;

public class MainActivity extends AppCompatActivity{

    ListView movieListView = null;
    MoviesArrayAdapter moviesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleMovieSearchEngine simpleMovieSearchEngine = new SimpleMovieSearchEngine(this);

        Map<Integer, Movie> movieMap = simpleMovieSearchEngine.getAllMovies();

        moviesArrayAdapter = new MoviesArrayAdapter(this, movieMap);
        movieListView = findViewById(R.id.movieListView);
        movieListView.setAdapter(moviesArrayAdapter);

        Toast toast = Toast.makeText(this, "There are " + movieMap.size() + " entries.", Toast.LENGTH_LONG);
        toast.show();

    }
}
