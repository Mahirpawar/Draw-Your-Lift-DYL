package com.dyl.drawyourlift.ui.preview;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.data.model.LiftProject;
import com.dyl.drawyourlift.data.repository.ProjectRepository;
import com.dyl.drawyourlift.drawing.elevation.ElevationView;
import com.dyl.drawyourlift.drawing.front.FrontView;
import com.dyl.drawyourlift.drawing.plan.PlanView;
import com.dyl.drawyourlift.utils.PdfGenerator;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ===== ROOT LAYOUT =====
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // ===== SCROLLABLE DRAWINGS =====
        LinearLayout drawingContainer = new LinearLayout(this);
        drawingContainer.setOrientation(LinearLayout.VERTICAL);

        drawingContainer.addView(new ElevationView(this));
        drawingContainer.addView(new FrontView(this));
        drawingContainer.addView(new PlanView(this));

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(drawingContainer);

        // ===== FIXED BUTTON BAR =====
        LinearLayout buttonBar = new LinearLayout(this);
        buttonBar.setOrientation(LinearLayout.VERTICAL);
        buttonBar.setPadding(24, 16, 24, 16);

        Button singlePdfBtn = new Button(this);
        singlePdfBtn.setText("Generate Single Page PDF");

        Button multiPdfBtn = new Button(this);
        multiPdfBtn.setText("Generate Multi Page PDF");

        buttonBar.addView(singlePdfBtn);
        buttonBar.addView(multiPdfBtn);

        // ===== APPLY WINDOW INSETS (KEY FIX) =====
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int bottomInset = insets.getSystemWindowInsetBottom();
            buttonBar.setPadding(
                    buttonBar.getPaddingLeft(),
                    buttonBar.getPaddingTop(),
                    buttonBar.getPaddingRight(),
                    bottomInset + 16
            );
            return insets;
        });

        // ===== BUTTON ACTIONS =====
        LiftProject project = ProjectRepository.getInstance().getProject();

        singlePdfBtn.setOnClickListener(v -> {
            try {
                String liftTypeLabel = getLiftTypeLabel();

                File pdf = PdfGenerator.generateSinglePagePdf(
                        this,
                        ProjectRepository.getInstance().getProject().projectName,
                        liftTypeLabel
                );
                Toast.makeText(this, "Single page PDF saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Single page PDF failed", Toast.LENGTH_SHORT).show();
            }
        });

        multiPdfBtn.setOnClickListener(v -> {
            try {

                File pdf = PdfGenerator.generateMultiPagePdf(
                        this,
                        ProjectRepository.getInstance().getProject().projectName

                );
                Toast.makeText(this, "Multi page PDF saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "PDF generation failed", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== ASSEMBLE VIEW =====
        root.addView(scrollView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));
        root.addView(buttonBar);

        setContentView(root);
    }
    private String getLiftTypeLabel() {
        LiftProject p = ProjectRepository.getInstance().getProject();

        String door =
                p.doorType.equals("AUTO") ? "Auto Door" : "Manual Door";

        String lift;

        switch (p.liftType) {
            case "GEARLESS":
                lift = "Gearless";
                break;
            case "TRACTION":
                lift = "Traction";
                break;
            case "CANTILEVER":
                lift = "Cantilever";
                break;
            case "HYDRAULIC":
                lift = "Hydraulic";
                break;
            default:
                lift = "Unknown";
        }

        return door + " – " + lift;
    }

}
