package com.dyl.drawyourlift.ui.preview;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.drawing.elevation.ElevationView;
import com.dyl.drawyourlift.drawing.front.FrontView;
import com.dyl.drawyourlift.drawing.plan.PlanView;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        container.addView(new ElevationView(this));
        container.addView(new FrontView(this));
        container.addView(new PlanView(this));

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(container);

        setContentView(scrollView);
    }
}
