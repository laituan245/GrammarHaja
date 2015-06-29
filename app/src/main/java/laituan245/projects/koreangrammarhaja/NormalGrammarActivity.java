package laituan245.projects.koreangrammarhaja;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class NormalGrammarActivity extends ActionBarActivity {
    public static final String GRAMMAR_TITLE_KEY = "GRAMMAR_TITLE_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.information_view);

        // Configuring the action bar
        android.support.v7.app.ActionBar actionBar =  getSupportActionBar() ;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66CCFF")));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        setTitle("      ");
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get data from the intent
        String grammarTitle = this.getIntent().getExtras().getString(GRAMMAR_TITLE_KEY);
        Log.d("TAG", grammarTitle);
        TextView mTextView = ((TextView) (findViewById(R.id.information)));
        String informationStr = "";
        for (int i = 0; i < MainActivity.allGrammarArray.size(); i++)
            if (MainActivity.allGrammarArray.get(i).getLabel().equals(grammarTitle)) {
                informationStr = MainActivity.allGrammarArray.get(i).getInformation();
                break;
            }

        // Set the text size appropriately
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            mTextView.setTextSize(16);
        }

        mTextView.setText(Html.fromHtml(preProcessingGrammarInfo(informationStr, grammarTitle)));


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

    private String preProcessingGrammarInfo (String text, String title) {
        text = "<h3>" + title + "</h3><br/>" + text;
        text = text.replaceAll("  ", "&nbsp;&nbsp;");
        text = text.replaceAll("\n", "<br/>");
        text = text.replaceAll ("-->", "&#8594;");
        text = text.replaceAll ("<usage>", "<b>");
        text = text.replaceAll ("</usage>", "</b>");
        text = text.replaceAll ("<example>", "<b><i>");
        text = text.replaceAll ("</example>", "</i></b>");
        return text;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

