package com.dyl.drawyourlift.ui.step3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dyl.drawyourlift.R;
import com.dyl.drawyourlift.data.repository.ProjectRepository;


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

        setupMachineData();
        setupSpinners();

        btnPrevious.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> saveAndProceed());
    }

    private void setupMachineData() {
        machineModels.put("Montanari", new String[]{"M100", "M200", "M300"});
        machineModels.put("Mona Drive", new String[]{"MD10", "MD20"});
        machineModels.put("Shreya Shakti", new String[]{"SS-1", "SS-2"});
        machineModels.put("Tektronix", new String[]{"TX-100", "TX-200"});
        machineModels.put("Sharp", new String[]{"SH-5", "SH-10"});
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


    }
}
