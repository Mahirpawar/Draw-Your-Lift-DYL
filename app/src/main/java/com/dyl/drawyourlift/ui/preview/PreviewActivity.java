package com.dyl.drawyourlift.ui.preview;

import android.os.Bundle;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.drawing.elevation.ElevationView;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create drawing view
        ElevationView elevationView = new ElevationView(this);

        // Wrap it in ScrollView
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(elevationView);

        // Set ONLY ONCE
        setContentView(scrollView);
    }
}
