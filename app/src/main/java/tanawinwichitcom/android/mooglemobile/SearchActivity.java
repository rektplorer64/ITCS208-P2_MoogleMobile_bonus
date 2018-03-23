package tanawinwichitcom.android.mooglemobile;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

/**
 * Created by tanaw on 3/22/2018.
 */

public class SearchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItem searchItemMenu = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchItemMenu.getActionView();


        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultActivity.class)));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(false);
        searchItemMenu.expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                SearchResultActivity.setSearchOptions(wantSort, wantAscendingOrder, wantTitle, wantTag, wantYear, wantRatings);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                return false;
            }
        });

        searchItemMenu.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(MenuItem item){
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
                finish();
                return false;
            }
        });

        return true;
    }

    private boolean wantSort, wantAscendingOrder, wantTitle, wantTag, wantYear, wantRatings;

    public void onRadioButtonClicked(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()){
            case R.id.radio_none:
                if(checked){
                    wantSort = false;
                }
                break;
            case R.id.radio_ascend:
                if(checked){
                    wantSort = true;
                    wantAscendingOrder = true;
                }
                break;
            case R.id.radio_descend:
                if(checked){
                    wantSort = true;
                    wantAscendingOrder = false;
                }
                break;
        }
    }

    public void onCheckboxClicked(View view){
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()){
            case R.id.checkbox_title:
                if(checked){
                    wantTitle = true;
                }
                break;
            case R.id.checkbox_tag:
                if(checked){
                    wantTag = true;
                }
                break;
            case R.id.checkbox_year:
                if(checked){
                    wantYear = true;
                }
                break;
            case R.id.checkbox_ratings:
                if(checked){
                    wantRatings = true;
                }
                break;
        }
    }
}
