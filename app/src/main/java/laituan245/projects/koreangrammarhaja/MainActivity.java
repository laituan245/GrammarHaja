package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;

import com.startapp.android.publish.StartAppSDK;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends ActionBarActivity implements GrammarLabelsFragment.OnGrammarLabelSelectedListener, OnQueryTextListener{

        public static ArrayList<GrammarRecord> allGrammarArray;
        public static ArrayList<ConfusingGrammarArticle> allArticleArray;
        private static final String SELECTED_TAB_POS_KEY = "SELECTED_TAB_POS_KEY";
        private static final String SEARCH_MENU_ITEM_EXPANDING_KEY = "SEARCH_MENU_ITEM_EXPANDING_KEY";
        private static final String SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY";
        private CardArrayAdapter mCardArrayAdapter;
        private Context mContext;
        private SearchView mSearchView;
        private Menu mMenu;
        private MenuItem searchMenuItem;
        private boolean saved_searchMenuItem_expanded = false;
        public static String saved_query_string = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            saved_searchMenuItem_expanded = false;
            saved_query_string = "";
            if (savedInstanceState != null) {
                if (savedInstanceState.getBoolean(SEARCH_MENU_ITEM_EXPANDING_KEY))
                    saved_searchMenuItem_expanded = true;
                saved_query_string = savedInstanceState.getString(SEARCH_TEXT_KEY);
            }

            super.onCreate(savedInstanceState);

            loadTheDatabase();

            setContentView(R.layout.activity_home);

            mContext = this;


            // Configuring the action bar
            android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66CCFF")));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            setTitle("      ");
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

            // Setting up the tabs
            establishTabs();

            // Setting up CardList
            ArrayList<Card> cards = new ArrayList<Card>();
            mCardArrayAdapter = new CardArrayAdapter(this,cards);
            CardListView cardListView = (CardListView) findViewById(R.id.myCardList);
            if (cardListView!=null){
                cardListView.setAdapter(mCardArrayAdapter);
            }
            for (int i = 0; i < allArticleArray.size(); i++)
            {
                CustomCard card = new CustomCard(this, R.layout.card_inner_layout);
                card.setTitle(allArticleArray.get(i).getTitle());
                card.setShortDescription(allArticleArray.get(i).getShort_description());
                card.setContent(allArticleArray.get(i).getContent());
                mCardArrayAdapter.add(card);
            }

            // Setting visibility of various components
            if (savedInstanceState != null)
                actionBar.selectTab(actionBar.getTabAt(savedInstanceState.getInt(SELECTED_TAB_POS_KEY,0)));
            if (actionBar.getSelectedTab().getPosition() == 0) {       // Home tab selected
                (findViewById(R.id.container1)).setVisibility(View.VISIBLE);
                (findViewById(R.id.container2)).setVisibility(View.INVISIBLE);
            }
            else if (actionBar.getSelectedTab().getPosition() == 1) {  // Confusions tab selected
                (findViewById(R.id.container1)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.container2)).setVisibility(View.VISIBLE);
            }


            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (savedInstanceState == null || InformationFragment.mCurrentPosition == -1) {
                    (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
                }
                else {
                    int tempInt = savedInstanceState.getInt(SELECTED_TAB_POS_KEY, 0);
                    if (tempInt == 0) {
                        (findViewById(R.id.information_fragment)).setVisibility(View.VISIBLE);
                        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    }
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

        public boolean onQueryTextChange(String newText) {
            GrammarLabelsFragment grammarLabelsFrag = (GrammarLabelsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.article_fragment);
            grammarLabelsFrag.filterText(newText);
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            int savedInt = 0;
            if ((findViewById(R.id.container2)).getVisibility() == View.VISIBLE)
                savedInt = 1;
            outState.putInt(SELECTED_TAB_POS_KEY, savedInt);
            if (searchMenuItem != null)
                outState.putBoolean(SEARCH_MENU_ITEM_EXPANDING_KEY, searchMenuItem.isActionViewExpanded());
            if (mSearchView != null && mSearchView.getQuery().toString().length() != 0)
                saved_query_string = mSearchView.getQuery().toString();
            outState.putString(SEARCH_TEXT_KEY, saved_query_string);
        }

        private void ShowSomeMenuItems () {
            searchMenuItem.setVisible(true);
            mMenu.findItem(R.id.change_view_style).setVisible(true);
        }
        private void HideSomeMenuItems () {
            searchMenuItem.setVisible(false);
            mMenu.findItem(R.id.change_view_style).setVisible(false);
        }

        private void establishTabs () {
            android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;

            // Specify that tabs should be displayed in the action bar.
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            // Create a tab listener that is called when the user changes tabs.
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // show the given tab
                    if (tab.getPosition() == 0) {       // Home tab selected
                        (findViewById(R.id.container1)).setVisibility(View.VISIBLE);
                        (findViewById(R.id.container2)).setVisibility(View.INVISIBLE);

                        // Update the menu item action_search (which contains the searchview) appropriately
                        if (searchMenuItem != null)
                            ShowSomeMenuItems();
                    }
                    else if (tab.getPosition() == 1) {  // Confusions tab selected
                        // Update the searchview and the menu item action_search appropriately
                        if (searchMenuItem != null) {
                            if (searchMenuItem.isActionViewExpanded())
                                searchMenuItem.collapseActionView();
                            HideSomeMenuItems();
                        }
                        //

                        (findViewById(R.id.container1)).setVisibility(View.INVISIBLE);
                        (findViewById(R.id.container2)).setVisibility(View.VISIBLE);

                        // "Resetting" the content of tab "Home"
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
                        }
                        InformationFragment grammarInfoFrag = (InformationFragment)
                                getSupportFragmentManager().findFragmentById(R.id.information_fragment);
                        grammarInfoFrag.updateInformationView(-1);


                        // Setting up CardList
                        ArrayList<Card> cards = new ArrayList<Card>();
                        mCardArrayAdapter = new CardArrayAdapter(mContext,cards);
                        CardListView cardListView = (CardListView) findViewById(R.id.myCardList);
                        if (cardListView!=null){
                            cardListView.setAdapter(mCardArrayAdapter);
                        }
                        for (int i = 0; i < allArticleArray.size(); i++)
                        {
                            CustomCard card = new CustomCard(mContext, R.layout.card_inner_layout);
                            card.setTitle(allArticleArray.get(i).getTitle());
                            card.setShortDescription(allArticleArray.get(i).getShort_description());
                            card.setContent(allArticleArray.get(i).getContent());
                            mCardArrayAdapter.add(card);
                        }
                    }
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
            actionBar.addTab(actionBar.newTab().setText("  Confusing Grammar  ").setTabListener(tabListener));

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            // Inflate the menu items for use in the action bar
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_activity_actions, menu);

            // Setting up the search view
            MenuItem searchItem = menu.findItem(R.id.action_search);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            if (mSearchView != null) {
                mSearchView.setOnQueryTextListener(this);
            }
            setupSearchView();

            this.mMenu = menu;
            this.searchMenuItem = searchItem;

            if (this.findViewById(R.id.container2).getVisibility() == View.VISIBLE)
                HideSomeMenuItems();

            boolean someCond = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && this.findViewById(R.id.information_fragment).getVisibility() == View.VISIBLE;
            if (someCond)
                HideSomeMenuItems();

            // Check if we should expand the search view and set the query appropriately
            if (!someCond && this.saved_searchMenuItem_expanded) {
                this.searchMenuItem.expandActionView();
                mSearchView.setQuery(this.saved_query_string,true);
            }

            // OnActionExpandListener
            MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener () {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    (mMenu.findItem(R.id.change_view_style)).setVisible(false);
                    (mMenu.findItem(R.id.action_rate)).setVisible(false);
                    (mMenu.findItem(R.id.action_share)).setVisible(false);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    (mMenu.findItem(R.id.change_view_style)).setVisible(true);
                    (mMenu.findItem(R.id.action_rate)).setVisible(true);
                    (mMenu.findItem(R.id.action_share)).setVisible(true);
                    saved_query_string = mSearchView.getQuery().toString();
                    return true;
                }
            });
            return true;
        }

        private void setupSearchView() {

            mSearchView.setIconifiedByDefault(false);
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setSubmitButtonEnabled(true);
            mSearchView.setQueryHint("Korean only");
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                mSearchView.setQueryHint("Enter Korean text only");
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                mSearchView.setQueryHint("Enter Korean text only");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle presses on the action bar items
            switch (item.getItemId()) {
                case R.id.action_search:
                    this.searchMenuItem.expandActionView();
                    mSearchView.setQuery(saved_query_string, true);
                    return true;
                case android.R.id.home:
                    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && InformationFragment.mCurrentPosition != -1) {
                        (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
                        InformationFragment.mCurrentPosition = -1;
                        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
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
            if (searchMenuItem.isActionViewExpanded())
                position = GrammarLabelsFragment.LabelMapping[position];
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                (findViewById(R.id.information_fragment)).setVisibility(View.VISIBLE);
                android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            InformationFragment grammarInfoFrag = (InformationFragment)
                    getSupportFragmentManager().findFragmentById(R.id.information_fragment);
            grammarInfoFrag.updateInformationView(position);

            ScrollView mScrollView = ((ScrollView) findViewById(R.id.textAreaScroller));
            mScrollView.scrollTo(0, mScrollView.getTop());

            if (searchMenuItem.isActionViewExpanded())
                searchMenuItem.collapseActionView();

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && this.findViewById(R.id.information_fragment).getVisibility() == View.VISIBLE)
                HideSomeMenuItems();
        }

        @Override
        public void onBackPressed() {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && InformationFragment.mCurrentPosition != -1) {
                (findViewById(R.id.information_fragment)).setVisibility(View.GONE);
                InformationFragment.mCurrentPosition = -1;
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else
                finish();
        }

        private void loadTheDatabase() {
            // Load the first table
            DbHelper_GrammarInfoTable myDbHelper = new DbHelper_GrammarInfoTable(this);
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

            // Load the second table
            DbHelper_ConfusingGrammarTable myDbHelper2 = new DbHelper_ConfusingGrammarTable(this);
            try {
                myDbHelper2.createDataBase();
            }
            catch (IOException ioe) {
                throw new Error("Unable to create database");
            }

            try {
                myDbHelper2.openDataBase();
                allArticleArray = myDbHelper2.getAllElements(true);
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

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                if ((findViewById(R.id.container1).getVisibility() == View.VISIBLE) && (findViewById(R.id.information_fragment).getVisibility() == View.VISIBLE))
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }



}