package com.example.printxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class OrderActivity extends AppCompatActivity {

    TextView txtSelectedProduct, txtTotalPrice;
    EditText edtQuantity, edtCustomText, edtDeliveryAddress, edtContactNumber;
    Spinner spinnerDelivery, spinnerMaterial, spinnerProductType;
    Button btnPlaceOrder, btnUpload, btnSelectFromAlbum;
    ImageView imgPreview;
    LinearLayout layoutDeliveryDetails, layoutSelectProduct;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;

    Uri imageUri;
    int basePrice = 500;
    String productName;
    int totalAmount = 0;
    int selectedSampleImage = -1;
    String existingDesignUrl = null;

    // මෙම ක්‍රමය මඟින් දුරකථනයේ ඇති ඕනෑම තැනකින් පින්තූර තෝරාගත හැකිය (Files/Downloads ඇතුළුව)
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    imgPreview.setImageURI(uri);
                    selectedSampleImage = -1;
                    existingDesignUrl = null;
                    Toast.makeText(this, "Design Selected!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize Views
        txtSelectedProduct = findViewById(R.id.txtSelectedProduct);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtCustomText = findViewById(R.id.edtCustomText);
        layoutDeliveryDetails = findViewById(R.id.layoutDeliveryDetails);
        layoutSelectProduct = findViewById(R.id.layoutSelectProduct);
        edtDeliveryAddress = findViewById(R.id.edtDeliveryAddress);
        edtContactNumber = findViewById(R.id.edtContactNumber);
        spinnerDelivery = findViewById(R.id.spinnerDelivery);
        spinnerMaterial = findViewById(R.id.spinnerMaterial);
        spinnerProductType = findViewById(R.id.spinnerProductType);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnUpload = findViewById(R.id.btnUpload);
        btnSelectFromAlbum = findViewById(R.id.btnSelectFromAlbum);
        imgPreview = findViewById(R.id.imgPreview);

        // Intent Data
        productName = getIntent().getStringExtra("productName");
        String priceStr = getIntent().getStringExtra("productPrice");
        selectedSampleImage = getIntent().getIntExtra("productImage", -1);
        existingDesignUrl = getIntent().getStringExtra("designUrl");
        
        if (priceStr != null) {
            try { basePrice = Integer.parseInt(priceStr); } catch (Exception e) { basePrice = 500; }
        }

        if ("Custom Artwork Order".equals(productName) || productName == null) {
            layoutSelectProduct.setVisibility(View.VISIBLE);
            String[] products = {"Business Card", "Flyer", "Poster", "T-Shirt", "Mug"};
            spinnerProductType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, products));
            spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    productName = "Custom " + products[position];
                    txtSelectedProduct.setText(productName);
                }
                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });
        } else {
            txtSelectedProduct.setText(productName);
        }

        if (selectedSampleImage != -1) {
            imgPreview.setImageResource(selectedSampleImage);
        } else if (existingDesignUrl != null) {
            com.bumptech.glide.Glide.with(this).load(existingDesignUrl).into(imgPreview);
        }

        setupControls();
    }

    private void setupControls() {
        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calculatePrice(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        String[] deliveryOptions = {"Home Delivery", "Store Pickup"};
        spinnerDelivery.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deliveryOptions));
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layoutDeliveryDetails.setVisibility(deliveryOptions[position].equals("Home Delivery") ? View.VISIBLE : View.GONE);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerMaterial.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Standard", "Premium", "Glossy"}));

        // පින්තූරය තේරීමට - මෙය එබූ විට පද්ධතියේ ගොනු (Files) විවෘත වේ
        btnUpload.setOnClickListener(v -> mGetContent.launch("image/*"));

        // Image Preview මත click කළ විටද පින්තූරය තේරීමට ඉඩ දෙමු
        imgPreview.setOnClickListener(v -> mGetContent.launch("image/*"));

        if (btnSelectFromAlbum != null) {
            btnSelectFromAlbum.setOnClickListener(v -> {
                Intent intent = new Intent(this, AlbumActivity.class);
                startActivity(intent);
                finish();
            });
        }

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateForm()) {
                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri != null) {
                    uploadImageToStorage();
                } else {
                    placeOrderInDatabase(existingDesignUrl);
                }
            }
        });
    }

    private void calculatePrice() {
        String qStr = edtQuantity.getText().toString().trim();
        if (!qStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(qStr);
                totalAmount = quantity * basePrice;
                txtTotalPrice.setText("Total Price : Rs. " + totalAmount);
            } catch (Exception e) { txtTotalPrice.setText("Total Price : Rs. 0"); }
        }
    }

    private void uploadImageToStorage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Submitting Order...");
        pd.setMessage("Uploading design to Firebase Storage...");
        pd.setCancelable(false);
        pd.show();

        String fileName = "designs/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference fileRef = mStorageRef.child(fileName);

        fileRef.putFile(imageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) throw task.getException();
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            pd.dismiss();
            if (task.isSuccessful()) {
                placeOrderInDatabase(task.getResult().toString());
            } else {
                Toast.makeText(this, "Upload Failed! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrderInDatabase(String designUrl) {
        String orderId = mDatabase.child("Orders").push().getKey();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String userId = mAuth.getUid();

        String finalDesign = designUrl != null ? designUrl : (selectedSampleImage != -1 ? "Sample Design" : "No Design");

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderId", orderId);
        orderMap.put("userId", userId);
        orderMap.put("productName", productName);
        orderMap.put("quantity", edtQuantity.getText().toString());
        orderMap.put("totalAmount", String.valueOf(totalAmount));
        orderMap.put("material", spinnerMaterial.getSelectedItem().toString());
        orderMap.put("delivery", spinnerDelivery.getSelectedItem().toString());
        orderMap.put("customText", edtCustomText.getText().toString().trim());
        orderMap.put("designUrl", finalDesign);
        orderMap.put("status", "Pending Payment");
        orderMap.put("orderDate", date);
        
        if (spinnerDelivery.getSelectedItem().toString().equals("Home Delivery")) {
            orderMap.put("deliveryAddress", edtDeliveryAddress.getText().toString().trim());
            orderMap.put("contactNumber", edtContactNumber.getText().toString().trim());
        }

        if (orderId != null) {
            mDatabase.child("Orders").child(orderId).setValue(orderMap).addOnSuccessListener(aVoid -> {
                // සැබෑ පින්තූරයක් නම් ඇල්බමයට එක් කරමු
                if (designUrl != null && !designUrl.startsWith("Sample")) {
                    mDatabase.child("Users").child(userId).child("savedDesigns").push().setValue(designUrl);
                }
                Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("productName", productName);
                intent.putExtra("quantity", edtQuantity.getText().toString());
                intent.putExtra("totalAmount", String.valueOf(totalAmount));
                intent.putExtra("designUrl", finalDesign);
                intent.putExtra("deliveryMethod", (String) orderMap.get("delivery"));
                startActivity(intent);
                finish();
            });
        }
    }

    private boolean validateForm() {
        if (edtQuantity.getText().toString().trim().isEmpty()) {
            edtQuantity.setError("Enter quantity");
            return false;
        }
        return true;
    }
}
