package com.example.sqlite;

import android.os.Bundle;
import android.os.Environment;
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
 * Ví dụ lưu tập tin ở bộ nhớ ngoài (trang 305-315),
 * đã đơn giản hóa cho Android mới (không hỏi runtime permission).
 */
public class ExternalStorageActivity extends AppCompatActivity {

    private EditText editText;
    private TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        editText = findViewById(R.id.editExternalInput);
        showText = findViewById(R.id.textExternalOutput);
    }

    // Lưu "public" (theo giáo trình) – trên Android mới sẽ dùng thư mục external RIÊNG của app,
    // nhưng nằm trong nhóm ALARMS để bạn dễ phân biệt.
    public void savePublic(View view) {
        String info = editText.getText().toString();
        if (info.isEmpty()) {
            Toast.makeText(this, "Nhập nội dung trước", Toast.LENGTH_SHORT).show();
            return;
        }

        File folder = getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        if (folder == null) {
            Toast.makeText(this, "Không truy cập được thư mục public (app-scoped)", Toast.LENGTH_SHORT).show();
            return;
        }

        File myFile = new File(folder, "myData1.txt");
        writeData(myFile, info);
        editText.setText("");
    }

    // Lưu "private": myData2.txt trong thư mục Demo của app (bị xóa khi gỡ app)
    public void savePrivate(View view) {
        String info = editText.getText().toString();
        if (info.isEmpty()) {
            Toast.makeText(this, "Nhập nội dung trước", Toast.LENGTH_SHORT).show();
            return;
        }

        File folder = getExternalFilesDir("Demo");
        if (folder == null) {
            Toast.makeText(this, "Không truy cập được thư mục private", Toast.LENGTH_SHORT).show();
            return;
        }

        File myFile = new File(folder, "myData2.txt");
        writeData(myFile, info);
        editText.setText("");
    }

    private void writeData(File myFile, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this, "Done: " + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi ghi file", Toast.LENGTH_SHORT).show();
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

    public void getPublic(View view) {
        File folder = getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        if (folder == null) {
            showText.setText("No Data");
            return;
        }
        File myFile = new File(folder, "myData1.txt");
        String text = getData(myFile);
        if (text != null) {
            showText.setText(text);
        } else {
            showText.setText("No Data");
        }
    }

    public void getPrivate(View view) {
        File folder = getExternalFilesDir("Demo");
        if (folder == null) {
            showText.setText("No Data");
            return;
        }
        File myFile = new File(folder, "myData2.txt");
        String text = getData(myFile);
        if (text != null) {
            showText.setText(text);
        } else {
            showText.setText("No Data");
        }
    }

    private String getData(File myFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(myFile);
            int i;
            StringBuilder buffer = new StringBuilder();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}


