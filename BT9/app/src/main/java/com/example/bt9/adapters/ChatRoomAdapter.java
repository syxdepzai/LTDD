package com.example.bt9.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.R;
import com.example.bt9.models.ChatRoom;
import com.example.bt9.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    
    private List<ChatRoom> chatRooms;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(ChatRoom room);
    }

    public ChatRoomAdapter(OnRoomClickListener listener) {
        this.chatRooms = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom room = chatRooms.get(position);
        
        holder.customerNameText.setText(room.getCustomerName());
        
        if (room.getLastMessage() != null) {
            holder.lastMessageText.setText(room.getLastMessage().getMessage());
            holder.timeText.setText(DateUtils.formatTimestamp(room.getLastMessage().getTimestamp()));
        } else {
            holder.lastMessageText.setText("Chưa có tin nhắn");
            holder.timeText.setText("");
        }
        
        if (room.getUnreadCount() > 0) {
            holder.unreadBadge.setVisibility(View.VISIBLE);
            holder.unreadBadge.setText(String.valueOf(room.getUnreadCount()));
        } else {
            holder.unreadBadge.setVisibility(View.GONE);
        }
        
        holder.roomCard.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoomClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public void setRooms(List<ChatRoom> rooms) {
        this.chatRooms = rooms;
        notifyDataSetChanged();
    }

    public void addRoom(ChatRoom room) {
        chatRooms.add(0, room);
        notifyItemInserted(0);
    }

    public void updateRoom(ChatRoom room) {
        for (int i = 0; i < chatRooms.size(); i++) {
            if (chatRooms.get(i).getRoomId().equals(room.getRoomId())) {
                chatRooms.set(i, room);
                notifyItemChanged(i);
                break;
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView roomCard;
        TextView customerNameText;
        TextView lastMessageText;
        TextView timeText;
        TextView unreadBadge;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomCard = itemView.findViewById(R.id.roomCard);
            customerNameText = itemView.findViewById(R.id.customerNameText);
            lastMessageText = itemView.findViewById(R.id.lastMessageText);
            timeText = itemView.findViewById(R.id.timeText);
            unreadBadge = itemView.findViewById(R.id.unreadBadge);
        }
    }
}

