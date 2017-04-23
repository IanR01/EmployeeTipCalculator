package com.ianrieken.employeetipcalculator;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by IanR8 on 21-Mar-17.
 */

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {

    //The number of tabs used in the app
    private final static int NUMBER_OF_TABS = 3;

    public FixedTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TipFragment();
            case 1:
                return new PaymentFragment();
            case 2:
                return new EmployeesFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }
}
