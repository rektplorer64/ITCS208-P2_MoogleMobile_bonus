package tanawinwichitcom.android.mooglemobile;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tanawinwichitcom.android.mooglemobile.moviefetcher.Movie;

/**
 * Created by tanaw on 3/24/2018.
 */

public class ProfileActivity extends AppCompatActivity{

    private Movie movieEntry;
    private RatingsArrayAdapter ratingsArrayAdapter;
    private TagsArrayAdapter tagsArrayAdapter;
    private TextView title;
    private RatingBar ratingBar;
    private TextView reviewCount;
    private TextView score;
    private TextView ratingsCard_count;
    private TextView movieYear;
    private RecyclerView reviewRecycleView;
    private RecyclerView tagRecycleView;

    private int movieID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        movieID = getIntent().getIntExtra("movieID", 1);
        movieEntry = MainActivity.movieMap.get(movieID);
        System.out.println(movieEntry);

        title = findViewById(R.id.movieTitle);
        title.setText(movieEntry.getTitle());

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(movieEntry.getMeanRating().floatValue());

        reviewCount = findViewById(R.id.reviewCount);
        reviewCount.setText("(" + movieEntry.getRating().size() + ")");

        ratingsCard_count = findViewById(R.id.profile_reviewCount);
        ratingsCard_count.setText("(" + movieEntry.getRating().size() + ")");

        score = findViewById(R.id.score);
        score.setText(String.format("%.1f ", movieEntry.getMeanRating()));

        movieYear = findViewById(R.id.movieYear);
        movieYear.setText(" • " + movieEntry.getYear());

        ratingBar.setRating(movieEntry.getMeanRating().floatValue());
        ratingBar.setIsIndicator(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagsArrayAdapter = new TagsArrayAdapter(this, movieEntry.getTags());
        tagRecycleView = findViewById(R.id.tagRecycleView);
        tagRecycleView.setHasFixedSize(true);
        tagRecycleView.setLayoutManager(layoutManager);
        tagRecycleView.setAdapter(tagsArrayAdapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ratingsArrayAdapter = new RatingsArrayAdapter(this, movieEntry);
        reviewRecycleView = findViewById(R.id.ratingsRecycleView);
        reviewRecycleView.setHasFixedSize(true);
        reviewRecycleView.setLayoutManager(layoutManager2);
        reviewRecycleView.setAdapter(ratingsArrayAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        FrameLayout cardViewFrameLayout = findViewById(R.id.tagCardView);
//        bottomUp.setAnimationListener(new Animation.AnimationListener(){
//            @Override
//            public void onAnimationStart(Animation animation){
////                cardViewFrameLayout.getLayoutParams().height = height * 2;
////                cardViewFrameLayout.requestLayout();
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation){
//                cardViewFrameLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                cardViewFrameLayout.requestLayout();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation){}
//        });
        cardViewFrameLayout.startAnimation(bottomUp);
        cardViewFrameLayout.invalidate();
        cardViewFrameLayout.setVisibility(View.VISIBLE);


    }
}
