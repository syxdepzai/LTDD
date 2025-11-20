package com.example.bt3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {

    GridView gridViewMonHoc;
    ArrayList<MonHoc> arrayListMonHoc;
    MonHocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        View mainLayout = findViewById(R.id.mainLayoutGrid);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arrayListMonHoc = new ArrayList<>();
        gridViewMonHoc = findViewById(R.id.gridViewMonHoc);
        themDuLieu();

        adapter = new MonHocAdapter(this, R.layout.row_monhoc, arrayListMonHoc);
        gridViewMonHoc.setAdapter(adapter);

        gridViewMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonHoc monHoc = arrayListMonHoc.get(position);
                Toast.makeText(GridViewActivity.this,
                        "Bạn chọn: " + monHoc.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        gridViewMonHoc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MonHoc monHoc = arrayListMonHoc.get(position);
                Toast.makeText(GridViewActivity.this,
                        "Long Click: " + monHoc.getName(),
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void themDuLieu() {
        arrayListMonHoc.add(new MonHoc("Java", "Lập trình hướng đối tượng", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Android", "Phát triển ứng dụng mobile", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("Web", "HTML, CSS, JavaScript", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("Database", "SQL, MySQL, SQLite", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Python", "Đa năng, dễ học", R.drawable.ic_python));
        arrayListMonHoc.add(new MonHoc("Spring Boot", "Java Enterprise", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Jetpack Compose", "UI hiện đại", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("ReactJS", "Thư viện JavaScript", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("MongoDB", "NoSQL Database", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Django", "Web Framework", R.drawable.ic_python));
        arrayListMonHoc.add(new MonHoc("Kotlin", "Lập trình Android", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("VueJS", "JavaScript Framework", R.drawable.ic_web));
    }
}
