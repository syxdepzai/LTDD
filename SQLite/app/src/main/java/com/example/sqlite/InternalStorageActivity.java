package com.example.sqlite;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Ví dụ lưu tập tin ở bộ nhớ trong (trang 299-304).
 */
public class InternalStorageActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editPass;
    private TextView getName;
    private TextView getPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        editName = findViewById(R.id.editName);
        editPass = findViewById(R.id.editPass);
        getName = findViewById(R.id.textGetName);
        getPass = findViewById(R.id.textGetPass);
    }

    // SAVE
    public void saveMe(View view) {
        File file;
        String name = editName.getText().toString();
        String password = editPass.getText().toString();
        FileOutputStream fileOutputStream = null;

        try {
            name = name + " ";
            file = getFilesDir();
            fileOutputStream = openFileOutput("Code.txt", MODE_PRIVATE);
            fileOutputStream.write(name.getBytes());
            fileOutputStream.write(password.getBytes());
            Toast.makeText(this, "Saved\nPath -- " + file + "/Code.txt", Toast.LENGTH_SHORT).show();
            editName.setText("");
            editPass.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Lỗi lưu file", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // LOAD
    public void loadMe(View view) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("Code.txt");
            int read;
            StringBuilder buffer = new StringBuilder();
            while ((read = fileInputStream.read()) != -1) {
                buffer.append((char) read);
            }
            String result = buffer.toString();
            String name = result.substring(0, result.indexOf(" "));
            String pass = result.substring(result.indexOf(" ") + 1);
            getName.setText(name);
            getPass.setText(pass);
            Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Không đọc được Code.txt", Toast.LENGTH_SHORT).show();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


