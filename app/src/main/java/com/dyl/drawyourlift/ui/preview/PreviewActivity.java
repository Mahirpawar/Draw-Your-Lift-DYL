package com.dyl.drawyourlift.ui.preview;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.data.repository.ProjectRepository;
import com.dyl.drawyourlift.drawing.elevation.ElevationView;
import com.dyl.drawyourlift.drawing.front.FrontView;
import com.dyl.drawyourlift.drawing.plan.PlanView;
import android.widget.Button;
import android.widget.Toast;

import com.dyl.drawyourlift.utils.PdfGenerator;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        container.addView(new ElevationView(this));
        container.addView(new FrontView(this));
        container.addView(new PlanView(this));

        Button generatePdfBtn = new Button(this);
        generatePdfBtn.setText("Generate PDF");

        generatePdfBtn.setOnClickListener(v -> {
            try {
                File pdf = PdfGenerator.generateMultiPagePdf(
                        this,
                        ProjectRepository.getInstance().getProject().projectName
                );
                Toast.makeText(this, "PDF saved: " + pdf.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "PDF generation failed", Toast.LENGTH_SHORT).show();
            }
        });


        container.addView(generatePdfBtn);


        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(container);

        setContentView(scrollView);
    }

}
