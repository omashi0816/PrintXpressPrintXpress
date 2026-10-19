package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MugActivity extends AppCompatActivity {

    ImageView img1, img2, img3;
    TextView name1, name2, name3, price1, price2, price3, desc1, desc2, desc3;
    LinearLayout mug_item1, mug_item2, mug_item3;
    CardView cardCustomMug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mug);

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

        mug_item1 = findViewById(R.id.mug_item1);
        mug_item2 = findViewById(R.id.mug_item2);
        mug_item3 = findViewById(R.id.mug_item3);
        cardCustomMug = findViewById(R.id.cardCustomMug);

        loadMugs();
        setupClicks();
    }

    private void loadMugs() {
        if (img1 != null) img1.setImageResource(R.drawable.mg1);
        name1.setText("White Ceramic Mug");
        price1.setText("Price: Rs. 800");
        desc1.setText("Simple high quality ceramic mug.");

        if (img2 != null) img2.setImageResource(R.drawable.mg2);
        name2.setText("Magic Heat Mug");
        price2.setText("Price: Rs. 1200");
        desc2.setText("Color changes with heat.");

        if (img3 != null) img3.setImageResource(R.drawable.mg3);
        name3.setText("Photo Custom Mug");
        price3.setText("Price: Rs. 1500");
        desc3.setText("Print your photo on mug.");
    }

    private void setupClicks() {
        if (mug_item1 != null) {
            mug_item1.setOnClickListener(v -> openDetail(name1.getText().toString(), "800", R.drawable.mg1));
        }
        if (mug_item2 != null) {
            mug_item2.setOnClickListener(v -> openDetail(name2.getText().toString(), "1200", R.drawable.mg2));
        }
        if (mug_item3 != null) {
            mug_item3.setOnClickListener(v -> openDetail(name3.getText().toString(), "1500", R.drawable.mg3));
        }
        
        // Custom Mug Click
        if (cardCustomMug != null) {
            cardCustomMug.setOnClickListener(v -> {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("productName", "Custom Printed Mug");
                intent.putExtra("productPrice", "900"); // Custom mug base price
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
