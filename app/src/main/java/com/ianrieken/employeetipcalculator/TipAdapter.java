package com.ianrieken.employeetipcalculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IanR8 on 26-Mar-17.
 */

public class TipAdapter extends ArrayAdapter<Tip> {

    public TipAdapter(Activity context, ArrayList<Tip> tipArrayList) {
        super(context, 0, tipArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_tip, parent, false);
        }

        Tip currentTip = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.textview_title);
        titleTextView.setText(currentTip.getTitle());

        TextView subTitleTextView = (TextView) listItemView.findViewById(R.id.textview_subtitle);
        subTitleTextView.setText(currentTip.getSubTitle());

        TextView amountTextView = (TextView) listItemView.findViewById(R.id.textview_amount);
        amountTextView.setText(String.valueOf(currentTip.getAmount()));

        return listItemView;
    }
}
