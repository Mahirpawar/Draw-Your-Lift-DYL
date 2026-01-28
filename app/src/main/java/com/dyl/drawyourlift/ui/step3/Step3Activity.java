package com.dyl.drawyourlift.ui.step3;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.dyl.drawyourlift.ui.step4.Step4Activity;


import java.util.HashMap;

public class Step3Activity extends AppCompatActivity {

    Spinner spRoping, spMachineBrand, spMachineModel,
            spCounterSide, spCounterDbg, spCounterPulley;

    HashMap<String, String[]> machineModels = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        spRoping = findViewById(R.id.spRoping);
        spMachineBrand = findViewById(R.id.spMachineBrand);
        spMachineModel = findViewById(R.id.spMachineModel);
        spCounterSide = findViewById(R.id.spCounterSide);
        spCounterDbg = findViewById(R.id.spCounterDbg);
        spCounterPulley = findViewById(R.id.spCounterPulley);

        Button btnPrevious = findViewById(R.id.btnPreviousStep3);
        Button btnNext = findViewById(R.id.btnNextStep3);
        View root = findViewById(R.id.rootStep3);

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


        setupMachineData();
        setupSpinners();

        btnPrevious.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> saveAndProceed());
    }

    private void setupMachineData() {
        machineModels.put("Montanari", new String[]{"M100", "M200", "M300"});
        machineModels.put("Mona Drive", new String[]{"MD10", "MD20"});
        machineModels.put("Bharat Bijli", new String[]{"SS-1", "SS-2"});
        machineModels.put("Techtronics", new String[]{"TX-100", "TX-200"});
        machineModels.put("Sharp", new String[]{"SH-5 hp","Sh-7.5 hp", "SH-10 hp"});
    }

    private void setupSpinners() {

        spRoping.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"1:1", "2:1"}
        ));

        String[] brands = machineModels.keySet().toArray(new String[0]);
        spMachineBrand.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                brands
        ));

        spMachineBrand.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent,
                                               android.view.View view,
                                               int position,
                                               long id) {
                        String brand = brands[position];
                        spMachineModel.setAdapter(new ArrayAdapter<>(
                                Step3Activity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                machineModels.get(brand)
                        ));
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });

        spCounterSide.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Left", "Right", "Back"}
        ));

        spCounterDbg.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"300", "350", "400", "500", "600", "650"}
        ));

        spCounterPulley.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"300", "350", "400", "450"}
        ));
    }

    private void saveAndProceed() {

        ProjectRepository repo = ProjectRepository.getInstance();

        repo.getProject().ropingStyle = spRoping.getSelectedItem().toString();
        repo.getProject().machineBrand = spMachineBrand.getSelectedItem().toString();
        repo.getProject().machineModel = spMachineModel.getSelectedItem().toString();
        repo.getProject().counterFrameSide = spCounterSide.getSelectedItem().toString();
        repo.getProject().counterDbgSize =
                Integer.parseInt(spCounterDbg.getSelectedItem().toString());
        repo.getProject().counterPulley =
                Integer.parseInt(spCounterPulley.getSelectedItem().toString());

        Toast.makeText(this, "Step 3 data saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Step4Activity.class));



    }
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}
