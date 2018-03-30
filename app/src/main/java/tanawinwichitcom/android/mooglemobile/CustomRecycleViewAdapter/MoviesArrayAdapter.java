package tanawinwichitcom.android.mooglemobile.CustomRecycleViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tanawinwichitcom.android.mooglemobile.Moviefetcher.Movie;
import tanawinwichitcom.android.mooglemobile.ProfileActivity;
import tanawinwichitcom.android.mooglemobile.R;

/**
 * Created by tanaw on 3/21/2018.
 */

public class MoviesArrayAdapter extends RecyclerView.Adapter<MoviesArrayAdapter.ViewHolder>{
    private final ArrayList<Movie> movieArrayList;
    private final Context context;
    private final Activity activity;

    private RecyclerView horizontalRecyclerView;

    /**
     * Constructor for MoviesArrayAdapter class
     *
     * @param context  Context of the Activity that calls
     * @param movieMap the Movie map for data binding to each item
     * @param activity An Activity (Will be used in makeSceneTransition() in ArrayAdapter)
     */
    public MoviesArrayAdapter(Context context, Map<Integer, Movie> movieMap, Activity activity){
        movieArrayList = new ArrayList<>(movieMap.values());        /* Converts Map to ArrayList */
        this.context = context;
        this.activity = activity;
    }

    /**
     * Constructor for MoviesArrayAdapter class
     *
     * @param context   Context of the Activity that calls
     * @param movieList the Movie list for data binding to each item
     * @param activity  An Activity (Will be used in makeSceneTransition() in ArrayAdapter)
     */
    public MoviesArrayAdapter(Context context, List<Movie> movieList, Activity activity){
        movieArrayList = new ArrayList<>(movieList);        /* Converts List to ArrayList */
        this.context = context;
        this.activity = activity;
    }

    /**
     * Specifies the XML layout file for each item.
     *
     * @param parent   Parent
     * @param viewType ViewType
     *
     * @return ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View rootView = LayoutInflater.from(context).inflate(R.layout.moviecard_layout, parent, false);
        return new ViewHolder(rootView);
    }

    /**
     * This method fixes the issue while scrolling some content mess up randomly
     *
     * @param position Position
     *
     * @return Position
     */
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Movie movieEntry = movieArrayList.get(position);
        //Toast.makeText(context, "Movie Title = " + movieEntry.getTitle() + "| Array Position = " + position, Toast.LENGTH_SHORT).show();
        if(movieEntry != null){
            holder.movieTitle.setText(movieEntry.getTitle());       /* Sets Title text to the TextView */

            if(movieEntry.getTags().size() != 0){       /* If the movie has at least a Tag, set up RecycleView for Tags */
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.Adapter tagsArrayAdapter = new MovieCategories_RecycleViewAdapter(context, movieEntry.getTags());
                horizontalRecyclerView.setHasFixedSize(true);       /* For a better Performance */
                horizontalRecyclerView.setLayoutManager(layoutManager);
                horizontalRecyclerView.setAdapter(tagsArrayAdapter);        /* Binds Adapter to RecycleView */
            }else{
                horizontalRecyclerView.setVisibility(View.GONE);
            }

            holder.reviewCount.setText("(" + movieEntry.getRating().size() + ")");      /* Sets Review Count text to the TextView */
            //System.out.println("rating size " + movieEntry.getRating().size());
            holder.score.setText(String.format("%.1f ", movieEntry.getMeanRating()));       /* Sets Mean Average text to the TextView */
            holder.movieYear.setText(" • " + movieEntry.getYear());     /* Sets Year text to the TextView */
            holder.ratingBar.setIsIndicator(true);      /* Sets RatingBar as a Indicator (Cannot change value) */
            holder.ratingBar.setRating(movieEntry.getMeanRating().floatValue());        /* Sets Rating to the RatingBar */

            //TODO: Disable For now
            holder.favorite.setVisibility(View.GONE);
            holder.lookUp.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener(){      /* Sets When user tap a list item */
                @Override
                public void onClick(View v){

                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("movieID", movieEntry.getID());

                    View decor = ((Activity) context).getWindow().getDecorView();               /* Gets DecorationView by casting Context yo Activity */
                    View statusBar = decor.findViewById(android.R.id.statusBarBackground);          /* Pair StatusBar by ID */
                    View navigationBar = decor.findViewById(android.R.id.navigationBarBackground);      /* Pair NavigationBar by ID */

                    ArrayList<Pair<View, String>> pairArrayList = new ArrayList<>();        /* ArrayList for the Shared Element Transitions */
                    //pairArrayList.add(Pair.create(holder.heroImageView, "movieImage"));
                    pairArrayList.add(Pair.create(holder.movieTitle, "movieTitle"));
                    pairArrayList.add(Pair.create(holder.reviewCount, "reviewCount"));
                    pairArrayList.add(Pair.create(holder.ratingBar, "ratingBar"));
                    pairArrayList.add(Pair.create(holder.movieYear, "year"));
                    pairArrayList.add(Pair.create(holder.score, "score"));
                    pairArrayList.add(Pair.create(holder.tagRecycle, "tagRecycler"));
                    //pairArrayList.add(Pair.create(holder.cardView, "movieCard"));
                    //pairArrayList.add(Pair.create(actionBar, "toolbar"));

                    /* This WILL PREVENT THE OVERLAP BETWEEN SHARE ELEMENTS AND STATUS BAR */
                    if(statusBar != null){
                        pairArrayList.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                    }

                    /* This WILL PREVENT THE OVERLAP BETWEEN SHARE ELEMENTS AND NAVIGATION BAR */
                    if(navigationBar != null){
                        pairArrayList.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
                    }

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(activity, pairArrayList.toArray(new Pair[pairArrayList.size()]));
                    System.out.println(movieEntry.getID());

                    holder.heroImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));

                    //Toolbar actionBar = decor.findViewById(R.id.mainToolbar);
                    //Animation toolBar_up = AnimationUtils.loadAnimation(context, R.anim.toolbar_up);
                    //actionBar.startAnimation(toolBar_up);
                    //actionBar.invalidate();
                    //actionBar.setVisibility(View.GONE);

                    context.startActivity(intent, optionsCompat.toBundle());        /* Start ProfileActivity with Shared Element Transitions Animations */
                }
            });
        }
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public int getItemCount(){
        return movieArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        // Every elements on each Movie Card
        private TextView reviewCount;
        private TextView score;
        private TextView movieTitle;
        private RatingBar ratingBar;
        private TextView movieYear;
        private View itemView;
        private View heroImageView;
        private RecyclerView tagRecycle;
        private CardView cardView;

        private ImageButton favorite;
        private ImageButton lookUp;

        /**
         * Binds Every View on Each item by their IDs
         *
         * @param itemView Parent View specified in onCreateViewHolder()
         */
        ViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.movieCard);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            reviewCount = itemView.findViewById(R.id.review);
            score = itemView.findViewById(R.id.score);
            horizontalRecyclerView = itemView.findViewById(R.id.tagRecycleView);
            tagRecycle = horizontalRecyclerView;
            ratingBar = itemView.findViewById(R.id.ratingBar);
            movieYear = itemView.findViewById(R.id.movieYear);
            heroImageView = itemView.findViewById(R.id.heroimage);

            favorite = itemView.findViewById(R.id.favoriteButton);
            lookUp = itemView.findViewById(R.id.lookUpButton);

            this.itemView = itemView;
        }
    }

}
