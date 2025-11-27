package com.example.bt6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.bt6.R;
import com.example.bt6.model.ImagesSlider;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private List<ImagesSlider> imagesList;

    public ImageSliderAdapter(Context context, List<ImagesSlider> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @Override
    public int getCount() {
        return imagesList != null ? imagesList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image_slider, container, false);

        ImageView imageView = view.findViewById(R.id.imageSlider);
        
        // Load image using Glide
        Glide.with(context)
                .load(imagesList.get(position).getAvatar())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void updateData(List<ImagesSlider> newImagesList) {
        this.imagesList = newImagesList;
        notifyDataSetChanged();
    }
}
