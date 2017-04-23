package com.ianrieken.employeetipcalculator;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
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

                //TODO remove
                /*--
                if(addEmployeeTime.getText().toString().equals("")) {
                    Log.v(LOG_TAG, "No amount of hours entered");
                    if(addedEmployees.size() == 0) {
                        showNoHoursConfirmationDialog();
                    } else {
                        //TODO make option to delete all registered hours
                        if(addEmployeeTime.getVisibility() == View.VISIBLE) {
                            showHoursRequiredDialog();
                        }
                    }

                }
                --*/
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Options menu
    ///////////////////////////////////////////////////////////////////////////////////////////////

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
                insertTipRegistration();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Loadermanager
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

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
                TipContract.EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE
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

            employeesList.add(new Employee(c.getInt(0), c.getString(1), isActive));

            if(c.getInt(2) == TipContract.EmployeeEntry.EMPLOYEE_ACTIVE) {
                activeEmployeesList.add(c.getString(1));
                Log.v(LOG_TAG, c.getString(1) + " is active");
            }
            Log.v(LOG_TAG, "Added to list: " + c.getString(1) + " with id " + c.getString(0));
        }

        c.close();
    }

    // Adds an employee to the list
    public AddedEmployee getAddedEmployee(String name, String hours) {
        AddedEmployee employee;
        double numericHours;

        numericHours = getNumericHour(hours);

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

        Uri newEmployee = insertEmployee(name, "", TipContract.EmployeeEntry.EMPLOYEE_ACTIVE);
        long id = ContentUris.parseId(newEmployee);
        //TODO fix: a new employee should be created instead of returning one with id 0
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

    //TODO remove?
    private double getNumericHour(String hoursInput) {

        if (hoursInput.equals("")) {
            //TODO Make sure the option to insert time disappears? Or is this already handled somewhere else?
            return 1;
        } else if (hoursInput.contains(":")) {
            String[] hoursSeperated = hoursInput.split(":");
            if (hoursSeperated.length == 2) {
                Log.v(LOG_TAG, "Hours seperated into " + hoursSeperated[0] + " and " + hoursSeperated[1]);
                if(hoursSeperated[0].length() > 0 && hoursSeperated[1].length() == 2) {
                    if(Integer.valueOf(hoursSeperated[1]) < 60){
                        double hoursOutput;
                        hoursOutput = Double.valueOf(hoursSeperated[0]) + (Double.valueOf(hoursSeperated[1]) / 60);
                        Log.v(LOG_TAG, "Succesfully translated hours into double: " + hoursOutput);
                        return hoursOutput;
                    } else {
                        Log.e(LOG_TAG, "Minutes should be max 59");
                        return 0;
                    }
                } else {
                    Log.e(LOG_TAG, "Hours should at least 1 and minutes should be exactly 2 characters long");
                    return 0;
                }
            } else {
                Log.e(LOG_TAG, "Input should only contain hours and minutes");
                return 0;
            }
        } else {
            Log.v(LOG_TAG, "Only whole hours entered: " + hoursInput);
            return Double.valueOf(hoursInput);
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
        String employeehours = "";

        for (int i=0; i<addedEmployees.size(); i++) {
            if(i!=0){
                employeeids = employeeids + ",";
                employeehours = employeehours + ",";
            }
            employeeids = employeeids + String.valueOf(addedEmployees.get(i).getId());
            employeehours = employeehours + String.valueOf(addedEmployees.get(i).getNumericHours());
        }

        Log.v(LOG_TAG, "Employee id's: " + employeeids);
        Log.v(LOG_TAG, "Employee hours: " + employeehours);




        values.put(RegisterEntry.COLUMN_REGISTER_DATE, dateTextView.getText().toString());
        values.put(RegisterEntry.COLUMN_REGISTER_AMOUNT, tipAmountEditText.getText().toString());

        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Checks
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private boolean checkInputName() {
        if(addEmployeeTextView.getText().toString().equals("")) {
            //TODO show dialog
            Log.v(LOG_TAG, "No employee name entered, not adding to the list");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkInputHours() {
        if (addEmployeeTime.getText().toString().equals("")) {
            if(addEmployeeTime.getVisibility() == View.INVISIBLE) {
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

}