package com.example.bt9.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt9.R;
import com.example.bt9.models.ChatMessage;
import com.example.bt9.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {
    
    private List<ChatMessage> messages;
    private String currentUserId;
    private String currentUserType;

    public ChatMessageAdapter(String currentUserId, String currentUserType) {
        this.messages = new ArrayList<>();
        this.currentUserId = currentUserId;
        this.currentUserType = currentUserType;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        boolean isSentByMe = message.getSenderId().equals(currentUserId);

        holder.messageText.setText(message.getMessage());
        holder.timeText.setText(DateUtils.formatTimestamp(message.getTimestamp()));
        holder.senderNameText.setText(message.getSenderName());

        // Căn chỉnh tin nhắn
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageCard.getLayoutParams();
        if (isSentByMe) {
            // Tin nhắn của mình - bên phải, màu xanh
            params.gravity = Gravity.END;
            holder.messageCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.message_sent_bg));
            holder.messageText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.timeText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.senderNameText.setVisibility(View.GONE);
        } else {
            // Tin nhắn của người khác - bên trái, màu xám
            params.gravity = Gravity.START;
            holder.messageCard.setCardBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.message_received_bg));
            holder.messageText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.timeText.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            holder.senderNameText.setVisibility(View.VISIBLE);
        }
        holder.messageCard.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        CardView messageCard;
        TextView messageText;
        TextView timeText;
        TextView senderNameText;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageCard = itemView.findViewById(R.id.messageCard);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
            senderNameText = itemView.findViewById(R.id.senderNameText);
        }
    }
}

