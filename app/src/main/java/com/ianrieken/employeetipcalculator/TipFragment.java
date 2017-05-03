package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.ianrieken.employeetipcalculator.data.TipContract.RegisterEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class TipFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int TIP_LOADER = 1;
    CursorAdapter tipAdapter;


    public TipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tip, container, false);

        //TODO make it a recyclerview?
        ListView tipListView = (ListView) view.findViewById(R.id.tip_list_view);
        View emptyViewTip = view.findViewById(R.id.empty_view_tip);
        tipListView.setEmptyView(emptyViewTip);

        tipAdapter = new TipCursorAdapter(getContext(), null);
        tipListView.setAdapter(tipAdapter);

        tipListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tipEditIntent = new Intent(getActivity(), TipEditActivity.class);
                Uri currentTipUri = ContentUris.withAppendedId(RegisterEntry.REGISTER_CONTENT_URI, id);
                tipEditIntent.setData(currentTipUri);
                startActivity(tipEditIntent);
            }
        });

        getLoaderManager().initLoader(TIP_LOADER, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {
                RegisterEntry._ID,
                RegisterEntry.COLUMN_REGISTER_AMOUNT,
                RegisterEntry.COLUMN_REGISTER_DATE,
                RegisterEntry.COLUMN_REGISTER_NREMPLOYEES,
                RegisterEntry.COLUMN_REGISTER_ACTION,
                RegisterEntry.COLUMN_REGISTER_DESCRIPTION,
                RegisterEntry.COLUMN_REGISTER_PAID
        };
        String selection = RegisterEntry.COLUMN_REGISTER_ACTION + "=?";
        String[] selectionArgs = new String[]{ String.valueOf(RegisterEntry.REGISTER_ACTION_TIP) };

        return new CursorLoader(getContext(),
                RegisterEntry.REGISTER_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tipAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tipAdapter.swapCursor(null);
    }
}
