package com.dyl.drawyourlift.ui.step1;

import com.dyl.drawyourlift.data.repository.ProjectRepository;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dyl.drawyourlift.R;
import android.content.Intent;
import com.dyl.drawyourlift.ui.step2.Step2Activity;


public class Step1Activity extends AppCompatActivity {

    EditText etUserName, etProjectName, etShaftWidth, etShaftDepth,
            etFloorHeight, etPitDepth, etOverheadHeight;
    Spinner spCapacity, spFloors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);

        etUserName = findViewById(R.id.etUserName);
        etProjectName = findViewById(R.id.etProjectName);
        etShaftWidth = findViewById(R.id.etShaftWidth);
        etShaftDepth = findViewById(R.id.etShaftDepth);
        etFloorHeight = findViewById(R.id.etFloorHeight);
        etPitDepth = findViewById(R.id.etPitDepth);
        etOverheadHeight = findViewById(R.id.etOverheadHeight);

        spCapacity = findViewById(R.id.spCapacity);
        spFloors = findViewById(R.id.spFloors);

        Button btnNext = findViewById(R.id.btnNextStep1);

        // Passenger Capacity
        String[] capacities = {"4 Passenger", "5 Passenger", "6 Passenger", "8 Passenger", "10 Passenger", "13 Passenger"};
        spCapacity.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, capacities));

        // Number of Floors
        String[] floors = new String[29];
        for (int i = 0; i < 29; i++) floors[i] = String.valueOf(i + 2);
        spFloors.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, floors));

        btnNext.setOnClickListener(v -> validateAndProceed());
    }

    private void validateAndProceed() {

        String projectName = etProjectName.getText().toString().trim();
        if (projectName.isEmpty()) {
            Toast.makeText(this, "Project Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        ProjectRepository repo = ProjectRepository.getInstance();

        repo.getProject().userName = etUserName.getText().toString().trim();
        repo.getProject().projectName = projectName;
        repo.getProject().passengerCapacity =
                Integer.parseInt(spCapacity.getSelectedItem().toString().split(" ")[0]);
        repo.getProject().numberOfFloors =
                Integer.parseInt(spFloors.getSelectedItem().toString());

        repo.getProject().shaftWidth =
                Integer.parseInt(etShaftWidth.getText().toString());
        repo.getProject().shaftDepth =
                Integer.parseInt(etShaftDepth.getText().toString());
        repo.getProject().floorHeight =
                Integer.parseInt(etFloorHeight.getText().toString());
        repo.getProject().pitDepth =
                Integer.parseInt(etPitDepth.getText().toString());
        repo.getProject().overheadHeight =
                Integer.parseInt(etOverheadHeight.getText().toString());

        Toast.makeText(this, "Step 1 data saved", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, Step2Activity.class));

    }

}
