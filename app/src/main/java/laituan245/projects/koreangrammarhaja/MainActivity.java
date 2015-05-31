package laituan245.projects.koreangrammarhaja;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements GrammarLabelsFragment.OnGrammarLabelSelectedListener {

    public static ArrayList<GrammarRecord> allGrammarArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadTheDatabase();

        setContentView(R.layout.activity_home);

        android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66CCFF")));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        setTitle("      ");
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        establishTabs();

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (savedInstanceState == null || InformationFragment.mCurrentPosition == -1) {
                (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
            }
            else {
                (findViewById(R.id.information_fragment)).setVisibility(View.VISIBLE);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            }
        }

        // Force the overflow button to appear on the action bar
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    private void establishTabs () {
        android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab

            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 2 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText("Home").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("History").setTabListener(tabListener));
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_rate:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=laituan245.projects.koreangrammarhaja"));
                startActivity(browserIntent);
                return true;
            case R.id.action_share:
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Korean Haja");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "This is an awesome app for learning Korean. https://play.google.com/store/apps/details?id=laituan245.projects.koreangrammarhaja");
                startActivity(Intent.createChooser(i,"Share via"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onGrammarSelected(int position) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            (findViewById(R.id.information_fragment)).setVisibility(View.VISIBLE);
            android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }

        InformationFragment grammarInfoFrag = (InformationFragment)
                getSupportFragmentManager().findFragmentById(R.id.information_fragment);
        grammarInfoFrag.updateInformationView(position);
    }

    @Override
    public void onBackPressed() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && InformationFragment.mCurrentPosition != -1) {
            (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
            InformationFragment.mCurrentPosition = -1;
            getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        else
            finish();
    }

    private void loadTheDatabase() {
        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        }
        catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
            allGrammarArray = myDbHelper.getAllElements(true);
        }
        catch(SQLException sqle){
            throw sqle;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && InformationFragment.mCurrentPosition == -1)
            (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
    }



}