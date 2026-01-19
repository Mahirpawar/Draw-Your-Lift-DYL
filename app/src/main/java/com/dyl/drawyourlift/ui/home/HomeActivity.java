package com.dyl.drawyourlift.ui.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dyl.drawyourlift.R;

public class HomeActivity extends AppCompatActivity {

    private String doorType = "";
    private String liftType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnAutoDoor = findViewById(R.id.btnAutoDoor);
        Button btnManualDoor = findViewById(R.id.btnManualDoor);
        Button btnGearless = findViewById(R.id.btnGearless);
        Button btnTraction = findViewById(R.id.btnTraction);
        Button btnCantilever = findViewById(R.id.btnCantilever);
        Button btnHydraulic = findViewById(R.id.btnHydraulic);
        Button btnNext = findViewById(R.id.btnNext);

        btnAutoDoor.setOnClickListener(v -> {
            doorType = "AUTO";
            Toast.makeText(this, "Auto Door Selected", Toast.LENGTH_SHORT).show();
        });

        btnManualDoor.setOnClickListener(v -> {
            doorType = "MANUAL";
            Toast.makeText(this, "Manual Door Selected", Toast.LENGTH_SHORT).show();
        });

        btnGearless.setOnClickListener(v -> liftType = "GEARLESS");
        btnTraction.setOnClickListener(v -> liftType = "TRACTION");
        btnCantilever.setOnClickListener(v -> liftType = "CANTILEVER");
        btnHydraulic.setOnClickListener(v -> liftType = "HYDRAULIC");

        btnNext.setOnClickListener(v -> {
            if (doorType.isEmpty() || liftType.isEmpty()) {
                Toast.makeText(this, "Please select Door Type and Lift Type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (doorType.equals("MANUAL") &&
                    (liftType.equals("CANTILEVER") || liftType.equals("HYDRAULIC"))) {
                Toast.makeText(this,
                        "Selected lift type not allowed for Manual Door",
                        Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(this,
                    "Proceeding to Step 1\n" + doorType + " - " + liftType,
                    Toast.LENGTH_LONG).show();

            // Step 1 Activity will be added on Day 3
        });
    }
}
