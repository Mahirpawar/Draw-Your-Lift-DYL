package com.dyl.drawyourlift.ui.step2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.R;
import com.dyl.drawyourlift.data.repository.ProjectRepository;
import com.dyl.drawyourlift.ui.step3.Step3Activity;

public class Step2Activity extends AppCompatActivity {

    Spinner spDoorOperator, spOpeningSide, spClearOpening,
            spDoorFinish, spDoorVision, spDoorHeight, spCabinFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        spDoorOperator = findViewById(R.id.spDoorOperator);
        spOpeningSide = findViewById(R.id.spOpeningSide);
        spClearOpening = findViewById(R.id.spClearOpening);
        spDoorFinish = findViewById(R.id.spDoorFinish);
        spDoorVision = findViewById(R.id.spDoorVision);
        spDoorHeight = findViewById(R.id.spDoorHeight);
        spCabinFinish = findViewById(R.id.spCabinFinish);

        Button btnPrevious = findViewById(R.id.btnPreviousStep2);
        Button btnNext = findViewById(R.id.btnNextStep2);
        View root = findViewById(R.id.rootStep2);

        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int bottomInset = insets.getSystemWindowInsetBottom();

            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    bottomInset + dpToPx(16)
            );
            return insets;
        });


        setupSpinners();

        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> saveAndProceed());
    }

    private void setupSpinners() {

        spDoorOperator.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Wittur", "Fermator", "Aaron", "Shiv shakti", "Other"}));

        spOpeningSide.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Center Opening", "Left Opening", "Right Opening"}));

        spClearOpening.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"600", "700", "750", "800", "850"}));

        spDoorFinish.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"SS Matt", "SS Mirror", "Powder Coated"}));

        spDoorVision.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Full Vision", "Half Vision", "Small Vision", "No Vision"}));

        spDoorHeight.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"2000", "2100", "2200"}));

        spCabinFinish.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"SS Matt", "SS Hairline", "Laminate", "Glass"}));
    }

    private void saveAndProceed() {

        ProjectRepository repo = ProjectRepository.getInstance();

        repo.getProject().doorOperator = spDoorOperator.getSelectedItem().toString();
        repo.getProject().openingSide = spOpeningSide.getSelectedItem().toString();
        repo.getProject().clearOpening =
                Integer.parseInt(spClearOpening.getSelectedItem().toString());
        repo.getProject().doorFinish = spDoorFinish.getSelectedItem().toString();
        repo.getProject().doorVision = spDoorVision.getSelectedItem().toString();
        repo.getProject().doorHeight =
                Integer.parseInt(spDoorHeight.getSelectedItem().toString());
        repo.getProject().cabinFinish = spCabinFinish.getSelectedItem().toString();

        Toast.makeText(this, "Step 2 data saved", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, Step3Activity.class));

    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}
