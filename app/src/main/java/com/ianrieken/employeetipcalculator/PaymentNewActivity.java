package com.ianrieken.employeetipcalculator;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ianrieken.employeetipcalculator.data.TipContract.RegisterEntry;

import java.util.ArrayList;

public class PaymentNewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public final static String DATEPICKER_TAG = "newPaymentDatePicker";
    private final static int TIP_LOADER = 10;

    CursorAdapter tipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_new);

        ListView tipsListView = (ListView) findViewById(R.id.payment_new_listview);

        tipAdapter = new PaymentTipCursorAdapter(this, null);
        tipsListView.setAdapter(tipAdapter);

        LinearLayout dateToPickerLayout = (LinearLayout) findViewById(R.id.payment_new_date_to);
        dateToPickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        getLoaderManager().initLoader(TIP_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Log.v(LOG_TAG, "Item in menu selected: " + item.getItemId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RegisterEntry._ID,
                RegisterEntry.COLUMN_REGISTER_AMOUNT,
                RegisterEntry.COLUMN_REGISTER_DATE,
                RegisterEntry.COLUMN_REGISTER_NREMPLOYEES,
                RegisterEntry.COLUMN_REGISTER_ACTION,
                RegisterEntry.COLUMN_REGISTER_DESCRIPTION
        };
        String selection = RegisterEntry.COLUMN_REGISTER_ACTION + "=?";
        String[] selectionArgs = new String[]{ String.valueOf(RegisterEntry.REGISTER_ACTION_TIP) };

        return new CursorLoader(this,
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
