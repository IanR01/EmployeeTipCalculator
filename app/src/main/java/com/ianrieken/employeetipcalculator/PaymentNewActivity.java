package com.ianrieken.employeetipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class PaymentNewActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public final static String DATEPICKER_TAG = "newPaymentDatePicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_new);

        final ArrayList<Payment> payments = new ArrayList<Payment>();
        payments.add(new Payment(true, "Ian", "Subtitle", 12.50));
        payments.add(new Payment(false, "Rishi", "Subtitle 2", 2.35));

        final PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentNewActivity.this, payments);
        ListView paymentListView = (ListView) findViewById(R.id.payment_new_listview);
        paymentListView.setAdapter(paymentAdapter);

        LinearLayout dateToPickerLayout = (LinearLayout) findViewById(R.id.payment_new_date_to);
        dateToPickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employee_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                Log.v(LOG_TAG, "Item in menu selected: " + item.getItemId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
