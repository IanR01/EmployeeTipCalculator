package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.support.v4.content.Loader;
import android.content.Loader;
import android.database.Cursor;
//import android.support.v4.content.CursorLoader;
import android.content.CursorLoader;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.ianrieken.employeetipcalculator.data.TipContract;
import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int EMPLOYEE_LOADER = 0;
    CursorAdapter employeeAdapter;


    public EmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        //TODO make it a recyclerview?
        ListView employeeListView = (ListView) view.findViewById(R.id.employee_list_view);
        View emptyViewEmployees = view.findViewById(R.id.empty_view_employees);
        employeeListView.setEmptyView(emptyViewEmployees);

        employeeAdapter = new EmployeeCursorAdapter(getContext(), null);
        employeeListView.setAdapter(employeeAdapter);


        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent employeeDetailIntent = new Intent(getActivity(), EmployeeDetailActivity.class);
                Uri currentEmployeeUri = ContentUris.withAppendedId(EmployeeEntry.EMPLOYEE_CONTENT_URI, id);
                employeeDetailIntent.setData(currentEmployeeUri);
                Log.v(LOG_TAG, "Uri added to intent: " + currentEmployeeUri);
                startActivity(employeeDetailIntent);
            }
        });

        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EmployeeEntry._ID,
                EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeEntry.COLUMN_EMPLOYEE_BALANCE
        };

        return new CursorLoader(getContext(),
                EmployeeEntry.EMPLOYEE_CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        employeeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        employeeAdapter.swapCursor(null);
    }



}
