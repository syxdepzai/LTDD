package vn.iotstar.bt8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPagerVideos;
    private FloatingActionButton fabUpload;
    private CircleImageView imgUserProfile;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // List chứa dữ liệu video
    private List<VideoModel> videoList;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 2. Ánh xạ View
        viewPagerVideos = findViewById(R.id.viewPagerVideos);
        fabUpload = findViewById(R.id.fabUpload);
        imgUserProfile = findViewById(R.id.imgUserProfile);
        progressBar = findViewById(R.id.progressBar);

        // 3. Sự kiện bấm nút Upload
        fabUpload.setOnClickListener(v -> {
            // Chuyển sang màn hình Upload (sẽ tạo sau)
            startActivity(new Intent(MainActivity.this, UploadVideoActivity.class));
        });

        // 4. Sự kiện bấm vào Avatar (Mở Profile hoặc Đăng xuất)
        imgUserProfile.setOnClickListener(v -> {
            // Tạm thời làm chức năng Đăng xuất để test
            mAuth.signOut();
            checkUserLogin();
        });

        // 5. Load dữ liệu
        videoList = new ArrayList<>();
        // Tạo Adapter (Sẽ tạo class này ở bài sau)
        videoAdapter = new VideoAdapter(this, videoList);
        viewPagerVideos.setAdapter(videoAdapter);

        fetchVideosFromFirestore();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLogin();
        loadUserInfo();
    }

    // Kiểm tra đăng nhập
    private void checkUserLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Chưa đăng nhập -> Về trang Login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Hiển thị Avatar người dùng hiện tại lên góc
    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(imgUserProfile);
        }
    }

    // Lấy danh sách video từ Firebase Firestore
    private void fetchVideosFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("videos")
                .get() // Lấy tất cả video
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    if (!queryDocumentSnapshots.isEmpty()) {
                        videoList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Convert dữ liệu JSON thành VideoModel
                            VideoModel video = document.toObject(VideoModel.class);
                            videoList.add(video);
                        }
                        // Cập nhật giao diện
                        videoAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Chưa có video nào!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi tải video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}