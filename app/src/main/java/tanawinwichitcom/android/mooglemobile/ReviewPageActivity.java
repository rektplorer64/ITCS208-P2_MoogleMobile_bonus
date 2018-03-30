package tanawinwichitcom.android.mooglemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Map;

import tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter.Review_RecycleViewAdapter;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.Rating;

/**
 * Created by tanaw on 3/26/2018.
 */

public class ReviewPageActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reviewpage);

        /* Custom Activity's Toolbar */
        Toolbar toolbar = findViewById(R.id.reviewToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);        /* Disables Default Behavior (Showing App Name on the Toolbar) */

        Intent intent = getIntent();
        int movieID = intent.getIntExtra("movieID", 1);     /* Receives an integer of movieID from previous Activity */

        Movie movieEntry = MainActivity.movieMap.get(movieID);      /* Gets a Movie instance by getting it from Movie Map using movieID as the key */
        Map<Integer, Rating> ratingMap = movieEntry.getRating();        /* Gets the Map of rating */

        /* Sets Text for Big Average Rating */
        TextView bigAverageScoreText = findViewById(R.id.bigAverageScoreText);
        bigAverageScoreText.setText(String.format("%.1f", movieEntry.getMeanRating()));

        /* Set Text for Total User Review */
        TextView totalUserReviews = findViewById(R.id.totalUserReviews);
        totalUserReviews.setText(String.valueOf(movieEntry.getRating().size()));

        /* Set RatingBar according to the Average Rating */
        RatingBar averageRatingBar = findViewById(R.id.averageRatingBar);
        averageRatingBar.setRating(Float.valueOf(String.valueOf(movieEntry.getMeanRating())));

        /* 5 Stars */
        int countUser_five = countUserByRating(5, ratingMap);       /* Count the number of user who gave 5 stars */
        ProgressBar rating_fiveStar = findViewById(R.id.rating_fiveStar);
        rating_fiveStar.setProgress(countUser_five);           /* Set the progress of the progress bar */
        rating_fiveStar.setMax(ratingMap.size());       /* Sets the Max value of the progress bar */
        TextView rating_fiveStarText = findViewById(R.id.rating_fiveStarText);
        rating_fiveStarText.setText(String.valueOf(countUser_five));        /* Set the TextView to show total number of people who gave 5 stars */

        /* 4 Stars */
        int countUser_four = countUserByRating(4, ratingMap);
        ProgressBar rating_fourStar = findViewById(R.id.rating_fourStar);
        rating_fourStar.setProgress(countUser_four);
        rating_fourStar.setMax(ratingMap.size());
        TextView rating_fourStarText = findViewById(R.id.rating_fourStarText);
        rating_fourStarText.setText(String.valueOf(countUser_four));

        /* 3 Stars */
        int countUser_three = countUserByRating(3, ratingMap);
        ProgressBar rating_threeStar = findViewById(R.id.rating_threeStar);
        rating_threeStar.setProgress(countUser_three);
        rating_threeStar.setMax(ratingMap.size());
        TextView rating_threeStarText = findViewById(R.id.rating_threeStarText);
        rating_threeStarText.setText(String.valueOf(countUser_three));

        /* 2 Stars */
        int countUser_two = countUserByRating(2, ratingMap);
        ProgressBar rating_twoStar = findViewById(R.id.rating_twoStar);
        rating_twoStar.setProgress(countUser_two);
        rating_twoStar.setMax(ratingMap.size());
        TextView rating_twoStarText = findViewById(R.id.rating_twoStarText);
        rating_twoStarText.setText(String.valueOf(countUser_two));

        /* 1 Star */
        int countUser_one = countUserByRating(1, ratingMap);
        ProgressBar rating_oneStar = findViewById(R.id.rating_oneStar);
        rating_oneStar.setProgress(countUser_one);
        rating_oneStar.setMax(ratingMap.size());
        TextView rating_oneStarText = findViewById(R.id.rating_oneStarText);
        rating_oneStarText.setText(String.valueOf(countUser_one));

        RecyclerView fullReviewRecycleView = findViewById(R.id.fullReviewRecycleView);                  /* Binds to RecycleView by its resource ID */
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);         /* Specifies the orientation of the RecycleView (LayoutManager cannot be user twice, so it must be created for the second RecycleView) */
        Review_RecycleViewAdapter reviewsArrayAdapter = new Review_RecycleViewAdapter(this, movieEntry);                                                              /* Instantiates the custom ArrayAdapter by giving context and Tags List */
        fullReviewRecycleView.setHasFixedSize(true);                                                                                                   /* Fixes the size of RecycleView (This increases Performance) */
        fullReviewRecycleView.setLayoutManager(layoutManager2);                                                                                        /* Binds LayoutManager to RecycleView */
        fullReviewRecycleView.setAdapter(reviewsArrayAdapter);
    }

    /**
     * Binds ToolBar to XML Files
     *
     * @param menu Menu
     *
     * @return Successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_toolbarbuttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.closeButton:      /* When Click Close Button */
                onBackPressed();        /* Invokes the back button to get back to previous Activity */
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Counts Number of Users who gave review score
     *
     * @param score     Score value
     * @param ratingMap Rating Map
     *
     * @return Number of User who gave the score
     */
    private int countUserByRating(double score, Map<Integer, Rating> ratingMap){
        int count = 0;
        for(int key : ratingMap.keySet()){
            if(Math.round(score) == Math.round(ratingMap.get(key).getRating())){
                count++;
            }
        }
        return count;
    }
}
