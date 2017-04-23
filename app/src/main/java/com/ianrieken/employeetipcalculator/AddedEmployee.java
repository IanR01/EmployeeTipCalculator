package com.ianrieken.employeetipcalculator;

import android.util.Log;

/**
 * Created by IanR8 on 27-Mar-17.
 */

public class AddedEmployee {
    private long mId;
    private String mName;
    private String mTime;

    public AddedEmployee(long id, String name, String time){
        mId = id;
        mName = name;
        mTime = time;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getTime() {
        return mTime;
    }

    public double getNumericHours() {
        if (mTime.contains(":")) {
            String[] hoursSeperated = mTime.split(":");
            if (hoursSeperated.length == 2) {
                if(hoursSeperated[0].length() > 0 && hoursSeperated[1].length() == 2) {
                    if(Integer.valueOf(hoursSeperated[1]) < 60){
                        double hoursOutput;
                        hoursOutput = Double.valueOf(hoursSeperated[0]) + (Double.valueOf(hoursSeperated[1]) / 60);
                        return hoursOutput;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return Double.valueOf(mTime);
        }
    }

    @Override
    public String toString() {
        return "AddedEmployee(mName = " + mName + ", mTime = " + mTime + ")";
    }
}
