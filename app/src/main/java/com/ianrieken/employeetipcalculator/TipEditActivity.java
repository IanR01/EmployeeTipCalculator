package com.ianrieken.employeetipcalculator;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ianrieken.employeetipcalculator.data.TipContract;
import com.ianrieken.employeetipcalculator.data.TipContract.RegisterEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TipEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final int EDIT_TIP_LOADER = 1;

    private Uri mCurrentTipUri;

    static final ArrayList<AddedEmployee> addedEmployees = new ArrayList<AddedEmployee>();
    static ArrayAdapter<String> adapter;

    //TODO check static leak error below
    static ListView addedEmployeeListView;
    TextView dateTextView;
    EditText tipAmountEditText;
    static EditText addEmployeeTime;
    AutoCompleteTextView addEmployeeTextView;

    List<String> activeEmployeesList;
    List<Employee> employeesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_edit);

        Intent intent = getIntent();
        mCurrentTipUri = intent.getData();

        if (mCurrentTipUri != null) {
            setTitle(getString(R.string.title_tip_edit));
            getLoaderManager().initLoader(EDIT_TIP_LOADER, null, this);
            Log.v(LOG_TAG, "mCurrentTipUri = " + mCurrentTipUri);
        }

        fillEmployeeLists();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, activeEmployeesList);
        addEmployeeTextView = (AutoCompleteTextView) findViewById(R.id.add_employee_autocompletetextview);
        addEmployeeTextView.setAdapter(adapter);
        addEmployeeTextView.setThreshold(1);

        addEmployeeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG, "A name has been chosen from the dropdown menu on position " + position);
            }
        });



        final AddedEmployeeAdapter addedEmployeeAdapter = new AddedEmployeeAdapter(TipEditActivity.this, addedEmployees);
        addedEmployeeAdapter.clear();

        addedEmployeeListView = (ListView) findViewById(R.id.added_employees_listview);



        addedEmployeeListView.setAdapter(addedEmployeeAdapter);

        LinearLayout datePickerLayout = (LinearLayout) findViewById(R.id.date_picker_layout);
        datePickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        dateTextView = (TextView) findViewById(R.id.date_textview);
        dateTextView.setText(dayOfMonth + "-" + (month + 1) + "-" + year);


        addEmployeeTime = (EditText) findViewById(R.id.time_edittext);
        tipAmountEditText = (EditText) findViewById(R.id.tip_amount_edittext);

        ImageView addEmployeeImageView = (ImageView) findViewById(R.id.imageview_add_employee);
        addEmployeeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Options menu
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tip_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Uri uri = insertTipRegistration();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentTipUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Loadermanager
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RegisterEntry._ID,
                RegisterEntry.COLUMN_REGISTER_AMOUNT,
                RegisterEntry.COLUMN_REGISTER_DATE,
                RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS,
                RegisterEntry.COLUMN_REGISTER_NAMES,
                RegisterEntry.COLUMN_REGISTER_HOURS
        };
        return new CursorLoader(this,
                mCurrentTipUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int amountColumnIndex = cursor.getColumnIndex(RegisterEntry.COLUMN_REGISTER_AMOUNT);
            int dateColumnIndex = cursor.getColumnIndex(RegisterEntry.COLUMN_REGISTER_DATE);
            int employeeIdsColumnIndex = cursor.getColumnIndex(RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS);
            int namesColumnIndex = cursor.getColumnIndex(RegisterEntry.COLUMN_REGISTER_NAMES);
            int hoursColumnIndex = cursor.getColumnIndex(RegisterEntry.COLUMN_REGISTER_HOURS);

            double amount = cursor.getDouble(amountColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String[] employeeIds = cursor.getString(employeeIdsColumnIndex).split(",");
            String[] names = cursor.getString(namesColumnIndex).split(",");
            String[] hours = cursor.getString(hoursColumnIndex).split(",");

            tipAmountEditText.setText(String.valueOf(amount));
            dateTextView.setText(date);

            for (int i=0; i<employeeIds.length; i++) {
                addedEmployees.add(new AddedEmployee(Long.valueOf(employeeIds[i]), names[i], hours[i]));
            }

            addedEmployeeListView.invalidateViews();


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Functions
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // Creates a two lists, one with all employees from the database (filled with Employee class),
    // and one with the String names of all active employees to use in the auto-complete text field
    private void fillEmployeeLists() {
        Cursor c;
        employeesList = new ArrayList<>();
        activeEmployeesList = new ArrayList<String>();

        String[] projection = {
                TipContract.EmployeeEntry._ID,
                TipContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                TipContract.EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE,
                TipContract.EmployeeEntry.COLUMN_EMPLOYEE_BALANCE
        };

        c = getContentResolver().query(TipContract.EmployeeEntry.EMPLOYEE_CONTENT_URI,
                projection,
                null,
                null,
                null);

        while (c.moveToNext()) {
            boolean isActive;
            if(c.getInt(2) == TipContract.EmployeeEntry.EMPLOYEE_ACTIVE) {
                isActive = true;
            } else {
                isActive = false;
            }

            employeesList.add(new Employee(c.getInt(0), c.getString(1), isActive, c.getDouble(3)));

            if(c.getInt(2) == TipContract.EmployeeEntry.EMPLOYEE_ACTIVE) {
                activeEmployeesList.add(c.getString(1));
                Log.v(LOG_TAG, c.getString(1) + " is active");
            }
            Log.v(LOG_TAG, "Added to list: " + c.getString(1) + " with id " + c.getString(0) + " and balance " + c.getDouble(3));
        }

        c.close();
    }

    // Adds an employee to the list
    public AddedEmployee getAddedEmployee(String name, String hours) {
        AddedEmployee employee;

        for (int i=0; i<employeesList.size(); i++) {

            //Check to see whether the entered employee name already exists
            if (name.toLowerCase().trim().equals(employeesList.get(i).getName().toLowerCase().trim())) {

                //Check to see whether the entered employee is marked as active
                if(employeesList.get(i).isActive()) {
                    Log.v(LOG_TAG, "Employee " + employeesList.get(i).getName() + " exists and is active");
                    employee = new AddedEmployee(employeesList.get(i).getId(), employeesList.get(i).getName(), hours);
                    return employee;
                } else {
                    //TODO Pop up a dialog telling the user the employee already exists but is not active. Show registration date.
                    //TODO Give the option to make the employee active again, or go back and change the name
                    Log.v(LOG_TAG, "Employee " + employeesList.get(i).getName() + " exists but is NOT active");
                    employee = new AddedEmployee(employeesList.get(i).getId(), employeesList.get(i).getName(), hours);
                    return employee;
                }
            }
        }

        Log.v(LOG_TAG, "Employee " + name + " does NOT exist yet");

        //TODO Show a confirmation dialog before adding a new employee
        Uri newEmployee = insertEmployee(name, "", TipContract.EmployeeEntry.EMPLOYEE_ACTIVE);
        long id = ContentUris.parseId(newEmployee);
        Log.v(LOG_TAG, "New employee added with id " + id);
        employee = new AddedEmployee(id, name, hours);
        return employee;
    }

    private void addEmployee() {
        if (checkInputName() && checkInputHours()) {
            addedEmployees.add(getAddedEmployee(addEmployeeTextView.getText().toString(), addEmployeeTime.getText().toString()));
            addedEmployeeListView.invalidateViews();
            //TODO check whether the employee is already in the list (preferably also remove the name from the shortlist)
            //TODO check whether the entered time is in the right format
            resetInputFields();
        }
    }

    // Removes an 'added employee' (by the user) from the list
    public static void removeEmployee(int position) {
        addedEmployees.remove(position);
        addedEmployeeListView.invalidateViews();
    }

    // In case a name is entered that doesn't exist yet, a new employee is inserted. The URI with
    // the appended _ID is returned
    public Uri insertEmployee(String name, String memo, int active) {
        ContentValues values = new ContentValues();


        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, name);
        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_MEMO, memo);
        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE, active);

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
    }

    // After an employee has been added to the list, the input fields of employee name and time
    // are made empty and the employee name field gets focus
    private void resetInputFields() {
        addEmployeeTextView.setText("");
        addEmployeeTime.setText("");
        addEmployeeTextView.requestFocus();
    }

    //TODO hier bezig:
    public Uri insertTipRegistration() {
        ContentValues values = new ContentValues();
        String employeeids = "";
        String employeenames = "";
        String employeehours = "";
        String employeedistribution = "";

        for (int i=0; i<addedEmployees.size(); i++) {
            if(i!=0){
                employeeids = employeeids + ",";
                employeenames = employeenames + ",";
                employeehours = employeehours + ",";
                employeedistribution = employeedistribution + ",";
            }
            employeeids = employeeids + String.valueOf(addedEmployees.get(i).getId());
            employeenames = employeenames + String.valueOf(addedEmployees.get(i).getName());
            employeehours = employeehours + String.valueOf(addedEmployees.get(i).getTime());
            employeedistribution = employeedistribution + String.valueOf(addedEmployees.get(i).getNumericHours());
        }

        Log.v(LOG_TAG, "Employee id's: " + employeeids);
        Log.v(LOG_TAG, "Employee names: " + employeenames);
        Log.v(LOG_TAG, "Employee hours: " + employeehours);
        Log.v(LOG_TAG, "Employee hours distribution: " + employeedistribution);

        values.put(RegisterEntry.COLUMN_REGISTER_DATE, dateTextView.getText().toString());
        values.put(RegisterEntry.COLUMN_REGISTER_AMOUNT, tipAmountEditText.getText().toString());
        values.put(RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS, employeeids);
        values.put(RegisterEntry.COLUMN_REGISTER_NAMES, employeenames);
        values.put(RegisterEntry.COLUMN_REGISTER_NREMPLOYEES, addedEmployees.size());
        values.put(RegisterEntry.COLUMN_REGISTER_DISTRIBUTION, employeedistribution);
        values.put(RegisterEntry.COLUMN_REGISTER_HOURS, employeehours);
        values.put(RegisterEntry.COLUMN_REGISTER_ACTION, RegisterEntry.REGISTER_ACTION_TIP);

        double[] currentEmployeeBalance = getCurrentEmployeeBalance(employeeids);

        if (mCurrentTipUri == null) {
            //A new tip registration is being added
            values.put(RegisterEntry.COLUMN_REGISTER_TIMESTAMP_CREATED, System.currentTimeMillis());

            //Insert a new row into the database
            Uri newUri = getContentResolver().insert(RegisterEntry.REGISTER_CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_tip_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_tip_successful), Toast.LENGTH_SHORT).show();
            }

            return newUri;
        } else {
            //An existing tip registration is being updated
            values.put(RegisterEntry.COLUMN_REGISTER_TIMESTAMP_UPDATED, System.currentTimeMillis());

            int rowsAffected = getContentResolver().update(mCurrentTipUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_tip_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_tip_successful), Toast.LENGTH_SHORT).show();
            }

            return mCurrentTipUri;
        }
    }

    public void deleteTipRegistration() {
        if (mCurrentTipUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentTipUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_tip_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_tip_successful), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private double[] getCurrentEmployeeBalance(String ids) {
        String[] id = ids.split(",");
        double[] balance = new double[ids.length()];

        for (int i=0; i<id.length; i++) {
            for(int j=0; j<employeesList.size(); j++) {
                if (employeesList.get(j).getId() == Long.valueOf(id[i])) {
                    balance[i] = employeesList.get(j).getBalance();
                    Log.v(LOG_TAG, "The balance of employee id " + id[i] + " is " + balance[i]);
                }
            }
        }

        return balance;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Checks
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private boolean checkInputName() {
        if(addEmployeeTextView.getText().toString().equals("")) {
            //TODO show dialog
            //TODO make sure no comma is used in employee names
            Log.v(LOG_TAG, "No employee name entered, not adding to the list");
            return false;
        } else {
            for(int i=0; i<addedEmployees.size(); i++) {
                Log.v(LOG_TAG, "Compare array with textfield: " + addedEmployees.get(i).getName() + " and " + addEmployeeTextView.getText().toString());
                if(addedEmployees.get(i).getName().toLowerCase().equals(addEmployeeTextView.getText().toString().toLowerCase())) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean checkInputHours() {
        if (addEmployeeTime.getText().toString().equals("")) {
            if(addEmployeeTime.getVisibility() == View.INVISIBLE) {
                addEmployeeTime.setText("1");
                return true;
            } else {
                Log.v(LOG_TAG, "No amount of hours entered");
                if(addedEmployees.size() == 0) {
                    showNoHoursConfirmationDialog();
                } else {
                    //TODO make option to delete all registered hours
                    showHoursRequiredDialog();
                }
                return false;
            }
        } else if (addEmployeeTime.getText().toString().contains(":")) {
            String[] hoursSeperated = addEmployeeTime.getText().toString().split(":");
            if (hoursSeperated.length == 2) {
                Log.v(LOG_TAG, "Hours seperated into " + hoursSeperated[0] + " and " + hoursSeperated[1]);
                if(hoursSeperated[0].length() > 0 && hoursSeperated[1].length() == 2) {
                    if(Integer.valueOf(hoursSeperated[1]) < 60){
                        return true;
                    } else {
                        Log.e(LOG_TAG, "Minutes should be max 59");
                        return false;
                    }
                } else {
                    Log.e(LOG_TAG, "Hours should at least 1 and minutes should be exactly 2 characters long");
                    return false;
                }
            } else {
                Log.e(LOG_TAG, "Input should only contain hours and minutes");
                return false;
            }
        } else {
            Log.v(LOG_TAG, "Only whole hours entered: " + addEmployeeTime.getText().toString());
            return true;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Confirmation dialogs
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void showNoHoursConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alertdialog_no_hours_message);
        builder.setPositiveButton(R.string.alertdialog_no_hours_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(LOG_TAG, "Positive button selected");
                addEmployeeTime.setVisibility(View.INVISIBLE);

                // After the hours input field has been made invisible, try again to add the employee
                addEmployee();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_no_hours_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(LOG_TAG, "Negative button selected");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showHoursRequiredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alertdialog_hours_required_message);
        builder.setNeutralButton(R.string.alertdialog_hours_required_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alertdialog_delete_message);
        builder.setPositiveButton(R.string.alertdialog_delete_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTipRegistration();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_delete_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
