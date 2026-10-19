package com.example.printxpress;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {

    TextView txtNotification1, txtDeliveryDetails, txtNotification2, txtNotification3;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize Views
        txtNotification1 = findViewById(R.id.txtNotification1);
        txtDeliveryDetails = findViewById(R.id.txtDeliveryDetails);
        txtNotification2 = findViewById(R.id.txtNotification2);
        txtNotification3 = findViewById(R.id.txtNotification3);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();

        // Fetch latest order for notification
        loadLatestOrderNotification();
    }

    private void loadLatestOrderNotification() {
        String uId = mAuth.getUid();
        if (uId == null) {
            txtNotification1.setText("Please log in to see notifications.");
            return;
        }

        // Search for the latest order of this user
        mDatabase.child("Orders").orderByChild("userId").equalTo(uId).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String productName = child.child("productName").getValue(String.class);
                                String status = child.child("status").getValue(String.class);
                                String delivery = child.child("delivery").getValue(String.class);
                                String address = child.child("deliveryAddress").getValue(String.class);

                                // Set dynamic notification text
                                txtNotification1.setText("✅ Order Successful: " + (productName != null ? productName : "New Order"));
                                
                                String deliveryInfo = "Current Status: " + (status != null ? status : "Processing");
                                if ("Home Delivery".equals(delivery)) {
                                    deliveryInfo += "\n🚚 Delivering to: " + (address != null ? address : "Registered Address");
                                } else {
                                    deliveryInfo += "\n🏠 Method: Store Pickup";
                                }
                                txtDeliveryDetails.setText(deliveryInfo);
                            }
                        } else {
                            txtNotification1.setText("No recent orders found.");
                            txtDeliveryDetails.setText("Place your first order to get real-time updates!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        txtNotification1.setText("Error loading notifications.");
                    }
                });
    }
}
