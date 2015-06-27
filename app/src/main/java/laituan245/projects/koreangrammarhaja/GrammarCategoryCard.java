package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Tuan Lai on 6/20/2015.
 */
public class GrammarCategoryCard extends Card {
    protected String title;
    protected int nbOfGrammars;
    protected ArrayList<String> grammarTitles;

    public GrammarCategoryCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init ();
    }

    public GrammarCategoryCard(Context context, int innerLayout, String category_title, ArrayList<GrammarRecord> allGrammars) {
        super(context, innerLayout);
        init ();
        this.title = category_title;
        nbOfGrammars = 0;
        for (int i = 0; i < allGrammars.size(); i++)
            if (allGrammars.get(i).getCategory().equals(this.title)) {
                nbOfGrammars++;
                grammarTitles.add(allGrammars.get(i).getLabel());
            }
    }

    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {

            }
        });
    }

    @Override
    public String getTitle() {
        return title;
    }

    public int getNbOfGrammars() {
        return nbOfGrammars;
    }

    public ArrayList<String> getGrammarTitles() {
        return grammarTitles;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setNbOfGrammars(int nbOfGrammars) {
        this.nbOfGrammars = nbOfGrammars;
    }

    public void setGrammarTitles(ArrayList<String> grammarTitles) {
        this.grammarTitles = grammarTitles;
    }

    public void setupInnerViewElements(ViewGroup parent, View view) {


    }

}
