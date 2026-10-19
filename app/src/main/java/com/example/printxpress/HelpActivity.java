package com.example.printxpress;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    TextView txtGuidelines, txtFaq, txtSupport;
    Button btnCall, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        txtGuidelines = findViewById(R.id.txtGuidelines);
        txtFaq = findViewById(R.id.txtFaq);
        txtSupport = findViewById(R.id.txtSupport);
        
        // Buttons (XML එකේ මේ IDs ඇති බවට වග බලා ගන්න)
        btnCall = findViewById(R.id.btnCall);
        btnEmail = findViewById(R.id.btnEmail);

        // Print Guidelines
        txtGuidelines.setText(
                "Print File Guidelines\n\n" +
                        "• Supported Formats: JPG, PNG, PDF\n" +
                        "• Recommended Resolution: 300 DPI\n" +
                        "• Use CMYK color format for best results\n" +
                        "• Add 3mm bleed margins for posters\n" +
                        "• Ensure text is clearly visible"
        );

        // FAQ Section
        txtFaq.setText(
                "Frequently Asked Questions\n\n" +
                        "Q: What file types can I upload?\n" +
                        "A: JPG, PNG, and PDF files are supported.\n\n" +
                        "Q: How long does printing take?\n" +
                        "A: Most orders take 1–3 days."
        );

        // Support Details
        txtSupport.setText(
                "Customer Support\n\n" +
                        "Working Hours: 8.00 AM - 6.00 PM\n" +
                        "Address: No 123, Galle Road, Colombo 03"
        );

        // Call Support
        if (btnCall != null) {
            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+94771234567"));
                startActivity(intent);
            });
        }

        // Email Support
        if (btnEmail != null) {
            btnEmail.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:support@printxpress.lk"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Inquiry - PrintXpress");
                try {
                    startActivity(Intent.createChooser(intent, "Send Email..."));
                } catch (Exception e) {
                    Toast.makeText(this, "No Email app found", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
