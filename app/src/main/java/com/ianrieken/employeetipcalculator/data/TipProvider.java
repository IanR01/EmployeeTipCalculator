package com.ianrieken.employeetipcalculator.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.ianrieken.employeetipcalculator.data.TipContract.EmployeeEntry;

/**
 * Created by IanR8 on 30-Mar-17.
 */

public class TipProvider extends ContentProvider {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private TipDbHelper mDbHelper;

    private static final int EMPLOYEES = 100;
    private static final int EMPLOYEE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(TipContract.CONTENT_AUTHORITY, TipContract.PATH_EMPLOYEES, EMPLOYEES);
        sUriMatcher.addURI(TipContract.CONTENT_AUTHORITY, TipContract.PATH_EMPLOYEES + "/#", EMPLOYEE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TipDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match) {
            case EMPLOYEES:
                cursor = db.query(EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EMPLOYEE_ID:
                selection = EmployeeEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = db.query(EmployeeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                return EmployeeEntry.EMPLOYEE_CONTENT_LIST_TYPE;
            case EMPLOYEE_ID:
                return EmployeeEntry.EMPLOYEE_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                return insertEmployee(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertEmployee(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "TEST insert employee");
        //TODO insert check functions, see PetProvider example

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(EmployeeEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EMPLOYEES:
                rowsDeleted = db.delete(EmployeeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                selection = EmployeeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = db.delete(EmployeeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                return updateEmployee(uri, values, selection, selectionArgs);
            case EMPLOYEE_ID:
                selection = EmployeeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateEmployee(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateEmployee(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO check whether inserted values are valid, see pet example

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(EmployeeEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
