package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FlyerActivity extends AppCompatActivity {

    ImageView img1, img2, img3;
    TextView name1, name2, name3, price1, price2, price3, desc1, desc2, desc3;
    LinearLayout flyer_item1, flyer_item2, flyer_item3;
    CardView cardCustomFlyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyer);

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

        flyer_item1 = findViewById(R.id.flyer_item1);
        flyer_item2 = findViewById(R.id.flyer_item2);
        flyer_item3 = findViewById(R.id.flyer_item3);
        cardCustomFlyer = findViewById(R.id.cardCustomFlyer);

        loadData();
        setupClicks();
    }

    private void loadData() {
        if (img1 != null) img1.setImageResource(R.drawable.flyer1);
        if (name1 != null) name1.setText("Business Flyer");
        if (price1 != null) price1.setText("Price: Rs. 500");
        if (desc1 != null) desc1.setText("Standard marketing flyer");

        if (img2 != null) img2.setImageResource(R.drawable.flyer2);
        if (name2 != null) name2.setText("Event Flyer");
        if (price2 != null) price2.setText("Price: Rs. 700");
        if (desc2 != null) desc2.setText("Perfect for advertising");

        if (img3 != null) img3.setImageResource(R.drawable.flyer3);
        if (name3 != null) name3.setText("Premium Flyer");
        if (price3 != null) price3.setText("Price: Rs. 900");
        if (desc3 != null) desc3.setText("Glossy professional look");
    }

    private void setupClicks() {
        if (flyer_item1 != null) {
            flyer_item1.setOnClickListener(v -> openDetail("Business Flyer", "500", R.drawable.flyer1));
        }
        if (flyer_item2 != null) {
            flyer_item2.setOnClickListener(v -> openDetail("Event Flyer", "700", R.drawable.flyer2));
        }
        if (flyer_item3 != null) {
            flyer_item3.setOnClickListener(v -> openDetail("Premium Flyer", "900", R.drawable.flyer3));
        }
        
        // Custom Flyer Upload Button Click
        if (cardCustomFlyer != null) {
            cardCustomFlyer.setOnClickListener(v -> {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("productName", "Custom Design Flyer");
                intent.putExtra("productPrice", "600"); // Base price for custom design
                startActivity(intent);
            });
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
