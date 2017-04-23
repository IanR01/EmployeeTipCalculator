package com.ianrieken.employeetipcalculator;

/**
 * Created by IanR8 on 27-Mar-17.
 */

public class Payment {
    private boolean mChecked;
    private String mName;
    private String mSubTitle;
    private double mAmount;

    public Payment(boolean checked, String name, String subTitle, double amount){
        mChecked = checked;
        mName = name;
        mSubTitle = subTitle;
        mAmount = amount;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public String getName() {
        return mName;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public double getAmount() {
        return mAmount;
    }

    @Override
    public String toString() {
        return "Tip(mChecked = " + mChecked + ", mName = " + mName + ", mSubTitle = " + mSubTitle + ", mAmount = " + mAmount + ")";
    }
}
