package tanawinwichitcom.android.mooglemobile.moviefetcher;// Name: Tanawin Wichit
// Student ID: 6088221
// Section: 1

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMovieSearchEngine implements BaseMovieSearchEngine{
    private Map<Integer, Movie> movies;
    private Context context;


    /**
     * Constructor for the SearchEngine
     */
    public SimpleMovieSearchEngine(Context context){
        movies = new HashMap<>();       /*Initiates the Map*/
        this.context = context;
        //loadData("movies.csv", "ratings.csv");
    }

    /**
     * Fetches movies.csv and extracts it into the movies Map
     *
     * @param movieFilename String value for the location of the CSV file
     *
     * @return A Map filled with fetched Data
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Map<Integer, Movie> loadMovies(String movieFilename){
        Map<Integer, Movie> moviesMap = new HashMap<>();    /*The Map*/

        //Reading file by using StringBuilder
        String contentBuilder = "";
        try{
            InputStream is = context.getAssets().open(movieFilename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            contentBuilder = new String(buffer);

        }catch(IOException ex){
            ex.printStackTrace();
        }

        String[] lines = contentBuilder.split("\n");        /* Separating Each lines into String */

        //Pattern for CSV; For example
        //101529,"Brass Teapot, The (2012)",Comedy|Fantasy|Thriller
        //  (1) , |-----(2)-------|  |(3)|  |--------(4)----------|
        Pattern eachLinePattern = Pattern.compile("(\\d+),[\\\"?]?(.*)\\s\\((\\d+)\\)[\\\"?]?,(.+)");

        //Fields Variable for Movie Datatype
        int movieID, year;
        String movieTitle, movieGenres;

        //Pattern for the forth group of eachLinePattern; For example
        //comedy|Fantasy|Thriller
        //  (1)    (2)      (3)
        Pattern categoriesPattern = Pattern.compile("([^|]+)");
        Matcher matcher;

        for(int i = 1; i < lines.length; i++){
            matcher = eachLinePattern.matcher(lines[i]);
            if(!matcher.find()){
                continue;
            }

            movieID = Integer.parseInt(matcher.group(1));       /*Assigns group 1 into movieID field variable*/
            movieTitle = matcher.group(2);       /*Assigns group 2 into movieTitle field variable*/
            year = Integer.parseInt(matcher.group(3));      /*Assigns group 3 into year field variable*/
            movieGenres = matcher.group(4);     /*Assigns group 4 into movieGenres field variable*/
            moviesMap.put(movieID, new Movie(movieID, movieTitle, year));    /*Stores all fields into the Map (the Key is movieID, and the value is a movie object)*/

            matcher = categoriesPattern.matcher(movieGenres);       /*Sets the pattern for the Categories String*/

            if(movieGenres.equals("(no genres listed)")){   /*If there is no categories to be assign*/
                moviesMap.get(movieID).addTag("Uncategorized");
                continue;
            }

            //Assigns each categories to each Movie Object by using its movieID
            //System.out.println(movieGenres);
            while(matcher.find()){
                //System.out.println("Genres = " + matcher.group());
                moviesMap.get(movieID).addTag(matcher.group());        //j is actually the movieID
            }

        }
        return moviesMap;
    }

    /**
     * Fetches ratings.csv and extracts it into the set of each Movie Object
     *
     * @param ratingFilename String value for the location of the CSV file
     */
    @Override
    public void loadRating(String ratingFilename){
        //Reading file by using StringBuilder
        String contentBuilder = "";
        try{
            InputStream is = context.getAssets().open(ratingFilename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            contentBuilder = new String(buffer);

        }catch(IOException ex){
            ex.printStackTrace();
        }
        String[] line = contentBuilder.split("\n");        /* Separating Each lines into String */

        //Pattern for the each line of Rating.csv; For example
        //668,108940,2.5,1391840917
        //(1)  (2)   (3)     (4)
        String eachLinePattern = "([\\d]+),([\\d]+),(.*),(\\d+)";
        Pattern pattern = Pattern.compile(eachLinePattern);

        //Fields Variable for Rating Datatype
        int userID;
        int movieId;
        double rating;
        long timestamp;

        for(int i = 1; i < line.length; i++){
            Matcher m = pattern.matcher(line[i]);
            if(m.find()){
                userID = Integer.parseInt(m.group(1));      /*Assigns group 1 into userID field variable*/
                movieId = Integer.parseInt(m.group(2));     /*Assigns group 2 into movieId field variable*/
                if(!Movie.isAvailableInTheDatabase(movieId, movies)){       /*If movie ID is not in the map skip it*/
                    continue;
                }
                rating = Double.parseDouble(m.group(3));        /*Assigns group 3 into rating field variable*/
                timestamp = Long.parseLong(m.group(4));      /*Assigns group 4 into timestamp field variable*/
                movies.get(movieId).addRating(new User(userID), movies.get(movieId), rating, timestamp);        /*Puts every fields into the set in a Movie Object*/
                //System.out.println(movies.get(movieId).getRating().values());
            }
        }
    }

    /**
     * Fetches data from CSV files
     *
     * @param movieFilename  String value for the location of the movies CSV file
     * @param ratingFilename String value for the location of the ratings CSV file
     */
    @Override
    public void loadData(String movieFilename, String ratingFilename){
        //Loads Movies then Ratings
        movies = loadMovies(movieFilename);
        loadRating(ratingFilename);
    }

    /**
     * Returns every Movie as a Map
     *
     * @return Map of every Movies
     */
    @Override
    public Map<Integer, Movie> getAllMovies(){
        return movies;
    }

    /**
     * Search Movies by the title
     *
     * @param title      Keyword String
     * @param exactMatch Exactly matches or not
     *
     * @return the List of Movies which fulfills the criteria
     */
    @Override
    public List<Movie> searchByTitle(String title, boolean exactMatch){
        List<Movie> result = new ArrayList<>();
        if(exactMatch){
            for(int movieID : movies.keySet()){
                if(title.toLowerCase().equals(movies.get(movieID).getTitle().toLowerCase())){
                    result.add(movies.get(movieID));
                }
            }
        }else{
            for(int movieID : movies.keySet()){
                if(movies.get(movieID).getTitle().toLowerCase().contains(title.toLowerCase())){
                    result.add(movies.get(movieID));
                }
            }
        }
        return result;
    }

    /**
     * Search Movies by the tag
     *
     * @param tag Keyword String
     *
     * @return the List of Movies which fulfills the criteria
     */
    @Override
    public List<Movie> searchByTag(String tag){
        List<Movie> result = new ArrayList<>();
        for(int movieID : movies.keySet()){
            if(movies.get(movieID).getTags().contains(tag)){
                result.add(movies.get(movieID));
            }
        }
        return result;
    }

    /**
     * Search Movies by using year input
     *
     * @param year Year of released
     *
     * @return the List of Movies which fulfills the criteria
     */
    @Override
    public List<Movie> searchByYear(int year){
        List<Movie> result = new ArrayList<>();
        for(int movieID : movies.keySet()){
            if(movies.get(movieID).getYear() == year){
                result.add(movies.get(movieID));
            }
        }
        return result;
    }

    /**
     * Search movies by given at least one field (title, tag, year)
     *
     * @param title Keyword String
     * @param tag   Keyword Tag String
     * @param year  Year of released
     *
     * @return the List of Movies which fulfills the criteria
     */
    @Override
    public List<Movie> advanceSearch(String title, String tag, int year){
        List<Movie> result = new ArrayList<>();
        boolean tagIsOk;
        boolean yearIsOk = false;
        for(int movieID : movies.keySet()){
            if(title != null){
                boolean titleIsOk = movies.get(movieID).getTitle().toLowerCase().contains(title.toLowerCase());
                if(tag != null){
                    tagIsOk = movies.get(movieID).getTags().contains(tag);
                    if(year != -1){
                        yearIsOk = movies.get(movieID).getYear() == year;
                        if(titleIsOk && tagIsOk && yearIsOk){
                            result.add(movies.get(movieID));
                        }
                    }else{
                        if(titleIsOk && tagIsOk){
                            result.add(movies.get(movieID));
                        }
                    }
                }else{
                    if(year != -1){
                        yearIsOk = movies.get(movieID).getYear() == year;
                        if(titleIsOk && yearIsOk){
                            result.add(movies.get(movieID));
                        }
                    }else{
                        if(titleIsOk){
                            result.add(movies.get(movieID));
                        }
                    }
                }
            }else{
                if(tag != null){
                    tagIsOk = movies.get(movieID).getTags().contains(tag);
                    if(year != -1){
                        yearIsOk = movies.get(movieID).getYear() == year;
                        if(tagIsOk && yearIsOk){
                            result.add(movies.get(movieID));
                        }
                    }else{
                        if(tagIsOk){
                            result.add(movies.get(movieID));
                        }
                    }
                }else{
                    if(year != -1){
                        if(yearIsOk){
                            result.add(movies.get(movieID));
                        }
                    }else{
                        continue;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param unsortedMovies
     * @param asc
     *
     * @return
     */
    @Override
    public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            unsortedMovies.sort(new Comparator<Movie>(){
                @Override
                public int compare(Movie c1, Movie c2){
                    return c1.getTitle().compareToIgnoreCase(c2.getTitle());
                }
            });
        }else{
            for(int i = 0; i < unsortedMovies.size() - 1; i++){     //Bubble sort String Array Alphabetically
                for(int j = 1; j < unsortedMovies.size() - i; j++){
                    if(unsortedMovies.get(j).getTitle().compareToIgnoreCase(unsortedMovies.get(j - 1).getTitle()) < 0){
                        Collections.swap(unsortedMovies, j, j - 1);
                    }
                }
            }
        }
        if(!asc){
            Collections.reverse(unsortedMovies);
        }
        return unsortedMovies;
    }

    /**
     * Sort Given List of Movies Numerically by their Average Rating
     *
     * @param unsortedMovies List of target Movies
     * @param asc            Boolean for Sorting in Ascending Order. If it is false, it means Sorting in Descending Order.
     *
     * @return Alphabetically Sorted List
     */
    @Override
    public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc){
        for(int i = 0; i < unsortedMovies.size(); i++){
            for(int j = i + 1; j < unsortedMovies.size(); j++){
                if(asc && unsortedMovies.get(i).getMeanRating() > unsortedMovies.get(j).getMeanRating()){
                    Collections.swap(unsortedMovies, i, j);
                }
                if(!asc && unsortedMovies.get(i).getMeanRating() < unsortedMovies.get(j).getMeanRating()){
                    Collections.swap(unsortedMovies, i, j);
                }
            }
        }
        return unsortedMovies;
    }

}
