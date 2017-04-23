package com.ianrieken.employeetipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PaymentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        final ArrayList<Payment> createPayments = new ArrayList<Payment>();
        createPayments.add(new Payment(true, "Ian", "Subtitle", 12.50));
        createPayments.add(new Payment(false, "Rishi", "Subtitle 2", 2.35));

        final PaymentAdapter createPaymentsAdapter = new PaymentAdapter(PaymentDetailActivity.this, createPayments);

        ListView createPaymentsListView = (ListView) findViewById(R.id.payment_detail_listview);

        createPaymentsListView.setAdapter(createPaymentsAdapter);

        //TODO change placeholder title
        setTitle("Februari");
    }
}
