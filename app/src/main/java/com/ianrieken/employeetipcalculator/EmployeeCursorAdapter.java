package com.ianrieken.employeetipcalculator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;

/**
 * Created by IanR8 on 31-Mar-17.
 */

public class EmployeeCursorAdapter extends CursorAdapter {

    public EmployeeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_employee, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.textview_name);
        TextView tvBalance = (TextView) view.findViewById(R.id.textview_balance);

        int nameColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_EMPLOYEE_NAME);
        int balanceColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_EMPLOYEE_BALANCE);

        String name = cursor.getString(nameColumnIndex);
        String balance = String.valueOf(cursor.getDouble(balanceColumnIndex));

        tvName.setText(name);
        tvBalance.setText(balance);
    }
}
