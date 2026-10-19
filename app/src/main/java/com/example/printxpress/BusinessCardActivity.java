package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BusinessCardActivity extends AppCompatActivity {

    ImageView img1, img2, img3;
    TextView name1, name2, name3;
    TextView price1, price2, price3;
    TextView desc1, desc2, desc3;
    LinearLayout card_item1, card_item2, card_item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

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

        card_item1 = findViewById(R.id.card_item1);
        card_item2 = findViewById(R.id.card_item2);
        card_item3 = findViewById(R.id.card_item3);

        loadCards();
        setupClicks();
    }

    private void loadCards() {
        if (img1 != null) img1.setImageResource(R.drawable.bussinescard1);
        if (name1 != null) name1.setText("Classic Matte Card");
        if (price1 != null) price1.setText("Price: Rs. 500");
        if (desc1 != null) desc1.setText("High quality matte finish.");

        if (img2 != null) img2.setImageResource(R.drawable.bussinescard2);
        if (name2 != null) name2.setText("Glossy Premium Card");
        if (price2 != null) price2.setText("Price: Rs. 800");
        if (desc2 != null) desc2.setText("Shinny professional look.");

        if (img3 != null) img3.setImageResource(R.drawable.bussinescard3);
        if (name3 != null) name3.setText("Eco-Friendly Card");
        if (price3 != null) price3.setText("Price: Rs. 600");
        if (desc3 != null) desc3.setText("Recycled paper material.");
    }

    private void setupClicks() {
        if (card_item1 != null) {
            card_item1.setOnClickListener(v -> openDetail("Classic Matte Card", "500", R.drawable.bussinescard1));
        }
        if (card_item2 != null) {
            card_item2.setOnClickListener(v -> openDetail("Glossy Premium Card", "800", R.drawable.bussinescard2));
        }
        if (card_item3 != null) {
            card_item3.setOnClickListener(v -> openDetail("Eco-Friendly Card", "600", R.drawable.bussinescard3));
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
