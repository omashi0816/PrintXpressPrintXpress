package com.example.printxpress;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderTrackingActivity extends AppCompatActivity {

    TextView txtProductName, txtOrderStatus, txtEstimatedTime, txtDeliveryInfo;
    TextView stepConfirmed, stepProcessing, stepPrinting, stepReady, stepDelivered;
    Button btnCancelOrder;
    DatabaseReference mDatabase;
    ValueEventListener trackingListener;
    String currentOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Initialize Views
        txtProductName = findViewById(R.id.txtProductName);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtEstimatedTime = findViewById(R.id.txtEstimatedTime);
        txtDeliveryInfo = findViewById(R.id.txtDeliveryInfo); // We'll add this to XML
        
        stepConfirmed = findViewById(R.id.stepConfirmed);
        stepProcessing = findViewById(R.id.stepProcessing);
        stepPrinting = findViewById(R.id.stepPrinting);
        stepReady = findViewById(R.id.stepReady);
        stepDelivered = findViewById(R.id.stepDelivered);
        
        btnCancelOrder = findViewById(R.id.btnCancelOrder);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String productName = getIntent().getStringExtra("productName");
        currentOrderId = getIntent().getStringExtra("orderId");

        if (productName != null) txtProductName.setText(productName);

        if (currentOrderId != null) {
            trackOrder();
        } else {
            findLatestOrder();
        }

        btnCancelOrder.setOnClickListener(v -> {
            if (currentOrderId != null) {
                mDatabase.child("Orders").child(currentOrderId).child("status").setValue("Cancelled")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();
                            btnCancelOrder.setVisibility(View.GONE);
                        });
            }
        });
    }

    private void findLatestOrder() {
        String uId = FirebaseAuth.getInstance().getUid();
        if (uId == null) return;

        mDatabase.child("Orders").orderByChild("userId").equalTo(uId).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                currentOrderId = child.getKey();
                                trackOrder();
                            }
                        }
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void trackOrder() {
        if (currentOrderId == null) return;

        trackingListener = mDatabase.child("Orders").child(currentOrderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    String deliveryMethod = snapshot.child("deliveryMethod").getValue(String.class);
                    String address = snapshot.child("deliveryAddress").getValue(String.class);
                    
                    if (status != null) {
                        txtOrderStatus.setText("Status: " + status);
                        updateProgressUI(status);
                    }

                    // Show Delivery Details
                    if (txtDeliveryInfo != null && deliveryMethod != null) {
                        if (deliveryMethod.equals("Home Delivery")) {
                            txtDeliveryInfo.setText("🚚 Delivery to: " + (address != null ? address : "Address Loading..."));
                        } else {
                            txtDeliveryInfo.setText("🏠 Method: Store Pickup");
                        }
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateProgressUI(String status) {
        int gray = 0xFF999999;
        int green = 0xFF43A047;
        int blue = 0xFF1565C0;

        txtOrderStatus.setTextColor(blue);
        stepConfirmed.setTextColor(gray); stepConfirmed.setText("⬜ Order Confirmed");
        stepProcessing.setTextColor(gray); stepProcessing.setText("⬜ Processing");
        stepPrinting.setTextColor(gray); stepPrinting.setText("⬜ Printing");
        stepReady.setTextColor(gray); stepReady.setText("⬜ Ready for Pickup");
        stepDelivered.setTextColor(gray); stepDelivered.setText("⬜ Delivered");

        if (status.contains("Pending") || status.contains("Paid") || status.equals("Confirmed") || status.equals("Processing")) {
            btnCancelOrder.setVisibility(View.VISIBLE);
        } else {
            btnCancelOrder.setVisibility(View.GONE);
        }

        if (status.contains("Pending") || status.contains("Paid") || status.equals("Confirmed") || 
            status.equals("Processing") || status.equals("Printing") || status.equals("Ready for Pickup") || status.equals("Delivered")) {
            stepConfirmed.setText("✔ Order Confirmed");
            stepConfirmed.setTextColor(green);
        }

        if (status.equals("Processing") || status.equals("Printing") || status.equals("Ready for Pickup") || status.equals("Delivered")) {
            stepProcessing.setText("✔ Processing");
            stepProcessing.setTextColor(green);
            txtEstimatedTime.setText("Estimated Time: 2 Days");
        }

        if (status.equals("Printing") || status.equals("Ready for Pickup") || status.equals("Delivered")) {
            stepPrinting.setText("✔ Printing");
            stepPrinting.setTextColor(green);
            txtEstimatedTime.setText("Estimated Time: 1 Day");
        }

        if (status.equals("Ready for Pickup") || status.equals("Delivered")) {
            stepReady.setText("✔ Ready for Pickup");
            stepReady.setTextColor(green);
            txtEstimatedTime.setText("Your order is ready!");
        }

        if (status.equals("Delivered")) {
            stepDelivered.setText("✔ Delivered");
            stepDelivered.setTextColor(green);
            txtEstimatedTime.setText("Completed");
        }
        
        if (status.equals("Cancelled")) {
            txtOrderStatus.setTextColor(0xFFD32F2F);
            txtEstimatedTime.setText("Order Cancelled");
            btnCancelOrder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentOrderId != null && trackingListener != null) {
            mDatabase.child("Orders").child(currentOrderId).removeEventListener(trackingListener);
        }
    }
}
