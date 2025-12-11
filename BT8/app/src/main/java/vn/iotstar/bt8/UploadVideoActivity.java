package vn.iotstar.bt8;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private VideoView videoPreview;
    private Button btnPickVideo, btnUpload;
    private EditText edtTitle, edtDesc;
    private ProgressBar progressBar;
    private Uri videoUri; // Lưu đường dẫn video đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        videoPreview = findViewById(R.id.videoPreview);
        btnPickVideo = findViewById(R.id.btnPickVideo);
        btnUpload = findViewById(R.id.btnUpload);
        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDesc);
        progressBar = findViewById(R.id.progressBarUpload);

        // 1. Chọn Video
        btnPickVideo.setOnClickListener(v -> openGallery());

        // 2. Upload Video
        btnUpload.setOnClickListener(v -> {
            if (videoUri != null) {
                uploadVideoToFirebase();
            } else {
                Toast.makeText(this, "Vui lòng chọn video!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            // Hiển thị video lên VideoView để check
            videoPreview.setVideoURI(videoUri);
            videoPreview.start();
        }
    }

    private void uploadVideoToFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        btnUpload.setEnabled(false);

        // Tạo tên file ngẫu nhiên dựa trên thời gian
        String fileName = "videos/" + System.currentTimeMillis() + ".mp4";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(fileName);

        // Upload lên Storage
        storageRef.putFile(videoUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Upload thành công -> Lấy link download
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        saveVideoToFirestore(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpload.setEnabled(true);
                    Toast.makeText(this, "Upload thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveVideoToFirestore(String videoUrl) {
        String title = edtTitle.getText().toString();
        String desc = edtDesc.getText().toString();
        String uploaderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        VideoModel video = new VideoModel(videoUrl, title, desc, uploaderId);

        FirebaseFirestore.getInstance().collection("videos")
                .add(video)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đăng video thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình này
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpload.setEnabled(true);
                    Toast.makeText(this, "Lỗi lưu Database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}