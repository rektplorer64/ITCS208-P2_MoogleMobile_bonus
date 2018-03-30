package tanawinwichitcom.android.mooglemobile.Moviefetcher;

import java.util.Objects;

// Name: Tanawin Wichit
// Student ID: 6088221
// Section: 1

public class Rating{
    public Movie m;
    public User u;
    public double rating;    //rating can be [0, 5]
    public long timestamp;    //timestamp tells you the time this rating was recorded

    /**
     * Constructor for Rating Class
     *
     * @param _u         User who given the Rating
     * @param _m         Movie who were given the Rating
     * @param _rating    Score of the Rating
     * @param _timestamp When the Rating was given
     */
    public Rating(User _u, Movie _m, double _rating, long _timestamp){
        if(_rating > 5 || _rating < 0){
            return;
        }
        this.m = _m;
        this.u = _u;
        this.rating = _rating;
        this.timestamp = _timestamp;
    }

    public Movie getMovie(){
        return m;
    }

    public User getUser(){
        return u;
    }

    public double getRating(){
        return rating;
    }

    public long getTimestamp(){
        return timestamp;
    }

    /**
     * Returns field variables as a String
     *
     * @return field variables String
     */
    public String toString(){
        return "[uid: " + u.getID() + " mid: " + m.getID() + " rating: " + rating + "/5 timestamp: " + timestamp + "]";
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Rating)){
            return false;
        }
        Rating rating1 = (Rating) o;
        return Double.compare(rating1.rating, rating) == 0 &&
                timestamp == rating1.timestamp &&
                Objects.equals(m, rating1.m) &&
                Objects.equals(u, rating1.u);
    }

    @Override
    public int hashCode(){

        return Objects.hash(u, rating, timestamp);
    }
}
