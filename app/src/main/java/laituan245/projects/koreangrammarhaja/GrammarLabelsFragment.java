package laituan245.projects.koreangrammarhaja;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GrammarLabelsFragment extends ListFragment {
    OnGrammarLabelSelectedListener mCallback;
    static String[] GrammarLabels;

    public interface OnGrammarLabelSelectedListener {
        public void onGrammarSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        String tempString;
        GrammarLabels = new String[MainActivity.allGrammarArray.size()];
        for (int i = 0; i < MainActivity.allGrammarArray.size(); i++) {
            tempString = " ";
            if (i + 1 < 10)
                tempString = tempString + "  ";
            else if (i + 1 < 100)
                tempString = tempString + "";
            GrammarLabels[i] = Integer.toString(i + 1) + "." + tempString + (MainActivity.allGrammarArray.get(i).getLabel());
        }

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, GrammarLabels));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getFragmentManager().findFragmentById(R.id.information_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnGrammarLabelSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGrammarLabelSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onGrammarSelected(position);
        getListView().setItemChecked(position, true);
        getListView().clearChoices();
        getListView().requestLayout();
    }
}
