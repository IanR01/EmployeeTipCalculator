package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {


    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        final ArrayList<Tip> payments = new ArrayList<Tip>();
        payments.add(new Tip("Februari", "7/13 uitbetaald", 294.45));
        payments.add(new Tip("Januari", "", 180.50));
        payments.add(new Tip("December", "18/18 uitbetaald", 301.80));


        final TipAdapter adapter = new TipAdapter(getActivity(), payments);

        //TODO make it a recyclerview?
        ListView paymentListView = (ListView) view.findViewById(R.id.payment_list_view);

        paymentListView.setAdapter(adapter);

        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent paymentDetailIntent = new Intent(getActivity(), PaymentDetailActivity.class);
                startActivity(paymentDetailIntent);
            }
        });

        return view;
    }

}
