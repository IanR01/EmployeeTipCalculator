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
 * Created by IanR8 on 23-Apr-17.
 */

public class TipCursorAdapter extends CursorAdapter {

    public TipCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_tip, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle = (TextView) view.findViewById(R.id.textview_title);
        TextView tvSubtitle = (TextView) view.findViewById(R.id.textview_subtitle);
        TextView tvAmount = (TextView) view.findViewById(R.id.textview_amount);

        int dateColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_DATE);
        int nrEmployeesColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_NREMPLOYEES);
        int amountColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_AMOUNT);

        String title = cursor.getString(dateColumnIndex);
        String subtitle = String.valueOf(cursor.getInt(nrEmployeesColumnIndex)) + " " + "aanpassen in TipCursorAdapter";
        String amount = String.valueOf(cursor.getDouble(amountColumnIndex));

        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
        tvAmount.setText(amount);
    }
}
