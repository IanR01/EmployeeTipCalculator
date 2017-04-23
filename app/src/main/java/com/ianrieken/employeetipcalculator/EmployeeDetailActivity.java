package com.ianrieken.employeetipcalculator;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;

public class EmployeeDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int EXISTING_EMPLOYEE_LOADER = 0;

    private Uri mCurrentEmployeeUri;

    private TextView mBalanceTextView;
    private TextView mMemoTextView;
    private TextView mActiveTextView;
    private TextView mRegdateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        Intent intent = getIntent();
        mCurrentEmployeeUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_EMPLOYEE_LOADER, null, this);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mBalanceTextView = (TextView) findViewById(R.id.textview_detail_balance);
        mMemoTextView = (TextView) findViewById(R.id.textview_detail_memo);
        mActiveTextView = (TextView) findViewById(R.id.textview_detail_active);
        mRegdateTextView = (TextView) findViewById(R.id.textview_detail_regdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Log.v(LOG_TAG, "Item in menu selected: " + item.getItemId());
                Intent employeeEditIntent = new Intent(EmployeeDetailActivity.this, EmployeeEditActivity.class);
                employeeEditIntent.setData(mCurrentEmployeeUri);
                startActivity(employeeEditIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EmployeeEntry._ID,
                EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeEntry.COLUMN_EMPLOYEE_BALANCE,
                EmployeeEntry.COLUMN_EMPLOYEE_MEMO,
                EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE,
                EmployeeEntry.COLUMN_EMPLOYEE_REGDATE
        };
        return new CursorLoader(this,
                mCurrentEmployeeUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_NAME);
            int balanceColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_BALANCE);
            int memoColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_MEMO);
            int activeColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE);
            int regdateColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_REGDATE);

            String name = cursor.getString(nameColumnIndex);
            double balance = cursor.getDouble(balanceColumnIndex);
            String memo = cursor.getString(memoColumnIndex);
            int active = cursor.getInt(activeColumnIndex);
            String regdate = cursor.getString(regdateColumnIndex);

            setTitle(name);
            mBalanceTextView.setText(String.valueOf(balance));
            mMemoTextView.setText(memo);
            switch (active) {
                case EmployeeEntry.EMPLOYEE_ACTIVE:
                    mActiveTextView.setText(R.string.caption_employee_active_yes);
                    break;
                case EmployeeEntry.EMPLOYEE_INACTIVE:
                    mActiveTextView.setText(R.string.caption_employee_active_no);
                    break;
                default:
                    mActiveTextView.setText("Error");
            }
            mRegdateTextView.setText(regdate);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
