package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Tuan Lai on 6/20/2015.
 */
public class CustomCard extends Card {
    protected String title;
    protected String shortDescription = "Welcome";
    protected String content;

    public CustomCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init ();
    }

    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Log.d("TAG", MainActivity.saved_query_string + " Inside CustomCard.java");
                Intent intent = new Intent(card.getContext(), ConfusingGrammarActivity.class);
                intent.putExtra(ConfusingGrammarActivity.CONFUSING_GRAMMAR_CONTENT_KEY, content);
                intent.putExtra(ConfusingGrammarActivity.CONFUSING_GRAMMAR_TITLE_KEY, title);
                card.getContext().startActivity(intent);
            }
        });
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView mTitle = (TextView) parent.findViewById(R.id.card_inner_simple_title);
        TextView mShortDescription = (TextView) parent.findViewById(R.id.card_inner_short_description);

        // Set the text size appropriately
        if ((parent.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            mShortDescription.setTextSize(15);
        }

        mTitle.setText(title);
        mShortDescription.setText(shortDescription);

    }

}
