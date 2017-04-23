package com.ianrieken.employeetipcalculator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IanR8 on 27-Mar-17.
 */

public class PaymentAdapter extends ArrayAdapter<Payment> {

    public PaymentAdapter(Activity context, ArrayList<Payment> paymentArrayList) {
        super(context, 0, paymentArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_payment_new, parent, false);
        }

        Payment currentPayment = getItem(position);

        CheckBox checkedCheckBox = (CheckBox) listItemView.findViewById(R.id.payment_new_checkbox);
        checkedCheckBox.setChecked(currentPayment.isChecked());

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.payment_name_textview);
        nameTextView.setText(currentPayment.getName());

        TextView subTitleTextView = (TextView) listItemView.findViewById(R.id.payment_subtitle_textview);
        subTitleTextView.setText(currentPayment.getSubTitle());

        TextView amountTextView = (TextView) listItemView.findViewById(R.id.payment_amount_textview);
        amountTextView.setText(String.valueOf(currentPayment.getAmount()));

        return listItemView;
    }
}
