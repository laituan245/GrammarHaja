package laituan245.projects.koreangrammarhaja;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InformationFragment extends Fragment {
    final static String ARG_POSITION = "position";
    static int mCurrentPosition = -1;
    String[]InformationArray;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        InformationArray = new String[MainActivity.allGrammarArray.size()];
        for (int i = 0; i < MainActivity.allGrammarArray.size(); i++)
            InformationArray[i] = (MainActivity.allGrammarArray.get(i).getInformation());

        return inflater.inflate(R.layout.information_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            updateInformationView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            updateInformationView(mCurrentPosition);
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

    public void updateInformationView(int position) {
        TextView information = (TextView) getActivity().findViewById(R.id.information);
        if (position == -1) {
            information.setText("");
            mCurrentPosition = -1;
            return;
        }
        // Set the text size appropriately
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            information.setTextSize(16);
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            information.setTextSize(16);
        }

        information.setText(Html.fromHtml(preProcessingGrammarInfo(InformationArray[position], GrammarLabelsFragment.GrammarLabels[position])));
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}
