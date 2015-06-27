package laituan245.projects.koreangrammarhaja;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;

/**
 * Created by Tuan Lai on 6/20/2015.
 */
public class GrammarCategoryCard extends CardWithList {
    protected String title;
    protected int nbOfGrammars;
    protected ArrayList<String> grammarTitles;

    public GrammarCategoryCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public GrammarCategoryCard(Context context, int innerLayout, String category_title, ArrayList<GrammarRecord> allGrammars) {
        super(context, innerLayout);
        this.title = category_title;
        nbOfGrammars = 0;
        for (int i = 0; i < allGrammars.size(); i++)
            if (allGrammars.get(i).getCategory().equals(this.title)) {
                nbOfGrammars++;
                grammarTitles.add(allGrammars.get(i).getLabel());
            }
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext());

        return header;
    }

    @Override
    protected void initCard() {

    }


    public void setupInnerViewElements(ViewGroup parent, View view) {

    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {
        View emptyView = new View(getContext());
        return emptyView;
    }

    @Override
    protected List<ListObject> initChildren() {

        List<ListObject> mObjects = new ArrayList<ListObject>();



        return mObjects;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_home;
    }

    // Setters and Getters
    public String getTitle() {
        return title;
    }

    public int getNbOfGrammars() {
        return nbOfGrammars;
    }

    public ArrayList<String> getGrammarTitles() {
        return grammarTitles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNbOfGrammars(int nbOfGrammars) {
        this.nbOfGrammars = nbOfGrammars;
    }

    public void setGrammarTitles(ArrayList<String> grammarTitles) {
        this.grammarTitles = grammarTitles;
    }

}
