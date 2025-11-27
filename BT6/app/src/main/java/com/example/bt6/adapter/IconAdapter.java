package com.example.bt6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bt6.R;
import com.example.bt6.model.IconModel;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {
    private Context context;
    private List<IconModel> iconModelList;

    public IconAdapter(Context context, List<IconModel> iconModelList) {
        this.context = context;
        this.iconModelList = iconModelList;
    }

    @NonNull
    @Override
    public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icon_promotion, parent, false);
        return new IconHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconHolder holder, int position) {
        IconModel iconModel = iconModelList.get(position);
        
        // Set image using Glide
        Glide.with(context)
                .load(iconModel.getImgId())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgIcon);
        
        // Set description text
        holder.tvDesc.setText(iconModel.getDesc());
    }

    @Override
    public int getItemCount() {
        return iconModelList != null ? iconModelList.size() : 0;
    }

    // Method for search functionality
    public void setListenerList(List<IconModel> iconModelList) {
        this.iconModelList = iconModelList;
        notifyDataSetChanged();
    }

    public static class IconHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvDesc;

        public IconHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
