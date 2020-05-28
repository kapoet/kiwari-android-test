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
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> users;
    Activity activity;
    clickListener clickListener;

    public UserAdapter(List<User> users, Activity activity, clickListener clickListener) {
        this.users = users;
        this.activity = activity;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvUsername.setText(users.get(position).getName());
        Glide.with(activity)
                .load(users.get(position).getImgURL())
                .into(holder.ivImgUser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImgUser;
        TextView tvUsername;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImgUser = itemView.findViewById(R.id.iv_userimage);
            tvUsername = itemView.findViewById(R.id.txt_username);
        }
    }

    public interface clickListener {
        void onItemClick(User user);
    }
}
