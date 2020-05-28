package com.ervin.kiwariandroidtest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ervin.kiwariandroidtest.model.Chat;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.Utils;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<Chat> chats;
    Activity activity;

    public ChatAdapter(List<Chat> chats, Activity activity) {
        this.chats = chats;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.txtTimestamp.setText(Utils.dateFormat(chat.getTimestamp()));
        holder.txtMessage.setText(chat.getMessage());
        holder.txtName.setText(chat.getSenderName());
        Glide.with(activity)
                .load(chat.getSenderImage())
                .into(holder.ivImageChat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtMessage, txtTimestamp;
        ImageView ivImageChat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_namechat);
            txtMessage = itemView.findViewById(R.id.txt_messagechat);
            txtTimestamp = itemView.findViewById(R.id.txt_timestamp);
            ivImageChat = itemView.findViewById(R.id.iv_chat);
        }
    }
}
