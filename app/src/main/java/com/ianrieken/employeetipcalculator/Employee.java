package com.ianrieken.employeetipcalculator;

/**
 * Created by IanR8 on 26-Mar-17.
 */

public class Employee {
    private long mId;
    private String mName;
    private double mBalance;
    private boolean mActive;

    public Employee(String name, double balance){
        mName = name;
        mBalance = balance;
    }

    public Employee(long id, String name, boolean active, double balance){
        mId = id;
        mName = name;
        mActive = active;
        mBalance = balance;
    }

    public String getName() {
        return mName;
    }

    public double getBalance() {
        return mBalance;
    }

    public long getId() {
        return mId;
    }

    public boolean isActive() {
        return mActive;
    }

    @Override
    public String toString() {
        return "Employee(mName = " + mName + ", mBalance = " + String.valueOf(mBalance) + ")";
    }
}
