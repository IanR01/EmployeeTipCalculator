package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TipFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();


    public TipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tip, container, false);

        final ArrayList<Tip> tips = new ArrayList<Tip>();
        tips.add(new Tip("Eerste dag", "3 personen - 3,23 p.p.", 45.67));
        tips.add(new Tip("Tweede dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Derde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Vierde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Vijfde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Zesde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Zevende dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Achtste dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Negende dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Tiende dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Elfde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Twaalfde dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Dertiende dag", "5 personen - 5,12 p.p.", 82.32));
        tips.add(new Tip("Veertiende dag", "5 personen - 5,12 p.p.", 82.32));


        final TipAdapter adapter = new TipAdapter(getActivity(), tips);

        //TODO make it a recyclerview?
        ListView tipListView = (ListView) view.findViewById(R.id.tip_list_view);

        tipListView.setAdapter(adapter);

        return view;
    }

}
