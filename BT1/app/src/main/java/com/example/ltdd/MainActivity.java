package com.example.ltdd;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;
    private TextView tvOutput;
    private Button btnReverse;
    private Button btnProcessArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ẩn thanh tiêu đề
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        // Ánh xạ các view
        edtInput = findViewById(R.id.edtInput);
        tvOutput = findViewById(R.id.tvOutput);
        btnReverse = findViewById(R.id.btnReverse);
        btnProcessArray = findViewById(R.id.btnProcessArray);

        // Xử lý ArrayList số chẵn lẻ
        btnProcessArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processArrayList();
            }
        });

        // Đảo ngược chuỗi và in hoa
        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseAndUppercase();
            }
        });
    }

    // Phương thức xử lý ArrayList
    private void processArrayList() {
        // Tạo ArrayList chứa các số
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25));

        // Tạo ArrayList cho số chẵn và số lẻ
        ArrayList<Integer> evenNumbers = new ArrayList<>();
        ArrayList<Integer> oddNumbers = new ArrayList<>();

        // Phân loại số chẵn và số lẻ
        for (Integer number : numbers) {
            if (number % 2 == 0) {
                evenNumbers.add(number);
            } else {
                oddNumbers.add(number);
            }
        }

        // In ra Log.d
        Log.d("ARRAY_LIST", "=== DANH SÁCH SỐ ===");
        Log.d("ARRAY_LIST", "Mảng ban đầu: " + numbers.toString());
        Log.d("ARRAY_LIST", "Tổng số phần tử: " + numbers.size());
        Log.d("ARRAY_LIST", "");
        Log.d("ARRAY_LIST", "=== SỐ CHẴN ===");
        Log.d("ARRAY_LIST", "Các số chẵn: " + evenNumbers.toString());
        Log.d("ARRAY_LIST", "Số lượng số chẵn: " + evenNumbers.size());
        Log.d("ARRAY_LIST", "");
        Log.d("ARRAY_LIST", "=== SỐ LẺ ===");
        Log.d("ARRAY_LIST", "Các số lẻ: " + oddNumbers.toString());
        Log.d("ARRAY_LIST", "Số lượng số lẻ: " + oddNumbers.size());

        // Hiển thị Toast thông báo
        Toast.makeText(this,
                "Đã xử lý ArrayList!\nSố chẵn: " + evenNumbers.size() +
                        "\nSố lẻ: " + oddNumbers.size() +
                        "\nXem Logcat để biết chi tiết",
                Toast.LENGTH_LONG).show();
    }

    // Phương thức đảo ngược và in hoa chuỗi
    private void reverseAndUppercase() {
        String input = edtInput.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập chuỗi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đảo ngược chuỗi theo từ
        String reversed = reverseWords(input);

        // In hoa
        String result = reversed.toUpperCase();

        // Hiển thị kết quả lên TextView
        tvOutput.setText(result);

        // Hiển thị Toast
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        // In ra Log để kiểm tra
        Log.d("STRING_REVERSE", "Chuỗi gốc: " + input);
        Log.d("STRING_REVERSE", "Chuỗi đảo ngược và in hoa: " + result);
    }

    // Phương thức đảo ngược từ trong chuỗi
    private String reverseWords(String str) {
        // Tách chuỗi thành các từ
        String[] words = str.trim().split("\\s+");

        // Đảo ngược thứ tự các từ
        StringBuilder reversed = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            reversed.append(words[i]);
            if (i > 0) {
                reversed.append(" ");
            }
        }

        return reversed.toString();
    }
}