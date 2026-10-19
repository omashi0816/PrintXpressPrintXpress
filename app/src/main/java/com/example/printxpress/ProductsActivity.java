package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductsActivity extends AppCompatActivity {

    TextView txtCategory;
    LinearLayout product1, product2, product3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        txtCategory = findViewById(R.id.txtCategory);
        product1 = findViewById(R.id.product1);
        product2 = findViewById(R.id.product2);
        product3 = findViewById(R.id.product3);

        String category = getIntent().getStringExtra("category");
        if (category != null) {
            txtCategory.setText(category);
        }

        product1.setOnClickListener(v -> openProductDetail(category + " Premium"));
        product2.setOnClickListener(v -> openProductDetail(category + " Standard"));
        product3.setOnClickListener(v -> openProductDetail(category + " Custom"));
    }

    private void openProductDetail(String productName) {
        Intent intent = new Intent(ProductsActivity.this, ProductDetailActivity.class);
        intent.putExtra("productName", productName);
        startActivity(intent);
    }
}
