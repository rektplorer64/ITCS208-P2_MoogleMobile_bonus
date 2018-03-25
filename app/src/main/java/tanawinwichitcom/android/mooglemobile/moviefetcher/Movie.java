package tanawinwichitcom.android.mooglemobile.moviefetcher;// Name: Tanawin Wichit
// Student ID: 6088221
// Section: 1

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Movie{
    //Field Variables
    private int mid;        /*An integer for Movie ID*/
    private String title;       /*A String for Movie Title*/
    private int year;       /*An integer for Movie's year of release*/
    private Set<String> tags;       /*A Set for Movie's tags*/
    private Map<Integer, Rating> ratings;    //mapping userID -> rating /*A Map for Ratings of the Movie (Integer of UserID is key, Rating is value)*/
    private Double avgRating;       /*A Double for Average Rating*/
    //additional

    /**
     * The constructor for Movie class
     *
     * @param _mid   Movie ID
     * @param _title Movie Title
     * @param _year  Movie's year of release
     */
    public Movie(int _mid, String _title, int _year){
        this.mid = _mid;
        this.title = _title;
        this.year = _year;

        tags = new HashSet<>();
        ratings = new HashMap<>();
    }

    /**
     * Returns Movie ID as an integer
     *
     * @return Movie ID
     */
    public int getID(){
        return mid;
    }

    /**
     * Returns Movie Title as a String
     *
     * @return Movie Title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Returns Movie tags as a Set
     *
     * @return Movie tags
     */
    public Set<String> getTags(){
        return tags;
    }

    /**
     * Adds a tag to the tags Set by giving a String
     *
     * @param tag A tag to add
     */
    public void addTag(String tag){
        tags.add(tag);
    }

    /**
     * Returns Movie's Year of release as an integer
     *
     * @return Movie's Year of release
     */
    public int getYear(){
        return year;
    }

    /**
     * Returns field variables as a String
     *
     * @return field variables String
     */
    public String toString(){
        avgRating = calMeanRating();
        return "[mid: " + mid + ":" + title + " (" + year + ") " + tags + "] -> avg rating: " + avgRating;
    }

    /**
     * Returns string of the Movie based on input parameters
     *
     * @param wantTitle   Boolean of the need for the Title
     * @param wantTag     Boolean of the need for the Tag
     * @param wantYear    Boolean of the need for the Year
     * @param wantRatings Boolean of the need for the Rating
     *
     * @return the String for querying
     */
    public String toQueryString(boolean wantTitle, boolean wantTag, boolean wantYear, boolean wantRatings){
        avgRating = calMeanRating();
        if(!wantTitle && !wantTag && !wantYear && !wantRatings){
            return toString();
        }else{
            return ((wantTitle) ? title : "") + ((wantYear) ? (" (" + year + ") ") : "") +
                    ((wantTag) ? (" " + tags + " ") : "") + " " + ((wantRatings) ? (" " + avgRating + " ") : "");
        }
    }

    /**
     * Calculates the average Rating
     *
     * @return average Rating
     */
    public double calMeanRating(){
        double sum = 0;
        //System.out.println("rating size " + ratings.size());
        for(Rating f : ratings.values()){
            sum += f.rating;
        }
        avgRating = sum / ratings.size();
        return avgRating;
    }

    /**
     * Calculates the average Rating and return it
     *
     * @return average Rating
     */
    public Double getMeanRating(){
        avgRating = calMeanRating();
        return avgRating;
    }

    public void addRating(User user, Movie movie, double rating, long timestamp){
        ratings.put(user.getID(), new Rating(user, movie, rating, timestamp));
    }

    public Map<Integer, Rating> getRating(){
        return ratings;
    }

    public static boolean isAvailableInTheDatabase(int movieID, Map<Integer, Movie> moviesMap){
        return moviesMap.get(movieID) != null && movieID == moviesMap.get(movieID).mid;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }

        Movie movie = (Movie) o;

        if(mid != movie.mid){
            return false;
        }
        if(year != movie.year){
            return false;
        }
        if(!title.equals(movie.title)){
            return false;
        }
        if(!tags.equals(movie.tags)){
            return false;
        }
        if(!ratings.equals(movie.ratings)){
            return false;
        }
        return avgRating.equals(movie.avgRating);
    }

    @Override
    public int hashCode(){
        int result = mid;
        result = 31 * result + title.hashCode();
        result = 31 * result + year;
        result = 31 * result + tags.hashCode();
        result = 31 * result + ratings.hashCode();
        result = 31 * result + avgRating.hashCode();
        return result;
    }
}
