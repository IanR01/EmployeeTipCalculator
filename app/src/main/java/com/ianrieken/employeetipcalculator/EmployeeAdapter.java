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

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    public EmployeeAdapter(Activity context, ArrayList<Employee> employeeArrayList) {
        super(context, 0, employeeArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_employee, parent, false);
        }

        Employee currentEmployee = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.textview_name);
        nameTextView.setText(currentEmployee.getName());

        TextView balanceTextView = (TextView) listItemView.findViewById(R.id.textview_balance);
        balanceTextView.setText(String.valueOf(currentEmployee.getBalance()));

        return listItemView;
    }
}
