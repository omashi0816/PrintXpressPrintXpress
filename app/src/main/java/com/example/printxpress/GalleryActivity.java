package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {

    private Map<Integer, String> cardNames = new HashMap<>();
    private EditText edtSearchGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        edtSearchGallery = findViewById(R.id.edtSearchGallery);

        // UPLOAD OWN DESIGN CARD
        View cardUploadOwn = findViewById(R.id.cardUploadOwn);
        if (cardUploadOwn != null) {
            cardNames.put(R.id.cardUploadOwn, "upload your own custom design");
            cardUploadOwn.setOnClickListener(v -> {
                Intent intent = new Intent(GalleryActivity.this, OrderActivity.class);
                intent.putExtra("productName", "Custom Artwork Order");
                intent.putExtra("productPrice", "1000"); // Default base price
                startActivity(intent);
            });
        }

        // Business Cards
        setupCard(R.id.card1, "Business Card v1", "500", R.drawable.bussinescard1);
        setupCard(R.id.card6, "Business Card v2", "800", R.drawable.bussinescard2);
        setupCard(R.id.card11, "Business Card v3", "600", R.drawable.bussinescard3);

        // Flyers
        setupCard(R.id.card2, "Flyer Design v1", "700", R.drawable.flyer1);
        setupCard(R.id.card7, "Flyer Design v2", "900", R.drawable.flyer2);
        setupCard(R.id.card12, "Flyer Design v3", "800", R.drawable.flyer3);

        // Posters
        setupCard(R.id.card3, "Poster Design v1", "1200", R.drawable.ps1);
        setupCard(R.id.card8, "Poster Design v2", "1500", R.drawable.ps2);
        setupCard(R.id.card13, "Poster Design v3", "1300", R.drawable.ps3);

        // T-Shirts
        setupCard(R.id.card4, "T-Shirt Design v1", "1500", R.drawable.tshirt1);
        setupCard(R.id.card9, "T-Shirt Design v2", "1800", R.drawable.tshirt2);
        setupCard(R.id.card14, "T-Shirt Design v3", "1600", R.drawable.tshirt3);

        // Mugs
        setupCard(R.id.card5, "Mug Design v1", "800", R.drawable.mg1);
        setupCard(R.id.card10, "Mug Design v2", "1200", R.drawable.mg2);
        setupCard(R.id.card15, "Mug Design v3", "1000", R.drawable.mg3);

        // Setup Search Listener
        if (edtSearchGallery != null) {
            edtSearchGallery.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterGallery(s.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupCard(int cardId, String name, String price, int imageResId) {
        View card = findViewById(cardId);
        if (card != null) {
            cardNames.put(cardId, name.toLowerCase());
            card.setOnClickListener(v -> {
                Intent intent = new Intent(GalleryActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", name);
                intent.putExtra("productPrice", price);
                intent.putExtra("productImage", imageResId);
                startActivity(intent);
            });
        }
    }

    private void filterGallery(String query) {
        for (Map.Entry<Integer, String> entry : cardNames.entrySet()) {
            View card = findViewById(entry.getKey());
            if (card != null) {
                if (entry.getValue().contains(query)) {
                    card.setVisibility(View.VISIBLE);
                } else {
                    card.setVisibility(View.GONE);
                }
            }
        }
    }
}
