package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    ImageView btnLogout, btnHelp, btnNotifications;
    CardView cardBusinessCards, cardFlyers, cardPosters, cardBanners, cardTShirts, cardMugs, cardGallery, cardCustomDesign, cardAlbum;
    LinearLayout navHome, navOrders, navProfile;
    TextView welcomeText;
    EditText edtSearch;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();

        // Views Initialize
        btnLogout = findViewById(R.id.btnLogout);
        btnHelp = findViewById(R.id.btnHelp);
        btnNotifications = findViewById(R.id.btnNotifications);
        
        cardBusinessCards = findViewById(R.id.cardBusinessCards);
        cardFlyers = findViewById(R.id.cardFlyers);
        cardPosters = findViewById(R.id.cardPosters);
        cardBanners = findViewById(R.id.cardBanners);
        cardTShirts = findViewById(R.id.cardTShirts);
        cardMugs = findViewById(R.id.cardMugs);
        cardGallery = findViewById(R.id.cardGallery);
        cardCustomDesign = findViewById(R.id.cardCustomDesign);
        cardAlbum = findViewById(R.id.cardAlbum);

        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);
        navProfile = findViewById(R.id.navProfile);
        welcomeText = findViewById(R.id.welcomeText);
        edtSearch = findViewById(R.id.edtSearch);

        loadUserInfo();
        setupClicks();
        setupSearch();
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase.child("Users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.getValue(String.class);
                        welcomeText.setText("Welcome back, " + name + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void setupSearch() {
        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterItems(s.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void filterItems(String query) {
        cardBusinessCards.setVisibility("business cards".contains(query) ? View.VISIBLE : View.GONE);
        cardFlyers.setVisibility("flyers".contains(query) ? View.VISIBLE : View.GONE);
        cardPosters.setVisibility("posters".contains(query) ? View.VISIBLE : View.GONE);
        cardBanners.setVisibility("banners".contains(query) ? View.VISIBLE : View.GONE);
        cardTShirts.setVisibility("t-shirts".contains(query) || "shirts".contains(query) ? View.VISIBLE : View.GONE);
        cardMugs.setVisibility("mugs".contains(query) || "custom mugs".contains(query) ? View.VISIBLE : View.GONE);
        if (cardGallery != null) cardGallery.setVisibility("gallery".contains(query) || "designs".contains(query) ? View.VISIBLE : View.GONE);
        if (cardCustomDesign != null) cardCustomDesign.setVisibility("custom design".contains(query) || "upload".contains(query) ? View.VISIBLE : View.GONE);
        if (cardAlbum != null) cardAlbum.setVisibility("album".contains(query) || "saved".contains(query) ? View.VISIBLE : View.GONE);
    }

    private void setupClicks() {
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, HelpActivity.class)));
        }

        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NotificationActivity.class)));
        }

        if (cardGallery != null) {
            cardGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        }

        if (cardAlbum != null) {
            cardAlbum.setOnClickListener(v -> startActivity(new Intent(this, AlbumActivity.class)));
        }

        if (cardCustomDesign != null) {
            cardCustomDesign.setOnClickListener(v -> {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra("productName", "Custom Artwork Order");
                intent.putExtra("productPrice", "1000");
                startActivity(intent);
            });
        }

        // Category Clicks
        if (cardBusinessCards != null) cardBusinessCards.setOnClickListener(v -> startActivity(new Intent(this, BusinessCardActivity.class)));
        if (cardFlyers != null) cardFlyers.setOnClickListener(v -> startActivity(new Intent(this, FlyerActivity.class)));
        if (cardPosters != null) cardPosters.setOnClickListener(v -> startActivity(new Intent(this, PosterActivity.class)));
        if (cardBanners != null) cardBanners.setOnClickListener(v -> startActivity(new Intent(this, BannerActivity.class)));
        if (cardTShirts != null) cardTShirts.setOnClickListener(v -> startActivity(new Intent(this, TShirtActivity.class)));
        if (cardMugs != null) cardMugs.setOnClickListener(v -> startActivity(new Intent(this, MugActivity.class)));

        // Navigation Clicks
        if (navOrders != null) navOrders.setOnClickListener(v -> startActivity(new Intent(this, OrderTrackingActivity.class)));
        if (navProfile != null) navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
