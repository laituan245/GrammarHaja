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
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * Created by Tuan Lai on 6/20/2015.
 */
public class GrammarCategoryCard extends CardWithList {

    public class GrammarObject extends DefaultListObject {
        protected String grammarTitle;

        public GrammarObject(Card parentCard,String grammar_title) {
            super(parentCard);
            grammarTitle = grammar_title;
            init();
        }

        private void init() {
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Intent intent = new Intent(view.getContext(), NormalGrammarActivity.class);
                    intent.putExtra(NormalGrammarActivity.GRAMMAR_TITLE_KEY, grammarTitle);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }
    protected String title;
    protected int unitNb;
    protected int nbOfGrammars;
    protected ArrayList<String> grammarTitles;

    public GrammarCategoryCard(Context context) {
        super(context);
    }

    public GrammarCategoryCard(Context context, int unitNb, String category_title, ArrayList<GrammarRecord> allGrammars) {
        super(context);
        this.title = category_title;
        this.unitNb = unitNb;
        grammarTitles = new ArrayList<String>();
        nbOfGrammars = 0;
        for (int i = 0; i < allGrammars.size(); i++) {
            if (allGrammars.get(i).getCategory().equals(this.title)) {
                nbOfGrammars++;
                grammarTitles.add(allGrammars.get(i).getLabel());
            }
        }
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext(), R.layout.category_card_header_layout);
        header.setTitle("Unit " + Integer.toString(unitNb) + ": " + title);
        return header;
    }

    @Override
    protected void initCard() {
    }


    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {
        TextView grammar_title_textview = (TextView) convertView.findViewById(R.id.grammar_title);
        GrammarObject grammarObject= (GrammarObject)object;
        grammar_title_textview.setText(grammarObject.grammarTitle);
        return convertView;
    }

    @Override
    protected List<ListObject> initChildren() {

        List<ListObject> mObjects = new ArrayList<ListObject>();
        for (int i = 0; i < grammarTitles.size(); i++)
        {
            GrammarObject newGrammarObject = new GrammarObject(this, grammarTitles.get(i));
            mObjects.add(newGrammarObject);
        }
        return mObjects;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.category_card_item_layout;
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
