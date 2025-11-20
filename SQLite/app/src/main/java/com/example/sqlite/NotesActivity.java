package com.example.sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

/**
 * Activity triển khai ví dụ SQLite Notes (trang 282-298).
 */
public class NotesActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = findViewById(R.id.toolbarNotes);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        listView = findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.row_notes, arrayList);
        listView.setAdapter(adapter);

        // Tạo bảng nếu chưa có
        databaseHandler.queryData("CREATE TABLE IF NOT EXISTS " +
                DatabaseHandler.TABLE_NOTES + " (" +
                DatabaseHandler.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseHandler.COL_NAME + " TEXT)");

        databaseSQLite();
    }

    private void databaseSQLite() {
        arrayList.clear();
        // Lấy dữ liệu
        android.database.Cursor cursor = databaseHandler.getData("SELECT * FROM " + DatabaseHandler.TABLE_NOTES);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            arrayList.add(new NotesModel(id, name));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    // Menu thêm Notes
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAddNotes) {
            dialogThem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_notes);

        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonAdd = dialog.findViewById(R.id.buttonThem);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        buttonAdd.setOnClickListener(v -> {
            String name = editText.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(NotesActivity.this, "Vui lòng nhập tên Notes", Toast.LENGTH_SHORT).show();
            } else {
                databaseHandler.queryData("INSERT INTO " + DatabaseHandler.TABLE_NOTES +
                        " VALUES (null, '" + name.replace("'", "''") + "')");
                Toast.makeText(NotesActivity.this, "Đã thêm Notes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                databaseSQLite();
            }
        });

        buttonHuy.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Hàm dialog cập nhật Notes
    public void dialogCapNhatNotes(String name, int id) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_notes);

        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonEdit = dialog.findViewById(R.id.buttonEdit);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        editText.setText(name);

        buttonEdit.setOnClickListener(v -> {
            String newName = editText.getText().toString().trim();
            databaseHandler.queryData("UPDATE " + DatabaseHandler.TABLE_NOTES +
                    " SET " + DatabaseHandler.COL_NAME + " = '" + newName.replace("'", "''") +
                    "' WHERE " + DatabaseHandler.COL_ID + " = " + id);
            Toast.makeText(NotesActivity.this, "Đã cập nhật Notes thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            databaseSQLite();
        });

        buttonHuy.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Hàm dialog xóa
    public void dialogDelete(String name, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa Notes \"" + name + "\" này không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.queryData("DELETE FROM " + DatabaseHandler.TABLE_NOTES +
                        " WHERE " + DatabaseHandler.COL_ID + " = " + id);
                Toast.makeText(NotesActivity.this,
                        "Đã xóa Notes \"" + name + "\" thành công", Toast.LENGTH_SHORT).show();
                databaseSQLite();
            }
        });
        builder.setNegativeButton("Không", null);
        builder.show();
    }
}
