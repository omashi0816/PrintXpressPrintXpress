package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView txtUserName, txtUserEmail, txtOrderHistory, txtPaymentInfo;
    Button btnEditProfile, btnLogout, btnProfileNotifications, btnProfileHelp;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtOrderHistory = findViewById(R.id.txtOrderHistory);
        txtPaymentInfo = findViewById(R.id.txtPaymentInfo);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnProfileNotifications = findViewById(R.id.btnProfileNotifications);
        btnProfileHelp = findViewById(R.id.btnProfileHelp);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // ඔබගේ නිවැරදි Database URL එක මෙහි භාවිතා කරමු
        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();

        if (user != null) {
            String userId = user.getUid();
            txtUserEmail.setText(user.getEmail());

            // 1. Fetch user name and detailed payment profile
            mDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        txtUserName.setText(name != null ? name : "User Name");

                        // Detailed Payment Profile (Item, Price, Design URL)
                        if (snapshot.hasChild("paymentProfile")) {
                            DataSnapshot paySnap = snapshot.child("paymentProfile");
                            String lastMethod = paySnap.child("lastPaymentMethod").getValue(String.class);
                            String lastItem = paySnap.child("lastItem").getValue(String.class);
                            String lastAmount = paySnap.child("lastAmount").getValue(String.class);
                            String lastDesign = paySnap.child("lastDesignUrl").getValue(String.class);
                            
                            StringBuilder payDetails = new StringBuilder();
                            payDetails.append("Last Method: ").append(lastMethod != null ? lastMethod : "N/A");
                            
                            if (lastItem != null) payDetails.append("\nLast Item: ").append(lastItem);
                            if (lastAmount != null) payDetails.append("\nAmount: Rs. ").append(lastAmount);
                            
                            // Design තොරතුරු පෙන්වීම
                            if (lastDesign != null) {
                                if (lastDesign.startsWith("http")) {
                                    payDetails.append("\nDesign: [Custom Uploaded]");
                                } else {
                                    payDetails.append("\nDesign: ").append(lastDesign);
                                }
                            }
                            
                            txtPaymentInfo.setText(payDetails.toString());
                        } else {
                            txtPaymentInfo.setText("No saved payment history");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });

            // 2. Fetch Order History
            mDatabase.child("Orders").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StringBuilder history = new StringBuilder();
                    if (snapshot.exists()) {
                        for (DataSnapshot orderSnap : snapshot.getChildren()) {
                            String pName = orderSnap.child("productName").getValue(String.class);
                            String status = orderSnap.child("status").getValue(String.class);
                            String amount = orderSnap.child("totalAmount").getValue(String.class);
                            
                            history.append("• ").append(pName)
                                   .append("\n  Amount: Rs. ").append(amount != null ? amount : "0")
                                   .append("\n  Status: ").append(status)
                                   .append("\n\n");
                        }
                        txtOrderHistory.setText(history.toString());
                    } else {
                        txtOrderHistory.setText("No orders yet.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    txtOrderHistory.setText("Error loading orders.");
                }
            });
        }

        // Setup Click Listeners
        if (btnProfileNotifications != null) {
            btnProfileNotifications.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, NotificationActivity.class)));
        }
        if (btnProfileHelp != null) {
            btnProfileHelp.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HelpActivity.class)));
        }

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
