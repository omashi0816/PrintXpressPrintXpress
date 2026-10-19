package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PosterActivity extends AppCompatActivity {

    ImageView img1, img2, img3;
    TextView name1, name2, name3, price1, price2, price3, desc1, desc2, desc3;
    LinearLayout poster_item1, poster_item2, poster_item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

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

        poster_item1 = findViewById(R.id.poster_item1);
        poster_item2 = findViewById(R.id.poster_item2);
        poster_item3 = findViewById(R.id.poster_item3);

        loadPosters();
        setupClicks();
    }

    private void loadPosters() {
        if (img1 != null) img1.setImageResource(R.drawable.ps1);
        name1.setText("A3 Movie Poster");
        price1.setText("Price: Rs. 1000");
        desc1.setText("High quality glossy movie poster");

        if (img2 != null) img2.setImageResource(R.drawable.ps2);
        name2.setText("Event Poster");
        price2.setText("Price: Rs. 1300");
        desc2.setText("Perfect for advertising events");

        if (img3 != null) img3.setImageResource(R.drawable.ps3);
        name3.setText("Custom Poster");
        price3.setText("Price: Rs. 1600");
        desc3.setText("Your own design printed professionally");
    }

    private void setupClicks() {
        if (poster_item1 != null) {
            poster_item1.setOnClickListener(v -> openDetail("A3 Movie Poster", "1000", R.drawable.ps1));
        }
        if (poster_item2 != null) {
            poster_item2.setOnClickListener(v -> openDetail("Event Poster", "1300", R.drawable.ps2));
        }
        if (poster_item3 != null) {
            poster_item3.setOnClickListener(v -> openDetail("Custom Poster", "1600", R.drawable.ps3));
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
