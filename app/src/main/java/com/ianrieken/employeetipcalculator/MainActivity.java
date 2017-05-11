package com.ianrieken.employeetipcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ianrieken.employeetipcalculator.data.TipContract;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final static int SECTION_TIP = 0;
    private final static int SECTION_PAYMENT = 1;
    private final static int SECTION_EMPLOYEES = 2;

    private final String LOG_TAG = this.getClass().getSimpleName();
    int[] tabTitles = {R.string.title_tab_tip, R.string.title_tab_payment, R.string.title_tab_employees};
    int[] tabIcons = {R.drawable.ic_list_white_24dp, R.drawable.ic_payment_white_24dp, R.drawable.ic_people_white_24dp};

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter mPagerAdapter = new FixedTabsPagerAdapter(getFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        // Place the icons from the array on the tabs
        for(int i=0; i<mTabLayout.getTabCount(); i++) {
            try {
                mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
            } catch (RuntimeException e){
                Log.e(LOG_TAG, "Error while placing icon on tab index " + i + ": " + e);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the FAB perform the right action for the selected tab
                switch(mViewPager.getCurrentItem()){
                    case SECTION_TIP:
                        Intent tipEditIntent = new Intent(MainActivity.this, TipEditActivity.class);
                        startActivity(tipEditIntent);
                        break;
                    case SECTION_PAYMENT:
                        Intent paymentNewIntent = new Intent(MainActivity.this, PaymentNewActivity.class);
                        startActivity(paymentNewIntent);
                        break;
                    case SECTION_EMPLOYEES:
                        Intent employeeEditIntent = new Intent(MainActivity.this, EmployeeEditActivity.class);
                        startActivity(employeeEditIntent);
                        break;
                    default:
                        Log.e(LOG_TAG, "FAB clicked, but no tab position was received");
                        break;
                }
            }
        });

        //Set the title in the actionbar to the title of the selected tab
        setTitle(tabTitles[mTabLayout.getSelectedTabPosition()]);
        getSupportActionBar().setElevation(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                //Change the title in the actionbar when another tab is selected
                setTitle(tabTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_rows:
                switch (mViewPager.getCurrentItem()) {
                    case SECTION_TIP:
                        break;
                    case SECTION_PAYMENT:
                        break;
                    case SECTION_EMPLOYEES:
                        getContentResolver().delete(TipContract.EmployeeEntry.EMPLOYEE_CONTENT_URI, null, null);
                        break;
                }
                return true;
            case R.id.action_insert_dummy_data:
                ContentValues values = new ContentValues();
                Calendar rightNow = Calendar.getInstance();
                Uri newUri;

                switch (mViewPager.getCurrentItem()) {
                    case SECTION_TIP:
                        break;
                    case SECTION_PAYMENT:
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_TIMESTAMP_CREATED, rightNow.get(Calendar.DATE));
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_DATE, rightNow.get(Calendar.DATE));
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_AMOUNT, 45.80);
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS, "1,2,3,4");
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_NAMES, "Naam1,Naam2,Naam3,Naam4");
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_NREMPLOYEES, 4);
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_DISTRIBUTION, "10.50,40,25,25");
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_PAID, "1,0,1,1");
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_ACTION, TipContract.RegisterEntry.REGISTER_ACTION_PAYMENT);
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_REGISTERIDS, "1,2,3,4");
                        values.put(TipContract.RegisterEntry.COLUMN_REGISTER_DESCRIPTION, "Wat gebeurt er met een beschrijving die veel te lang is? Loopt die het scherm uit of gaat deze automatisch door naar de volgende regel?");

                        newUri = getContentResolver().insert(TipContract.RegisterEntry.REGISTER_CONTENT_URI, values);
                        Log.v(LOG_TAG, "New Uri inserted: " + newUri);

                        break;
                    case SECTION_EMPLOYEES:
                        //TODO remove:
                        /*
                        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Rish");
                        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_BALANCE, 12.35);
                        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE, TipContract.EmployeeEntry.EMPLOYEE_INACTIVE);
                        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_MEMO, "Test memo");
                        String testDate = rightNow.get(Calendar.DAY_OF_MONTH) + "-" + rightNow.get(Calendar.MONTH) + "-" + rightNow.get(Calendar.YEAR);
                        Log.v(LOG_TAG, "Testdate = " + testDate);
                        values.put(TipContract.EmployeeEntry.COLUMN_EMPLOYEE_REGDATE, testDate);

                        newUri = getContentResolver().insert(TipContract.EmployeeEntry.EMPLOYEE_CONTENT_URI, values);
                        Log.v(LOG_TAG, "New Uri inserted: " + newUri);
                        */
                        break;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
