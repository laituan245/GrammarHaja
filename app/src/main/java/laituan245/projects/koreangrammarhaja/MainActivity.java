package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends ActionBarActivity implements GrammarLabelsFragment.OnGrammarLabelSelectedListener, OnQueryTextListener{

        private static final String[] GRAMMAR_CATEGORY =
                {"Intro to the Korean Language",
                        "Tenses",
                        "Negative Expressions",
                        "Particles",
                        "Listing and Contrast",
                        "Time Expressions",
                        "Ability and Possibility",
                        "Demands, Obligations, Permission / Prohibition",
                        "Expressions of Hope",
                        "Reasons and Causes",
                        "Making Requests and Assisting",
                        "Trying New Things and Experiences",
                        "Asking Opinions and Making Suggestions",
                        "Intentions and Plans",
                        "Background Information and Explanations",
                        "Purpose and Intention",
                        "Conditions and Suppositions",
                        "Conjecture",
                        "Changes in Parts of Speech",
                        //"Expressions of State",
                        "Confirming Information",
                        "Discovery and Surprise",
                        "Quotations",
                        "Other Miscellaneous"};

        public static ArrayList<GrammarRecord> allGrammarArray;
        public static ArrayList<ConfusingGrammarArticle> allArticleArray;
        private static final String SELECTED_TAB_POS_KEY = "SELECTED_TAB_POS_KEY";
        private static final String SEARCH_MENU_ITEM_EXPANDING_KEY = "SEARCH_MENU_ITEM_EXPANDING_KEY";
        private static final String SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY";
        private static final String CATEGORY_OR_LIST_KEY = "CATEGORY_OR_LIST_KEY";
        private static final String DO_NOT_SHOW_TIPS_KEY = "DO_NOT_SHOW_TIPS_KEY";
        private static final String DISMISS_DIALOG_KEY = "DISMISS_DIALOG_KEY";
        private CardArrayAdapter mCardArrayAdapter;
        private Context mContext;
        private SearchView mSearchView;
        private Menu mMenu;
        private MenuItem searchMenuItem;
        private boolean saved_searchMenuItem_expanded = false;
        private String saved_query_string = "";
        private String category_or_list = "Category View";
        private boolean dismissedDialog = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            dismissedDialog = false;
            saved_searchMenuItem_expanded = false;
            saved_query_string = "";
            category_or_list = "Category View";
            if (savedInstanceState != null) {
                if (savedInstanceState.getBoolean(SEARCH_MENU_ITEM_EXPANDING_KEY))
                    saved_searchMenuItem_expanded = true;
                saved_query_string = savedInstanceState.getString(SEARCH_TEXT_KEY);
                if (savedInstanceState.getString(CATEGORY_OR_LIST_KEY).length() > 0)
                    category_or_list = savedInstanceState.getString(CATEGORY_OR_LIST_KEY);
                dismissedDialog = savedInstanceState.getBoolean(DISMISS_DIALOG_KEY, false);
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
            cardListView.setScrollbarFadingEnabled(false);
            if (cardListView!=null){
                cardListView.setAdapter(mCardArrayAdapter);
            }
            for (int i = 0; i < allArticleArray.size(); i++)
            {
                ConfusingGrammarCard card = new ConfusingGrammarCard(this, R.layout.card_inner_layout);
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
                if (category_or_list.toLowerCase().contains("category") == false) {
                    (findViewById(R.id.container1_listview)).setVisibility(View.GONE);
                    (findViewById(R.id.container1_categoryview)).setVisibility(View.VISIBLE);
                }
                else {
                    (findViewById(R.id.container1_listview)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.container1_categoryview)).setVisibility(View.GONE);
                }
            }
            else if (actionBar.getSelectedTab().getPosition() == 1) {  // Confusions tab selected
                (findViewById(R.id.container1)).setVisibility(View.INVISIBLE);
                (findViewById(R.id.container2)).setVisibility(View.VISIBLE);
            }


            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (savedInstanceState == null || InformationFragment.mCurrentPosition == -1 || (category_or_list.toLowerCase().contains("category")) == false) {
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

            // Set up "EvenColumn" (For Category View)
            CardListView EvenColumn = (CardListView) findViewById(R.id.EvenColumn);
            EvenColumn.setScrollbarFadingEnabled(false);
            ArrayList<Card> EvenCards = new ArrayList<Card>();

            CardArrayAdapter EvenCardArrayAdapter = new CardArrayAdapter(this,EvenCards);

            if (EvenColumn!=null){
                EvenColumn.setAdapter(EvenCardArrayAdapter);
            }

            for (int i = 0; i < GRAMMAR_CATEGORY.length; i++)
            {
                GrammarCategoryCard card = new GrammarCategoryCard(this, i, GRAMMAR_CATEGORY[i], allGrammarArray);
                card.init();
                EvenCardArrayAdapter.add(card);
            }

            //
            SharedPreferences prefs = this.getSharedPreferences("laituan245.projects.koreangrammarhaja", Context.MODE_PRIVATE);
            boolean doNotShowTips = prefs.getBoolean(DO_NOT_SHOW_TIPS_KEY, false);
            if ((dismissedDialog == false || savedInstanceState == null) && doNotShowTips == false) {
                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("Tips").customView(R.layout.tip_dialog_layout, wrapInScrollView)
                        .icon(getResources().getDrawable(R.drawable.ic_launcher))
                        .positiveText("Got it. Thanks")
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dismissedDialog = true;
                                MaterialDialog tempDialog = (MaterialDialog) dialog;
                                View v = tempDialog.getCustomView();
                                boolean isChecked = ((CheckBox) v.findViewById(R.id.chkdonotshowagain)).isChecked();
                                SharedPreferences prefs = ((MaterialDialog) dialog).getContext().getSharedPreferences("laituan245.projects.koreangrammarhaja", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean(DO_NOT_SHOW_TIPS_KEY, isChecked);
                                editor.apply();
                            }
                        }).build();
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();
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

            String tempString = category_or_list;
            if (mMenu != null && mMenu.findItem(R.id.change_view_style) != null)
                tempString = mMenu.findItem(R.id.change_view_style).getTitle().toString();
            outState.putString(CATEGORY_OR_LIST_KEY, tempString);
            outState.putBoolean(DISMISS_DIALOG_KEY, dismissedDialog);
            outState.putString ("TEMP_STRING_KEY", "TEMP STRING");
        }

        private void ShowSomeMenuItems () {
            searchMenuItem.setVisible(true);
            mMenu.findItem(R.id.change_view_style).setVisible(true);
            if (category_or_list.toLowerCase().contains("category") == false)
                searchMenuItem.setVisible(false);
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
                        if (category_or_list.toLowerCase().contains("category") == false) {
                            (findViewById(R.id.container1_listview)).setVisibility(View.GONE);
                            (findViewById(R.id.container1_categoryview)).setVisibility(View.VISIBLE);
                        }
                        else {
                            (findViewById(R.id.container1_listview)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.container1_categoryview)).setVisibility(View.GONE);
                        }

                        // Update the menu item action_search (which contains the searchview) appropriately
                        if (searchMenuItem != null)
                            ShowSomeMenuItems();

                        // Set up "EvenColumn" (For Category View)
                        CardListView EvenColumn = (CardListView) findViewById(R.id.EvenColumn);
                        ArrayList<Card> EvenCards = new ArrayList<Card>();
                        CardArrayAdapter EvenCardArrayAdapter = new CardArrayAdapter(mContext,EvenCards);
                        EvenColumn.setScrollbarFadingEnabled(false);
                        if (EvenColumn!=null){
                            EvenColumn.setAdapter(EvenCardArrayAdapter);
                        }

                        for (int i = 0; i < GRAMMAR_CATEGORY.length; i++)
                        {
                            GrammarCategoryCard card = new GrammarCategoryCard(mContext, i,  GRAMMAR_CATEGORY[i], allGrammarArray);
                            card.init();
                            EvenCardArrayAdapter.add(card);
                        }
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
                        cardListView.setScrollbarFadingEnabled(false);
                        if (cardListView!=null){
                            cardListView.setAdapter(mCardArrayAdapter);
                        }
                        for (int i = 0; i < allArticleArray.size(); i++)
                        {
                            ConfusingGrammarCard card = new ConfusingGrammarCard(mContext, R.layout.card_inner_layout);
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
                    (mMenu.findItem(R.id.action_learn_more)).setVisible(false);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    (mMenu.findItem(R.id.change_view_style)).setVisible(true);
                    (mMenu.findItem(R.id.action_rate)).setVisible(true);
                    (mMenu.findItem(R.id.action_share)).setVisible(true);
                    (mMenu.findItem(R.id.action_learn_more)).setVisible(true);
                    saved_query_string = mSearchView.getQuery().toString();
                    return true;
                }
            });

            //
            MenuItem tempItem = mMenu.findItem(R.id.change_view_style);
            if (category_or_list.toLowerCase().contains("category") == false) {
                tempItem.setTitle(getResources().getString(R.string.list_view));
                tempItem.setIcon(getResources().getDrawable(R.drawable.ic_action_list_view));
                searchItem.setVisible(false);
            }
            else {
                tempItem.setTitle(getResources().getString(R.string.category_view));
                tempItem.setIcon(getResources().getDrawable(R.drawable.ic_action_dashboard_view));
            }
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
                case R.id.change_view_style:
                    if (searchMenuItem.isActionViewExpanded())
                        searchMenuItem.collapseActionView();

                    if (item.getTitle().toString().contains("Category")) {
                        item.setTitle(getResources().getString(R.string.list_view));
                        item.setIcon(getResources().getDrawable(R.drawable.ic_action_list_view));
                        (findViewById(R.id.container1_listview)).setVisibility(View.GONE);
                        (findViewById(R.id.container1_categoryview)).setVisibility(View.VISIBLE);
                        category_or_list = getResources().getString(R.string.list_view);
                        searchMenuItem.setVisible(false);
                    }
                    else {
                        item.setTitle(getResources().getString(R.string.category_view));
                        item.setIcon(getResources().getDrawable(R.drawable.ic_action_dashboard_view));
                        (findViewById(R.id.container1_listview)).setVisibility(View.VISIBLE);
                        (findViewById(R.id.container1_categoryview)).setVisibility(View.GONE);
                        category_or_list = getResources().getString(R.string.category_view);
                        searchMenuItem.setVisible(true);
                    }

                    return true;
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
                case R.id.action_learn_more:
                    Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/koreanhaja"));
                    startActivity(fbIntent);
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