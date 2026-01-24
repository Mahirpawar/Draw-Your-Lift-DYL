package com.dyl.drawyourlift.ui.step4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.R;
import com.dyl.drawyourlift.data.repository.ProjectRepository;
import android.content.Intent;
import com.dyl.drawyourlift.ui.preview.PreviewActivity;



public class Step4Activity extends AppCompatActivity {

    EditText etLeftWall, etMainBracket, etCounterBracket,
            etCabinWallGap, etCabinOffset, etSiteAddress;

    Spinner spGuideRail, spPdfType, spBracketDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4);


        etLeftWall = findViewById(R.id.etLeftWall);
        etMainBracket = findViewById(R.id.etMainBracket);
        etCounterBracket = findViewById(R.id.etCounterBracket);
        etCabinWallGap = findViewById(R.id.etCabinWallGap);
        etCabinOffset = findViewById(R.id.etCabinOffset);
        etSiteAddress = findViewById(R.id.etSiteAddress);

        spGuideRail = findViewById(R.id.spGuideRail);
        spPdfType = findViewById(R.id.spPdfType);
        spBracketDistance = findViewById(R.id.spBracketDistance);

        Button btnPrevious = findViewById(R.id.btnPreviousStep4);
        Button btnSubmit = findViewById(R.id.btnSubmitStep4);

        setupSpinners();

        btnPrevious.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> saveAndProceed());
    }

    private void setupSpinners() {

        spGuideRail.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"9 x 5 mm", "16 x 10 mm"}
        ));

        spPdfType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Single Page PDF", "Multi Page PDF"}
        ));

        spBracketDistance.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"1800", "2000", "2500"}
        ));
    }

    private void saveAndProceed() {

        ProjectRepository repo = ProjectRepository.getInstance();

        repo.getProject().leftWallDistance = parse(etLeftWall);
        repo.getProject().mainBracketDistance = parse(etMainBracket);
        repo.getProject().counterBracketDistance = parse(etCounterBracket);
        repo.getProject().cabinWallGap = parse(etCabinWallGap);
        repo.getProject().cabinCenterOffset = parse(etCabinOffset);
        repo.getProject().siteAddress = etSiteAddress.getText().toString().trim();

        repo.getProject().guideRailPreference = spGuideRail.getSelectedItem().toString();
        repo.getProject().pdfType = spPdfType.getSelectedItem().toString();
        repo.getProject().bracketDistance =
                Integer.parseInt(spBracketDistance.getSelectedItem().toString());

        Toast.makeText(this, "Opening Preview", Toast.LENGTH_SHORT).show();


        startActivity(new Intent(this, PreviewActivity.class));

    }

    private int parse(EditText et) {
        String v = et.getText().toString().trim();
        return v.isEmpty() ? 0 : Integer.parseInt(v);
    }
}
