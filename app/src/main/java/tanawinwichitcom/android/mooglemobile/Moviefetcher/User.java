package tanawinwichitcom.android.mooglemobile.Moviefetcher;
// Name: Tanawin Wichit
// Student ID: 6088221
// Section: 1

import java.util.HashSet;
import java.util.Set;

public class User{
    public static int lastUserID;

    private int uid;
    private String username;
    private String password;
    private String email;

    private Set<Movie> favoriteSet;

    public User(int _id){
        this.uid = _id;
        lastUserID = _id;
        favoriteSet = new HashSet<>();
    }

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.uid = lastUserID;
    }

    public int getID(){
        return uid;
    }

    public Set<Movie> getFavoriteSet(){
        return favoriteSet;
    }

    public void addFavorite(Movie movie){
        favoriteSet.add(movie);
    }

    public void removeFavorite(Movie movie){
        favoriteSet.remove(movie);
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        User user = (User) o;
        return uid == user.uid;
    }

    @Override
    public int hashCode(){
        return uid;
    }
}
