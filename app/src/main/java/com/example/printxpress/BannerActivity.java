package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BannerActivity extends AppCompatActivity {

    ImageView img1;
    TextView name1, price1, desc1;
    CardView banner_item1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        img1 = findViewById(R.id.img1);
        name1 = findViewById(R.id.name1);
        price1 = findViewById(R.id.price1);
        desc1 = findViewById(R.id.desc1);
        banner_item1 = findViewById(R.id.banner_item1);

        loadData();
        setupClicks();
    }

    private void loadData() {
        if (img1 != null) img1.setImageResource(R.drawable.banner);
        if (name1 != null) name1.setText("Event Banner");
        if (price1 != null) price1.setText("Price: Rs. 2500");
        if (desc1 != null) desc1.setText("Large Size | Outdoor Use");
    }

    private void setupClicks() {
        if (banner_item1 != null) {
            banner_item1.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProductDetailActivity.class);
                intent.putExtra("productName", "Event Banner");
                intent.putExtra("productPrice", "2500");
                intent.putExtra("productImage", R.drawable.banner);
                startActivity(intent);
            });
        }
    }
}
