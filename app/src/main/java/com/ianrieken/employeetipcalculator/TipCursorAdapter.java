package com.ianrieken.employeetipcalculator;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
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

    private final String LOG_TAG = this.getClass().getSimpleName();

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
        int actionColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_ACTION);
        int descriptionColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_DESCRIPTION);
        int paidColumnIndex = cursor.getColumnIndexOrThrow(RegisterEntry.COLUMN_REGISTER_PAID);


        //TODO retrieve string from strings.xml resources file: amount_employees
        String title = "";
        String subtitle = "";
        String amount = String.valueOf(cursor.getDouble(amountColumnIndex));
        int action = cursor.getInt(actionColumnIndex);

        switch(action){
            case RegisterEntry.REGISTER_ACTION_TIP:
                title = cursor.getString(dateColumnIndex);
                subtitle = String.valueOf(cursor.getInt(nrEmployeesColumnIndex)) + " " + "pax";
                break;
            case RegisterEntry.REGISTER_ACTION_PAYMENT:
                if(cursor.getString(descriptionColumnIndex) == null){
                    title = cursor.getString(dateColumnIndex);
                } else {
                    title = cursor.getString(descriptionColumnIndex);
                }
                String paid = cursor.getString(paidColumnIndex);
                subtitle = paid;
                //TODO Format subtitle string something like: 3/4 paid
                break;
            default:
                Log.e(LOG_TAG, "REGISTER_ACTION not recognised");
                break;
        }

        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
        tvAmount.setText(amount);
    }
}
