package vn.iotstar.bt8;

public class VideoModel {
    private String videoUrl;      // Đường dẫn video trên Storage
    private String title;         // Tiêu đề
    private String description;   // Mô tả
    private String uploaderId;    // Ai đăng video này?

    // Bắt buộc phải có Constructor rỗng cho Firebase
    public VideoModel() {
    }

    public VideoModel(String videoUrl, String title, String description, String uploaderId) {
        this.videoUrl = videoUrl;
        this.title = title;
        this.description = description;
        this.uploaderId = uploaderId;
    }

    // Getter và Setter
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUploaderId() { return uploaderId; }
    public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
}