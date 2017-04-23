package com.ianrieken.employeetipcalculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;
import com.ianrieken.employeetipcalculator.data.TipContract.RegisterEntry;
import com.ianrieken.employeetipcalculator.data.TipContract.PaymentEntry;

/**
 * Created by IanR8 on 30-Mar-17.
 */

public class TipDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tippot.db";
    public static final int DATABASE_VERSION = 1;

    public TipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + EmployeeEntry.TABLE_NAME + " ("
                + EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeeEntry.COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, "
                + EmployeeEntry.COLUMN_EMPLOYEE_BALANCE + " INTEGER NOT NULL DEFAULT 0, "
                + EmployeeEntry.COLUMN_EMPLOYEE_EARNED + " INTEGER NOT NULL DEFAULT 0, "
                + EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE + " INTEGER NOT NULL DEFAULT 1, "
                + EmployeeEntry.COLUMN_EMPLOYEE_MEMO + " TEXT, "
                + EmployeeEntry.COLUMN_EMPLOYEE_REGDATE + " TEXT, "
                + EmployeeEntry.COLUMN_EMPLOYEE_COUNT + " INTEGER NOT NULL DEFAULT 0);";

        String SQL_CREATE_REGISTER_TABLE = "CREATE TABLE " + RegisterEntry.TABLE_NAME + " ("
                + RegisterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RegisterEntry.COLUMN_REGISTER_TIMESTAMP + " INTEGER NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_DATE + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_AMOUNT + " INTEGER NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_DISTRIBUTION + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_ACTION + " INTEGER NOT NULL DEFAULT 0, "
                + RegisterEntry.COLUMN_REGISTER_PAYMENTID + " INTEGER);";

        String SQL_CREATE_PAYMENTS_TABLE = "CREATE TABLE " + PaymentEntry.TABLE_NAME + " ("
                + PaymentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PaymentEntry.COLUMN_PAYMENT_TIMESTAMP_CREATED + " INTEGER NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_TIMESTAMP_UPDATED + " INTEGER, "
                + PaymentEntry.COLUMN_PAYMENT_EMPLOYEEIDS + " TEXT NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_PAYED + " INTEGER NOT NULL DEFAULT 1, "
                + PaymentEntry.COLUMN_PAYMENT_PAYMENT_DATE + " TEXT NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_UNTIL_DATE + " TEXT, "
                + PaymentEntry.COLUMN_PAYMENT_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_EMPLOYEES_TABLE);
        db.execSQL(SQL_CREATE_REGISTER_TABLE);
        db.execSQL(SQL_CREATE_PAYMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
