package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
public class PaymentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int PAYMENT_LOADER = 1;
    CursorAdapter paymentAdapter;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        //TODO make it a recyclerview?
        ListView paymentListView = (ListView) view.findViewById(R.id.payment_list_view);
        View emptyViewPayment = view.findViewById(R.id.empty_view_payment);
        paymentListView.setEmptyView(emptyViewPayment);

        paymentAdapter = new TipCursorAdapter(getContext(), null); //Using a TipCursorAdapter: the layout is the same
        paymentListView.setAdapter(paymentAdapter);

        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent paymentDetailIntent = new Intent(getActivity(), PaymentDetailActivity.class);
                startActivity(paymentDetailIntent);
            }
        });

        getLoaderManager().initLoader(PAYMENT_LOADER, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RegisterEntry._ID,
                RegisterEntry.COLUMN_REGISTER_AMOUNT,
                RegisterEntry.COLUMN_REGISTER_DATE,
                RegisterEntry.COLUMN_REGISTER_NREMPLOYEES,
                RegisterEntry.COLUMN_REGISTER_PAID
        };
        String selection = RegisterEntry.COLUMN_REGISTER_ACTION + "=?";
        String[] selectionArgs = new String[]{ String.valueOf(RegisterEntry.REGISTER_ACTION_PAYMENT)};

        return new CursorLoader(getContext(),
                RegisterEntry.REGISTER_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        paymentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        paymentAdapter.swapCursor(null);
    }
}
