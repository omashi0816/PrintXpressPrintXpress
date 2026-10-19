package com.example.printxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AlbumActivity extends AppCompatActivity {

    GridLayout albumGrid;
    TextView txtNoDesigns;
    View cardAddDesign;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uploadDesignToAlbum(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        albumGrid = findViewById(R.id.albumGrid);
        txtNoDesigns = findViewById(R.id.txtNoDesigns);
        cardAddDesign = findViewById(R.id.cardAddDesign);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://printxpress-fb488-default-rtdb.firebaseio.com/").getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (cardAddDesign != null) {
            cardAddDesign.setOnClickListener(v -> mGetContent.launch("image/*"));
        }

        loadUserAlbum();
    }

    private void loadUserAlbum() {
        String uId = mAuth.getUid();
        if (uId == null) return;

        mDatabase.child("Users").child(uId).child("savedDesigns").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                albumGrid.removeAllViews();
                // සැමවිටම "Add Design" බොත්තම මුලින්ම තබමු
                if (cardAddDesign != null) albumGrid.addView(cardAddDesign);

                if (snapshot.exists()) {
                    txtNoDesigns.setVisibility(View.GONE);
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String imageUrl = child.getValue(String.class);
                        if (imageUrl != null) {
                            addDesignToGrid(imageUrl);
                        }
                    }
                } else {
                    txtNoDesigns.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void uploadDesignToAlbum(Uri uri) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.setMessage("Adding design to your album");
        pd.setCancelable(false);
        pd.show();

        String fileName = "album/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference fileRef = mStorageRef.child(fileName);

        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String uId = mAuth.getUid();
                if (uId != null) {
                    mDatabase.child("Users").child(uId).child("savedDesigns").push().setValue(downloadUri.toString());
                }
                pd.dismiss();
                Toast.makeText(this, "Design Saved Successfully!", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void addDesignToGrid(String url) {
        ImageView imageView = new ImageView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 400; // Fixed width for Grid
        params.height = 400;
        params.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

        // මෙහිදී Glide භාවිතා කළ යුතුයි. (මම දැනටමත් Glide පද්ධතියට එක් කළා)
        com.bumptech.glide.Glide.with(this).load(url).into(imageView);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(AlbumActivity.this, ProductDetailActivity.class);
            intent.putExtra("productName", "Custom Order from Album");
            intent.putExtra("productPrice", "1000");
            intent.putExtra("productImage", url); // URL එක පින්තූරය ලෙස යවමු
            startActivity(intent);
        });
        
        albumGrid.addView(imageView);
    }
}
