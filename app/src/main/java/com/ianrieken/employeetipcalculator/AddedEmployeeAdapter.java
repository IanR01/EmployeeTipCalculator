package com.ianrieken.employeetipcalculator;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IanR8 on 27-Mar-17.
 */

public class AddedEmployeeAdapter extends ArrayAdapter<AddedEmployee> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public AddedEmployeeAdapter(Activity context, ArrayList<AddedEmployee> addedEmployeeArrayList) {
        super(context, 0, addedEmployeeArrayList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_added_employees, parent, false);
        }

        AddedEmployee currentAddedEmployee = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_textview);
        nameTextView.setText(currentAddedEmployee.getName());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_edittext);
        timeTextView.setText(currentAddedEmployee.getTime());

        ImageView deleteImageView = (ImageView) listItemView.findViewById(R.id.delete_icon);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(LOG_TAG, "Geklikt op deletebutton " + position);
                TipEditActivity.removeEmployee(position);
                if(TipEditActivity.addedEmployees.size() == 0) {
                    TipEditActivity.addEmployeeTime.setVisibility(View.VISIBLE);
                }
            }
        });

        return listItemView;
    }
}
