package com.example.bt3;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView rvSongs;
    ArrayList<SongModel> mSongList;
    SongAdapter mSongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        View mainLayout = findViewById(R.id.mainLayoutRecycler);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvSongs = findViewById(R.id.rvSongs);

        mSongList = new ArrayList<>();
        themDuLieu();

        mSongAdapter = new SongAdapter(this, mSongList);
        rvSongs.setAdapter(mSongAdapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void themDuLieu() {
        mSongList.add(new SongModel("S001", "Lạc Trôi", "Mưa dầm thật lâu...", "Sơn Tùng M-TP"));
        mSongList.add(new SongModel("S002", "Nơi Này Có Anh", "Đã bao giờ em muốn bỏ qua...", "Sơn Tùng M-TP"));
        mSongList.add(new SongModel("S003", "Bèo Dạt Mây Trôi", "Bèo dạt mây trôi...", "Anh Tú"));
        mSongList.add(new SongModel("S004", "3107", "Cuộc sống anh ngày càng khó...", "Duongg, Nâu, W/N"));
        mSongList.add(new SongModel("S005", "Chúng Ta Của Hiện Tại", "Có những phút giây đôi ta...", "Sơn Tùng M-TP"));
        mSongList.add(new SongModel("S006", "Em Của Ngày Hôm Qua", "Em à...", "Sơn Tùng M-TP"));
        mSongList.add(new SongModel("S007", "Đi Theo Bóng Mặt Trời", "Anh muốn nắm tay em...", "Đen Vâu"));
        mSongList.add(new SongModel("S008", "Anh Đếch Cần Gì Nhiều Ngoài Em", "Anh đếch cần gì nhiều...", "Đen Vâu, Thành Đồng"));
        mSongList.add(new SongModel("S009", "Bạc Phận", "Nào cô thương người này chăng...", "K-ICM"));
        mSongList.add(new SongModel("S010", "Hơn Cả Yêu", "Thì ra anh chỉ là...", "Đức Phúc"));
        mSongList.add(new SongModel("S011", "Sóng Gió", "Đắm trong men say...", "K-ICM, APJ"));
        mSongList.add(new SongModel("S012", "Một Đêm Say", "Đêm qua em có nhớ không...", "Bích Phương"));
        mSongList.add(new SongModel("S013", "Lạ Lùng", "Anh đã quen với việc...", "Vũ"));
        mSongList.add(new SongModel("S014", "Yêu Một Người Có Lẽ", "Yêu một người có lẽ...", "Lou Hoàng"));
        mSongList.add(new SongModel("S015", "Buồn Của Anh", "Nếu em cần một người...", "Đạt G, DuUyên"));
    }
}
