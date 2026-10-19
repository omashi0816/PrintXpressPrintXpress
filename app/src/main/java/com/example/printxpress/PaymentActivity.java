package com.example.printxpress;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    RadioGroup paymentGroup;
    View cardDetailsLayout, bankDetailsLayout, codLayout;
    EditText cardNumber, cardName, expDate, cvv;
    Button payBtn;
    TextView summaryProductName, summaryQuantity, summaryTotal, summaryDelivery;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    String orderId, productName, quantity, totalAmount, deliveryMethod, designUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        paymentGroup = findViewById(R.id.paymentGroup);
        cardDetailsLayout = findViewById(R.id.cardDetailsLayout);
        bankDetailsLayout = findViewById(R.id.bankDetailsLayout);
        codLayout = findViewById(R.id.codLayout);
        cardNumber = findViewById(R.id.cardNumber);
        cardName = findViewById(R.id.cardName);
        expDate = findViewById(R.id.expDate);
        cvv = findViewById(R.id.cvv);
        payBtn = findViewById(R.id.payBtn);
        summaryProductName = findViewById(R.id.summaryProductName);
        summaryQuantity = findViewById(R.id.summaryQuantity);
        summaryTotal = findViewById(R.id.summaryTotal);
        summaryDelivery = findViewById(R.id.summaryDelivery);

        // Get Data from Intent
        productName = getIntent().getStringExtra("productName");
        quantity = getIntent().getStringExtra("quantity");
        totalAmount = getIntent().getStringExtra("totalAmount");
        orderId = getIntent().getStringExtra("orderId");
        deliveryMethod = getIntent().getStringExtra("deliveryMethod");
        designUrl = getIntent().getStringExtra("designUrl"); // Design URL එක ලබා ගැනීම

        // Set UI Text
        summaryProductName.setText("Product: " + productName);
        summaryQuantity.setText("Quantity: " + quantity);
        summaryTotal.setText("Total: Rs. " + totalAmount);
        summaryDelivery.setText("Delivery: " + (deliveryMethod != null ? deliveryMethod : "Store Pickup"));

        paymentGroup.setOnCheckedChangeListener((group, checkedId) -> {
            cardDetailsLayout.setVisibility(checkedId == R.id.rCard ? View.VISIBLE : View.GONE);
            bankDetailsLayout.setVisibility(checkedId == R.id.rBank ? View.VISIBLE : View.GONE);
            codLayout.setVisibility(checkedId == R.id.rCOD ? View.VISIBLE : View.GONE);
        });

        payBtn.setOnClickListener(v -> {
            int selectedId = paymentGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            String method = ((RadioButton) findViewById(selectedId)).getText().toString();
            String userId = mAuth.getUid();

            if (selectedId == R.id.rCard && cardNumber.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter card details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (orderId != null && userId != null) {
                // Update the main Orders node
                DatabaseReference orderRef = mDatabase.child("Orders").child(orderId);
                orderRef.child("status").setValue("Paid - " + method);
                orderRef.child("paymentMethod").setValue(method);

                // Update user's last payment profile (Design එකත් සමඟ)
                Map<String, Object> paymentProfile = new HashMap<>();
                paymentProfile.put("lastPaymentMethod", method);
                paymentProfile.put("lastItem", productName);
                paymentProfile.put("lastAmount", totalAmount);
                paymentProfile.put("lastDesignUrl", designUrl != null ? designUrl : "No Design"); // Design එක save කිරීම

                mDatabase.child("Users").child(userId).child("paymentProfile").setValue(paymentProfile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Order Confirmed Successfully!", Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Error: Missing Order or User ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
