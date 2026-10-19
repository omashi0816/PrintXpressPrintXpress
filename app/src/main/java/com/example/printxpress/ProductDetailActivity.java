package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtProductName, txtDescription, txtPrice;
    ImageView imgProduct;
    Button btnOrderNow;
    String productName, productPrice, designUrl;
    int productImageRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtProductName = findViewById(R.id.txtProductName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        imgProduct = findViewById(R.id.imgProduct);
        btnOrderNow = findViewById(R.id.btnOrderNow);

        // Get data from intent
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getStringExtra("productPrice");
        productImageRes = getIntent().getIntExtra("productImage", -1);
        designUrl = getIntent().getStringExtra("designUrl"); // ඇල්බමයෙන් එන විට මෙය භාවිතා වේ

        if (productName != null) txtProductName.setText(productName);
        if (productPrice != null) txtPrice.setText("Price per unit: Rs. " + productPrice);
        
        // පින්තූරය පෙන්වීම (URL එකක් ඇත්නම් එය භාවිතා කරයි, නැතිනම් Resource ID එක භාවිතා කරයි)
        if (designUrl != null && !designUrl.isEmpty()) {
            Glide.with(this).load(designUrl).into(imgProduct);
        } else if (productImageRes != -1) {
            imgProduct.setImageResource(productImageRes);
        } else {
            imgProduct.setImageResource(R.drawable.logonew);
        }

        txtDescription.setText("High quality printing service with premium materials and fast delivery. Perfect for your personal or business needs.");

        btnOrderNow.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
            intent.putExtra("productName", productName);
            intent.putExtra("productPrice", productPrice);
            intent.putExtra("productImage", productImageRes);
            intent.putExtra("designUrl", designUrl);
            startActivity(intent);
        });
    }
}
