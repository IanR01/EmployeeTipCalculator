package com.ianrieken.employeetipcalculator;

/**
 * Created by IanR8 on 26-Mar-17.
 */

public class Tip {
    private String mTitle;
    private String mSubTitle;
    private double mAmount;

    public Tip(String title, String subTitle, double amount){
        mTitle = title;
        mSubTitle = subTitle;
        mAmount = amount;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public double getAmount() {
        return mAmount;
    }

    @Override
    public String toString() {
        return "Tip(mTitle = " + mTitle + ", mSubTitle = " + mSubTitle + ", mAmount = " + mAmount + ")";
    }
}
