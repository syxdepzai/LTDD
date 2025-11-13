package com.example.bt2;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView1;
    private final int[] backgrounds = {
            R.drawable.bg_green,
            R.drawable.bg_blue,
            R.drawable.bg_purple
    };
    private Switch switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main).setBackgroundResource(R.drawable.bg_light);
        // setRandomBackground();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView1 = findViewById(R.id.imageView1);
        imageView1.setImageResource(R.drawable.sample_image);
        switchTheme = findViewById(R.id.switchTheme);
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.main).setBackgroundResource(R.drawable.bg_dark);
                } else {
                    findViewById(R.id.main).setBackgroundResource(R.drawable.bg_light);
                }
            }
        });
        Button btnOpenLinear = findViewById(R.id.btnOpenLinear);
        btnOpenLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LinearActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRandomBackground() {
        Random random = new Random();
        int randomIndex = random.nextInt(backgrounds.length);
        int backgroundID = backgrounds[randomIndex];
        findViewById(R.id.main).setBackgroundResource(backgroundID);
    }
}