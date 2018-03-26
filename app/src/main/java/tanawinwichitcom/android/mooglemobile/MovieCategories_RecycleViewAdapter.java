package tanawinwichitcom.android.mooglemobile;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by tanaw on 3/21/2018.
 */

public class MovieCategories_RecycleViewAdapter extends RecyclerView.Adapter<MovieCategories_RecycleViewAdapter.ViewHolder>{
    private final ArrayList<String> tagArrayList;
    private final Context context;

    private CardView cardView;
    private TextView button;

    public MovieCategories_RecycleViewAdapter(Context context, Set<String> tagsSet){
        tagArrayList = new ArrayList<>();
        tagArrayList.addAll(tagsSet);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View rootView = LayoutInflater.from(context).inflate(R.layout.tagscard_layout, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String tagsEntry = tagArrayList.get(position);
        if(tagsEntry != null){
            button.setText(tagsEntry);
            String tagLowerCase = tagsEntry.toLowerCase();
            if(tagLowerCase.equals("animation") || tagLowerCase.equals("comedy") || tagLowerCase.equals("sci-fi")){
                button.setTextColor(ContextCompat.getColor(context, R.color.black));
            }else{
                button.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, getColorForTag(tagsEntry)));
        }
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public int getItemCount(){
        return tagArrayList.size();
    }

    private int getColorForTag(String tag){
        int colorCode;
        switch(tag.toLowerCase()){
            case "action":
                colorCode = R.color.action;
                break;
            case "adventure":
                colorCode = R.color.adventure;
                break;
            case "animation":
                colorCode = R.color.animation;
                break;
            case "children":
                colorCode = R.color.children;
                break;
            case "comedy":
                colorCode = R.color.comedy;
                break;
            case "crime":
                colorCode = R.color.crime;
                break;
            case "documentary":
                colorCode = R.color.documentary;
                break;
            case "drama":
                colorCode = R.color.drama;
                break;
            case "fantasy":
                colorCode = R.color.fantasy;
                break;
            case "filmnoir":
                colorCode = R.color.filmnoir;
                break;
            case "horror":
                colorCode = R.color.horror;
                break;
            case "imax":
                colorCode = R.color.imax;
                break;
            case "musical":
                colorCode = R.color.musical;
                break;
            case "mystery":
                colorCode = R.color.mystery;
                break;
            case "romance":
                colorCode = R.color.romance;
                break;
            case "sci-fi":
                colorCode = R.color.scifi;
                break;
            case "thriller":
                colorCode = R.color.thriller;
                break;
            case "war":
                colorCode = R.color.war;
                break;
            case "western":
                colorCode = R.color.western;
                break;
            default:
                colorCode = R.color.undefined;
        }
        return colorCode;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView){
            super(itemView);
            button = itemView.findViewById(R.id.tagTitle);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
