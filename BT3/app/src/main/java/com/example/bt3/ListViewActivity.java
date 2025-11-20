package com.example.bt3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    ListView listViewMonHoc;
    ArrayList<MonHoc> arrayListMonHoc;
    MonHocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        View mainLayout = findViewById(R.id.mainLayoutList);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arrayListMonHoc = new ArrayList<>();
        listViewMonHoc = findViewById(R.id.listViewMonHoc);
        themDuLieu();

        adapter = new MonHocAdapter(this, R.layout.row_monhoc, arrayListMonHoc);
        listViewMonHoc.setAdapter(adapter);

        listViewMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonHoc monHoc = arrayListMonHoc.get(position);
                Toast.makeText(ListViewActivity.this,
                        "Bạn chọn: " + monHoc.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listViewMonHoc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MonHoc monHoc = arrayListMonHoc.get(position);
                Toast.makeText(ListViewActivity.this,
                        "Long Click: " + monHoc.getName(),
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void themDuLieu() {
        arrayListMonHoc.add(new MonHoc("Lập trình Java", "Ngôn ngữ lập trình hướng đối tượng mạnh mẽ", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Lập trình Android", "Phát triển ứng dụng di động trên nền tảng Android", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("Lập trình Web", "Thiết kế và phát triển website với HTML, CSS, JavaScript", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("Cơ sở dữ liệu", "Quản lý và xử lý dữ liệu với SQL, MySQL, SQLite", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Lập trình Python", "Ngôn ngữ lập trình đa năng, dễ học, mạnh mẽ", R.drawable.ic_python));
        arrayListMonHoc.add(new MonHoc("Java Spring Boot", "Framework phát triển ứng dụng Java Enterprise", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Android Jetpack Compose", "Toolkit hiện đại để xây dựng UI Android", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("ReactJS", "Thư viện JavaScript để xây dựng giao diện người dùng", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("MongoDB", "Cơ sở dữ liệu NoSQL linh hoạt và mạnh mẽ", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Machine Learning với Python", "Trí tuệ nhân tạo và học máy với Python", R.drawable.ic_python));
        arrayListMonHoc.add(new MonHoc("Java Hibernate", "Framework ORM cho Java để làm việc với database", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Kotlin Android", "Ngôn ngữ lập trình hiện đại cho Android", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("VueJS", "Framework JavaScript progressive để xây dựng UI", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("PostgreSQL", "Hệ quản trị cơ sở dữ liệu quan hệ mã nguồn mở", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Data Science Python", "Phân tích dữ liệu và khoa học dữ liệu với Python", R.drawable.ic_python));
        arrayListMonHoc.add(new MonHoc("Java Microservices", "Kiến trúc Microservices với Java", R.drawable.ic_java));
        arrayListMonHoc.add(new MonHoc("Flutter", "Framework đa nền tảng để phát triển mobile", R.drawable.ic_android));
        arrayListMonHoc.add(new MonHoc("Angular", "Framework TypeScript để xây dựng ứng dụng web", R.drawable.ic_web));
        arrayListMonHoc.add(new MonHoc("Firebase Realtime Database", "Cơ sở dữ liệu thời gian thực từ Google", R.drawable.ic_database));
        arrayListMonHoc.add(new MonHoc("Django Python", "Framework web back-end mạnh mẽ cho Python", R.drawable.ic_python));
    }
}
