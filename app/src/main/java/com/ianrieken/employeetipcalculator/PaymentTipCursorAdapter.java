package com.ianrieken.employeetipcalculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ianrieken.employeetipcalculator.data.TipContract.RegisterEntry;

/**
 * Created by IanR8 on 12-May-17.
 */

public class PaymentTipCursorAdapter extends CursorAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public PaymentTipCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_payment_new, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle = (TextView) view.findViewById(R.id.payment_title_textview);
        TextView tvSubtitle = (TextView) view.findViewById(R.id.payment_subtitle_textview);
        TextView tvAmount = (TextView) view.findViewById(R.id.payment_amount_textview);

        int dateColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_DATE);
        int nrEmployeesColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_NREMPLOYEES);
        int amountColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_AMOUNT);
        int descriptionColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_DESCRIPTION);

        String title = "";
        String subtitle = String.valueOf(cursor.getInt(nrEmployeesColumnIndex)) + " " + "pax";
        String amount = String.valueOf(cursor.getDouble(amountColumnIndex));

        if(cursor.getString(descriptionColumnIndex) == null){
            title = cursor.getString(dateColumnIndex);
        } else {
            title = cursor.getString(descriptionColumnIndex);
        }

        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
        tvAmount.setText(amount);
    }
}
