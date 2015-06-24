package laituan245.projects.koreangrammarhaja;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GrammarLabelsFragment extends ListFragment {
    OnGrammarLabelSelectedListener mCallback;
    static String[] GrammarLabels;
    public static int[] LabelMapping;
    private int layout;

    public interface OnGrammarLabelSelectedListener {
        public void onGrammarSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        LabelMapping = new int[MainActivity.allGrammarArray.size()];

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

    private String removeUnnecessaryChars(String inputText) {
        String tempString = inputText.replaceAll(" ", "");
        tempString =  tempString.replaceAll("/", "");
        tempString =  tempString.replaceAll("-", "");
        tempString =  tempString.replace(".", "");
        for (int tempNb = 0; tempNb < 10; tempNb++)
            tempString = tempString.replaceAll(Integer.toString(tempNb), "");
        return tempString;
    }

    public void filterText (String text) {
        int n = MainActivity.allGrammarArray.size();
        int countFailed = 0;
        text = removeUnnecessaryChars(text);

        boolean [] passedFilter = new boolean[n];
        for (int i = 0; i < n; i++) {
            passedFilter[i] = false;
            String tempString = removeUnnecessaryChars(GrammarLabels[i]);
            if (tempString.contains(text))
                passedFilter[i] = true;
            else
                countFailed++;
        }

        //
        String[] tempGrammarLabels = new String[n-countFailed];
        int tempIndex = 0;
        for (int i = 0; i < n; i++)
            if (passedFilter[i])
            {
                LabelMapping[tempIndex] = i;
                tempGrammarLabels[tempIndex] = GrammarLabels[i];
                tempIndex++;
            }

        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, tempGrammarLabels));


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
