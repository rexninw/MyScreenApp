package com.example.screenmirror;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1001;
    private Button connectButton;
    private boolean isConnecting = false;
    private MediaProjectionManager projectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.connectButton);
        Spinner menuSpinner = findViewById(R.id.menuSpinner);

        String[] menuItems = {"تنظیمات", "درباره", "خروج"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, menuItems);
        menuSpinner.setAdapter(adapter);

        projectionManager = (MediaProjectionManager)
                getSystemService(MEDIA_PROJECTION_SERVICE);

        connectButton.setOnClickListener(v -> {
            if (!isConnecting) {
                requestScreenCapture();
            } else {
                stopSharing();
            }
        });
    }

    private void requestScreenCapture() {
        connectButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFFFFC107));
        connectButton.setText("Connecting...");
        isConnecting = true;

        Intent intent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE_SCREEN_CAPTURE);
    }

    private void stopSharing() {
        connectButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF00C853));
        connectButton.setText("Connect");
        isConnecting = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                connectButton.setText("Disconnect");
                connectButton.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(0xFFFFC107));
                isConnecting = true;
            } else {
                stopSharing();
            }
        }
    }
}
