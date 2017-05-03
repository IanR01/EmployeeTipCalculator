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
                + EmployeeEntry.COLUMN_EMPLOYEE_ACTIVE + " INTEGER NOT NULL DEFAULT " + EmployeeEntry.EMPLOYEE_ACTIVE + ", "
                + EmployeeEntry.COLUMN_EMPLOYEE_MEMO + " TEXT, "
                + EmployeeEntry.COLUMN_EMPLOYEE_REGDATE + " TEXT, "
                + EmployeeEntry.COLUMN_EMPLOYEE_COUNT + " INTEGER NOT NULL DEFAULT 0);";

        String SQL_CREATE_REGISTER_TABLE = "CREATE TABLE " + RegisterEntry.TABLE_NAME + " ("
                + RegisterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RegisterEntry.COLUMN_REGISTER_TIMESTAMP_CREATED + " INTEGER NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_TIMESTAMP_UPDATED + " INTEGER, "
                + RegisterEntry.COLUMN_REGISTER_DATE + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_AMOUNT + " INTEGER NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_EMPLOYEEIDS + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_NAMES + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_NREMPLOYEES + " INTEGER NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_DISTRIBUTION + " TEXT NOT NULL, "
                + RegisterEntry.COLUMN_REGISTER_HOURS + " TEXT, "
                + RegisterEntry.COLUMN_REGISTER_PAID + " TEXT NOT NULL, " // Array of PAYMENT_PENDING and PAYMENT_COMPLETED when not everyone is paid, else the value is just PAYMENT_ALL_PAID
                + RegisterEntry.COLUMN_REGISTER_ACTION + " INTEGER NOT NULL DEFAULT 0, "
                + RegisterEntry.COLUMN_REGISTER_PAYMENTID + " INTEGER NOT NULL DEFAULT 0, "
                + RegisterEntry.COLUMN_REGISTER_REGISTERIDS + " TEXT);"; // Only used when a payment is registered, ACTION_PAYMENT

        //Getting rid of this table:
        String SQL_CREATE_PAYMENTS_TABLE = "CREATE TABLE " + PaymentEntry.TABLE_NAME + " ("
                + PaymentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PaymentEntry.COLUMN_PAYMENT_TIMESTAMP_CREATED + " INTEGER NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_TIMESTAMP_UPDATED + " INTEGER, "
                + PaymentEntry.COLUMN_PAYMENT_EMPLOYEEIDS + " TEXT NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_PAID + " INTEGER NOT NULL DEFAULT 1, " //getting value PAID when all employees got their money
                + PaymentEntry.COLUMN_PAYMENT_PAYMENT_DATE + " TEXT NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_REGISTERIDS + " TEXT NOT NULL, "
                + PaymentEntry.COLUMN_PAYMENT_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_EMPLOYEES_TABLE);
        db.execSQL(SQL_CREATE_REGISTER_TABLE);
        // TODO remove creation of payments table, everything is gonna be in the REGISTER table: db.execSQL(SQL_CREATE_PAYMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
