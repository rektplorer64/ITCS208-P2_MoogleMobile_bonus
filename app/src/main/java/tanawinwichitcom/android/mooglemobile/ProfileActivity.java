package tanawinwichitcom.android.mooglemobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;

/**
 * Created by tanaw on 3/24/2018.
 */

public class ProfileActivity extends AppCompatActivity{

    private Movie movieEntry;                               /* An entry for Movie which is used for shown data in the activity */
    private Review_RecycleViewAdapter reviewsArrayAdapter;        /* The RecycleViewAdapter for the list of */
    private MovieCategories_RecycleViewAdapter tagsArrayAdapter;              /* The RecycleViewAdapter for the tags */
    private TextView title;                                 /* TextView for the Movie entry's title */
    private RatingBar ratingBar;                            /* RatingBar Indicator for Movie's Rating */
    private TextView reviewCount;                           /* TextView for the total Size of Rating List */
    private TextView averageScore;                          /* TextView for the average score */
    private TextView ratingsCard_count;                     /* TextView for the total Size of Rating List (on the Lower CardView) */
    private TextView movieYear;                             /* TextView for displaying the Movie's Year */
    private RecyclerView reviewRecycleView;                 /* RecycleView for each Review item (on the Lower CardView) */
    private RecyclerView tagRecycleView;                    /* RecycleView for tags */

    private int movieID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* Custom Activity's Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);        /* Disables Default Behavior (Showing App Name on the Toolbar) */

        /* Getting Movie's ID value from the called intent */
        movieID = getIntent().getIntExtra("movieID", 1);                          /* Receives Integer from the Intent which is called by other Activity (By giving value name, and default value) */
        movieEntry = MainActivity.movieMap.get(movieID);                                            /* Use that ID to locate Movie entry by using get(movieID) on public HashMap of Movie */
        //System.out.println(movieEntry);

        /* Binds title TextView to title value of current Movie entry */
        title = findViewById(R.id.movieTitle);
        title.setText(movieEntry.getTitle());

        /* Binds ratingBar RatingBar to rating value of current Movie entry */
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(movieEntry.getMeanRating().floatValue());

        /* Binds reviewCount TextView to review count of current Movie entry */
        reviewCount = findViewById(R.id.reviewCount);
        reviewCount.setText("(" + movieEntry.getRating().size() + ")");

        /* Binds ratingsCard_count TextView (on the lower CardView) to review count of current Movie entry */
        ratingsCard_count = findViewById(R.id.profile_reviewCount);
        ratingsCard_count.setText("(" + movieEntry.getRating().size() + ")");

        /* Binds averageScore TextView to average score of current Movie entry */
        averageScore = findViewById(R.id.score);
        averageScore.setText(String.format("%.1f ", movieEntry.getMeanRating()));

        /* Binds movieYear TextView to movie year of current Movie entry */
        movieYear = findViewById(R.id.movieYear);
        movieYear.setText(" • " + movieEntry.getYear());

        /* Binds Tags List to the RecycleView */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);        /* Specifies the orientation of the RecycleView */
        tagsArrayAdapter = new MovieCategories_RecycleViewAdapter(this, movieEntry.getTags());                                              /* Instantiates the custom ArrayAdapter by giving context and Tags List */
        tagRecycleView = findViewById(R.id.tagRecycleView);                                                                                        /* Binds to RecycleView by its resource ID */
        tagRecycleView.setHasFixedSize(true);                                                                                                      /* Fixes the size of RecycleView (This increases Performance) */
        tagRecycleView.setLayoutManager(layoutManager);                                                                                            /* Binds LayoutManager to RecycleView */
        tagRecycleView.setAdapter(tagsArrayAdapter);                                                                                               /* Binds ArrayAdapter to RecycleView */

        /* Binds Review List to the RecycleView */
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);         /* Specifies the orientation of the RecycleView (LayoutManager cannot be user twice, so it must be created for the second RecycleView) */
        reviewsArrayAdapter = new Review_RecycleViewAdapter(this, movieEntry);                                                              /* Instantiates the custom ArrayAdapter by giving context and Tags List */
        reviewRecycleView = findViewById(R.id.ratingsRecycleView);                                                                                 /* Binds to RecycleView by its resource ID */
        reviewRecycleView.setHasFixedSize(true);                                                                                                   /* Fixes the size of RecycleView (This increases Performance) */
        reviewRecycleView.setLayoutManager(layoutManager2);                                                                                        /* Binds LayoutManager to RecycleView */
        reviewRecycleView.setAdapter(reviewsArrayAdapter);                                                                                         /* Binds ArrayAdapter to RecycleView */

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        FrameLayout cardViewFrameLayout = findViewById(R.id.tagCardView);

        cardViewFrameLayout.startAnimation(bottomUp);
        cardViewFrameLayout.invalidate();
        cardViewFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_toolbarbuttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.closeButton:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
