package tanawinwichitcom.android.mooglemobile.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import tanawinwichitcom.android.mooglemobile.R;


/**
 * Created by tanaw on 3/22/2018.
 */

public class SearchFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private boolean wantSort = false, wantAscendingOrder = true, wantTitle = true, wantTag = true, wantYear = true, wantRatings = true;
    private int sortType;
    private final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);

        Toolbar toolbar = findViewById(R.id.searchFilterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        //getSupportActionBar()

        /* Disables Radio buttons and the Spinner when the Activity starts */
        findViewById(R.id.radio_ascend).setEnabled(false);
        findViewById(R.id.radio_descend).setEnabled(false);
        findViewById(R.id.sortingSpinner).setEnabled(false);
        findViewById(R.id.radio_ascend).setClickable(false);
        findViewById(R.id.radio_descend).setClickable(false);

        Spinner spinner = findViewById(R.id.sortingSpinner);
        spinner.setOnItemSelectedListener(this);        /* Sets what method should be called when selecting an item in the spinner */
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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
            public boolean onQueryTextSubmit(String query){     /* When enter the text and press search or enter */
                if(!wantTitle && !wantTag && !wantYear && !wantRatings){
                    Toast.makeText(context, "Invalid Search Option!", Toast.LENGTH_LONG).show();
                    return true;
                }
                if(query.isEmpty()){
                    Toast.makeText(context, "Invalid Search String!", Toast.LENGTH_LONG).show();
                    return true;
                }
                SearchResultActivity.setSearchOptions(wantSort, wantAscendingOrder, wantTitle, wantTag, wantYear, wantRatings, sortType);       /* Sends Search option to SearchResultActivity */
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
                onBackPressed();        /* When clicking back arrow, it will return to the previous activity */
                return false;
            }
        });


        return true;
    }

    public void onRadioButtonClicked(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()){
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

    public void onCheckedChanged(View view){
        boolean checked = ((Switch) view).isChecked();
        if(checked){        /* If the sorting toggle is checked */
            wantSort = true;
            wantAscendingOrder = true;
            findViewById(R.id.radio_ascend).setEnabled(true);
            findViewById(R.id.radio_descend).setEnabled(true);
            findViewById(R.id.sortingSpinner).setEnabled(true);

            findViewById(R.id.radio_ascend).setClickable(true);
            findViewById(R.id.radio_descend).setClickable(true);
            findViewById(R.id.sortingSpinner).setClickable(true);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){      /* Disables every sorting except alphabetical on Android Device with OS version lower than 7.0 */
                findViewById(R.id.sortingSpinner).setEnabled(false);
                findViewById(R.id.sortingSpinner).setClickable(false);
            }
        }else{
            findViewById(R.id.radio_ascend).setEnabled(false);
            findViewById(R.id.radio_descend).setEnabled(false);
            findViewById(R.id.sortingSpinner).setEnabled(false);

            findViewById(R.id.radio_ascend).setClickable(false);
            findViewById(R.id.radio_descend).setClickable(false);
            findViewById(R.id.sortingSpinner).setClickable(false);
            wantSort = false;
            wantAscendingOrder = false;
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
                }else{
                    wantTitle = false;
                }
                break;
            case R.id.checkbox_tag:
                if(checked){
                    wantTag = true;
                }else{
                    wantTag = false;
                }
                break;
            case R.id.checkbox_year:
                if(checked){
                    wantYear = true;
                }else{
                    wantYear = false;
                }
                break;
            case R.id.checkbox_ratings:
                if(checked){
                    wantRatings = true;
                }else{
                    wantRatings = false;
                }
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        sortType = -1;
        switch(position){
            case 0:     //Sort by Alphabet
                sortType = 0;
                break;
            case 1:     //Sort by Average Score
                sortType = 1;
                break;
            case 2:     //Sort by Year
                sortType = 2;
                break;
            case 3:     //Sort by Number of Ratings
                sortType = 3;
                break;
            case 4:     //Sort by Number of Tags
                sortType = 4;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }
}
