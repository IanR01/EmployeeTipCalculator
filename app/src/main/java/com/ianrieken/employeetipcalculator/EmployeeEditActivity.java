package com.ianrieken.employeetipcalculator;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.ianrieken.employeetipcalculator.data.TipContract;
import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;
import com.ianrieken.employeetipcalculator.data.TipProvider;

import java.util.Calendar;

public class EmployeeEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int UPDATE_EMPLOYEE_LOADER = 0;

    private Uri mCurrentEmployeeUri;

    private EditText nameEditText;
    private EditText memoEditText;
    private Switch activeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        Intent intent = getIntent();
        mCurrentEmployeeUri = intent.getData();

        if (mCurrentEmployeeUri == null) {
            //Do nothing?
        } else {
            setTitle(R.string.title_employee_edit_update);
            getLoaderManager().initLoader(UPDATE_EMPLOYEE_LOADER, null, this);
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        nameEditText = (EditText) findViewById(R.id.edit_text_name);
        memoEditText = (EditText) findViewById(R.id.edt_text_memo);
        activeSwitch = (Switch) findViewById(R.id.switch_active);
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

                int active = 0;
                if (activeSwitch.isChecked()) {
                    active = EmployeeEntry.EMPLOYEE_ACTIVE;
                } else {
                    active = EmployeeEntry.EMPLOYEE_INACTIVE;
                }

                Intent employeeDetailIntent = new Intent(EmployeeEditActivity.this, EmployeeDetailActivity.class);
                Uri uri = insertEmployee(nameEditText.getText().toString(), memoEditText.getText().toString(), active);
                employeeDetailIntent.setData(uri);
                startActivity(employeeDetailIntent);
                Log.v(LOG_TAG, "Item in menu selected: " + item.getItemId());
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
                EmployeeEntry.COLUMN_EMPLOYEE_MEMO,
                EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE
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
            int memoColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_MEMO);
            int activeColumnIndex = cursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE);

            String name = cursor.getString(nameColumnIndex);
            String memo = cursor.getString(memoColumnIndex);
            int active = cursor.getInt(activeColumnIndex);

            nameEditText.setText(name);
            memoEditText.setText(memo);
            switch (active) {
                case EmployeeEntry.EMPLOYEE_ACTIVE:
                    activeSwitch.setChecked(true);
                    break;
                case EmployeeEntry.EMPLOYEE_INACTIVE:
                    activeSwitch.setChecked(false);
                    break;
                default:
                    Log.e(LOG_TAG, "Error in setting active switch");
                    activeSwitch.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public Uri insertEmployee(String name, String memo, int active) {
        ContentValues values = new ContentValues();


        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, name);
        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_MEMO, memo);
        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE, active);


        if (mCurrentEmployeeUri == null) {
            // In case of a new employee, the registration date should be added
            Calendar rightNow = Calendar.getInstance();
            String testDate = rightNow.get(Calendar.DAY_OF_MONTH) + "-" + (rightNow.get(Calendar.MONTH)+1) + "-" + rightNow.get(Calendar.YEAR);
            values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_REGDATE, testDate);

            // Then a new row should be inserted into the database
            Uri newUri = getContentResolver().insert(TipContract.EmployeeEntry.EMPLOYEE_CONTENT_URI, values);

            if(newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_employee_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_employee_successful), Toast.LENGTH_SHORT).show();
            }

            return newUri;
        } else {
            int rowsAffected = getContentResolver().update(mCurrentEmployeeUri, values, null, null);
            if(rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_employee_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_employee_successful), Toast.LENGTH_SHORT).show();
            }
            return mCurrentEmployeeUri;
        }
    }
}