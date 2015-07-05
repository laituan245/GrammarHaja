package laituan245.projects.koreangrammarhaja;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;
import java.lang.reflect.Field;

/**
 * Created by Tuan Lai on 6/21/2015.
 */
public class ConfusingGrammarActivity extends ActionBarActivity {
    public static String CONFUSING_GRAMMAR_TITLE_KEY = "CONFUSING_GRAMMAR_TITLE_KEY";
    public static String CONFUSING_GRAMMAR_CONTENT_KEY = "CONFUSING_GRAMMAR_CONTENT_KEY";
    private String title = "";
    private String content = "";

    private String HTMLPreprocessing (String text, String title) {
        text = "<h3>" + title + "</h3><br/>" + text;
        text = text.replaceAll("  ", "&nbsp;&nbsp;");
        text = text.replaceAll("\n", "<br/>");
        text = text.replaceAll("-->", "&#8594;");
        text = text.replaceAll("<usage>", "<b>");
        text = text.replaceAll ("</usage>", "</b>");
        text = text.replaceAll ("<example>", "<b><i>");
        text = text.replaceAll ("</example>", "</i></b>");

        return text;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confusing_grammar_activity_layout);

        // Configuring the action bar
        android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66CCFF")));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        setTitle("      ");
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get data from the intent
        title = this.getIntent().getExtras().getString(CONFUSING_GRAMMAR_TITLE_KEY);
        content = this.getIntent().getExtras().getString(CONFUSING_GRAMMAR_CONTENT_KEY);
        final TextView mTextView = ((TextView) (findViewById(R.id.content_textview)));
        mTextView.setText(Html.fromHtml(HTMLPreprocessing(content, title)));
        // Set the text size appropriately
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            mTextView.setTextSize(16);
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            mTextView.setTextSize(16);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        (menu.findItem(R.id.action_search)).setVisible(false);
        (menu.findItem(R.id.change_view_style)).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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


}
