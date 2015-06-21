package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.Intent;
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
                Intent intent = new Intent(card.getContext(), ConfusingGrammarActivity.class);
                card.getContext().startActivity(intent);
            }
        });
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

        mTitle.setText(title);
        mShortDescription.setText(shortDescription);

    }

}
