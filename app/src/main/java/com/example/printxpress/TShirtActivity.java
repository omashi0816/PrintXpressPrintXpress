package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TShirtActivity extends AppCompatActivity {

    ImageView img1, img2, img3;
    TextView name1, name2, name3, price1, price2, price3, desc1, desc2, desc3;
    LinearLayout tshirt_item1, tshirt_item2, tshirt_item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tshirt);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);

        price1 = findViewById(R.id.price1);
        price2 = findViewById(R.id.price2);
        price3 = findViewById(R.id.price3);

        desc1 = findViewById(R.id.desc1);
        desc2 = findViewById(R.id.desc2);
        desc3 = findViewById(R.id.desc3);

        tshirt_item1 = findViewById(R.id.tshirt_item1);
        tshirt_item2 = findViewById(R.id.tshirt_item2);
        tshirt_item3 = findViewById(R.id.tshirt_item3);

        loadTShirts();
        setupClicks();
    }

    private void loadTShirts() {
        // වෙන් වෙන් පින්තූර ලබා දීම
        if (img1 != null) img1.setImageResource(R.drawable.tshirt1);
        name1.setText("Classic Cotton T-Shirt");
        price1.setText("Price: Rs. 1200");
        desc1.setText("100% cotton comfortable wear.");

        if (img2 != null) img2.setImageResource(R.drawable.tshirt2);
        name2.setText("Custom Print T-Shirt");
        price2.setText("Price: Rs. 1500");
        desc2.setText("Upload your design and print.");

        if (img3 != null) img3.setImageResource(R.drawable.tshirt3);
        name3.setText("Oversize Street T-Shirt");
        price3.setText("Price: Rs. 1800");
        desc3.setText("Modern street style oversized fit.");
    }

    private void setupClicks() {
        if (tshirt_item1 != null) {
            tshirt_item1.setOnClickListener(v -> openDetail("Classic Cotton T-Shirt", "1200", R.drawable.tshirt1));
        }
        if (tshirt_item2 != null) {
            tshirt_item2.setOnClickListener(v -> openDetail("Custom Print T-Shirt", "1500", R.drawable.tshirt2));
        }
        if (tshirt_item3 != null) {
            tshirt_item3.setOnClickListener(v -> openDetail("Oversize Street T-Shirt", "1800", R.drawable.tshirt3));
        }
    }

    private void openDetail(String name, String price, int imageResId) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productName", name);
        intent.putExtra("productPrice", price);
        intent.putExtra("productImage", imageResId);
        startActivity(intent);
    }
}
