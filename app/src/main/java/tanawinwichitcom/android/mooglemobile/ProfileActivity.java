package tanawinwichitcom.android.mooglemobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter.MovieCategories_RecycleViewAdapter;
import tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter.Review_RecycleViewAdapter;
import tanawinwichitcom.android.mooglemobile.Moviefetcher.Movie;

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
    private Button moreReviewButton;
    private ImageView heroImage;

    private ImageButton favoriteButton;
    private ImageButton addToPlaylistButton;
    private ImageButton lookUpButton;
    private ImageButton shareButton;

    private int movieID;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
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

        heroImage = findViewById(R.id.imageView);
        heroImage.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_down));

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

        moreReviewButton = findViewById(R.id.moreReviewButton);
        if(movieEntry.getRating().size() == 0){
            moreReviewButton.setVisibility(View.GONE);
        }else{
            moreReviewButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(getBaseContext(), ReviewPageActivity.class);
                    intent.putExtra("movieID", movieID);
                    startActivity(intent);
                }
            });
        }

        /* Binds Tags List to the RecycleView */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);        /* Specifies the orientation of the RecycleView */
        tagsArrayAdapter = new MovieCategories_RecycleViewAdapter(this, movieEntry.getTags());                                              /* Instantiates the custom ArrayAdapter by giving context and Tags List */
        tagRecycleView = findViewById(R.id.tagRecycleView);                                                                                        /* Binds to RecycleView by its resource ID */
        tagRecycleView.setHasFixedSize(true);                                                                                                      /* Fixes the size of RecycleView (This increases Performance) */
        tagRecycleView.setLayoutManager(layoutManager);                                                                                            /* Binds LayoutManager to RecycleView */
        tagRecycleView.setAdapter(tagsArrayAdapter);                                                                                               /* Binds ArrayAdapter to RecycleView */

        /* Sets up ImageButtons */
        favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: Implement Adding Favorite
                Toast.makeText(context, "Favorite feature Coming Soon(TM)", Toast.LENGTH_SHORT).show();
            }
        });

        addToPlaylistButton = findViewById(R.id.addToPlaylistButton);
        addToPlaylistButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: Implement Adding to Playlist
                Toast.makeText(context, "Add to Playlist feature Coming Soon(TM)", Toast.LENGTH_SHORT).show();
            }
        });

        lookUpButton = findViewById(R.id.lookUpButton);
        lookUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, movieEntry.getTitle()); // query contains search string
                context.startActivity(intent);
            }
        });

        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: Implement Sharing
                Toast.makeText(context, "Share feature Coming Soon(TM)", Toast.LENGTH_SHORT).show();
            }
        });

        /* Binds Review List to the RecycleView */
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);         /* Specifies the orientation of the RecycleView (LayoutManager cannot be user twice, so it must be created for the second RecycleView) */
        reviewsArrayAdapter = new Review_RecycleViewAdapter(this, movieEntry, 4);                                                              /* Instantiates the custom ArrayAdapter by giving context and Tags List */
        reviewRecycleView = findViewById(R.id.ratingsRecycleView);                                                                                 /* Binds to RecycleView by its resource ID */
        reviewRecycleView.setHasFixedSize(true);                                                                                                   /* Fixes the size of RecycleView (This increases Performance) */
        reviewRecycleView.setLayoutManager(layoutManager2);                                                                                        /* Binds LayoutManager to RecycleView */
        reviewRecycleView.setAdapter(reviewsArrayAdapter);                                                                                         /* Binds ArrayAdapter to RecycleView */

        /* UNUSED - Getting Screen Width and Height */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        /* Sets the animation for the Actions Card */
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);      /* Loads from XML file in res/anim */
        FrameLayout actionsCardFrameLayout = findViewById(R.id.actionsCardLayout);
        actionsCardFrameLayout.startAnimation(bottomUp);
        actionsCardFrameLayout.invalidate();
        actionsCardFrameLayout.setVisibility(View.VISIBLE);

        /* Sets the animation for the Review Card */
        FrameLayout reviewCardFrameLayout = findViewById(R.id.reviewCardLayout);
        reviewCardFrameLayout.startAnimation(bottomUp);
        reviewCardFrameLayout.invalidate();
        reviewCardFrameLayout.setVisibility(View.VISIBLE);
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
