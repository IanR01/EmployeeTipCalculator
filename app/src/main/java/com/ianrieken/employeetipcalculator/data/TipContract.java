package com.ianrieken.employeetipcalculator.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by IanR8 on 30-Mar-17.
 */

public class TipContract {

    private TipContract() {}

    public static final String CONTENT_AUTHORITY = "com.ianrieken.employeetipcalculator";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EMPLOYEES = "employees";
    public static final String PATH_REGISTER = "register";
    public static final String PATH_PAYMENTS = "payments";

    public static class EmployeeEntry implements BaseColumns {
        public static final String TABLE_NAME = "employees";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_EMPLOYEE_NAME = "name";
        public static final String COLUMN_EMPLOYEE_BALANCE = "balance";
        public static final String COLUMN_EMPLOYEE_EARNED = "earned";
        public static final String COLUMN_EMPLOYEE_ACTIVE = "active";
        public static final String COLUMN_EMPLOYEE_MEMO = "memo";
        public static final String COLUMN_EMPLOYEE_REGDATE = "regdate";
        public static final String COLUMN_EMPLOYEE_COUNT = "count";

        public static final int EMPLOYEE_ACTIVE = 1;
        public static final int EMPLOYEE_INACTIVE = 2;

        public static final Uri EMPLOYEE_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EMPLOYEES);

        public static final String EMPLOYEE_CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;
        public static final String EMPLOYEE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;

    }

    public static class RegisterEntry implements BaseColumns {
        public static final String TABLE_NAME = "register";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_REGISTER_TIMESTAMP_CREATED = "timestamp_created";
        public static final String COLUMN_REGISTER_TIMESTAMP_UPDATED = "timestamp_updated";
        public static final String COLUMN_REGISTER_DATE = "date";
        public static final String COLUMN_REGISTER_AMOUNT = "amount";
        public static final String COLUMN_REGISTER_EMPLOYEEIDS = "employeeids";
        public static final String COLUMN_REGISTER_NAMES = "names"; //TODO remove names from table; only save ID's and retrieve names from database
        public static final String COLUMN_REGISTER_NREMPLOYEES = "nremployees";
        public static final String COLUMN_REGISTER_DISTRIBUTION = "distribution";
        public static final String COLUMN_REGISTER_HOURS = "hours";
        public static final String COLUMN_REGISTER_PAID = "paid"; //TODO: maybe remove paid from table? Can be calculated from distribution and hours. Or use in case of ACTION_PAYMENT instead
        public static final String COLUMN_REGISTER_ACTION = "action";
        public static final String COLUMN_REGISTER_PAYMENTID = "paymentid";
        public static final String COLUMN_REGISTER_REGISTERIDS = "registerids";

        public static final int REGISTER_ACTION_TIP = 1;
        public static final int REGISTER_ACTION_PAYMENT = 2;

        public static final int REGISTER_PAYMENT_PENDING = 1;
        public static final int REGISTER_PAYMENT_COMPLETED = 2;
        public static final int REGISTER_PAYMENT_ALL_PAID = 3;

        public static final Uri REGISTER_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REGISTER);

        public static final String REGISTER_CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGISTER;
        public static final String REGISTER_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGISTER;
    }

    public static class PaymentEntry implements BaseColumns {
        public static final String TABLE_NAME = "payments";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PAYMENT_TIMESTAMP_CREATED = "timestamp_created";
        public static final String COLUMN_PAYMENT_TIMESTAMP_UPDATED = "timestamp_updated";
        public static final String COLUMN_PAYMENT_EMPLOYEEIDS = "employeeids";
        public static final String COLUMN_PAYMENT_PAID = "paid";
        public static final String COLUMN_PAYMENT_PAYMENT_DATE = "date";
        public static final String COLUMN_PAYMENT_REGISTERIDS = "registerids";
        public static final String COLUMN_PAYMENT_DESCRIPTION = "description";

        public static final int PAYMENT_PENDING = 1;
        public static final int PAYMENT_COMPLETED = 2;

        public static final Uri PAYMENTS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PAYMENTS);

        public static final String PAYMENTS_CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENTS;
        public static final String PAYMENTS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENTS;
    }
}
