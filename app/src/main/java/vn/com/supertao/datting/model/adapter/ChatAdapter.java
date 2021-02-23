package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;


import vn.com.supertao.datting.FragmentChat;
import vn.com.supertao.datting.R;
import vn.com.supertao.datting.model.object.Chat;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> chatList;
    private ConstraintLayout cltChat;

    public ChatAdapter(Context mContext, List<Chat> chatList, ConstraintLayout cltChat) {
        this.mContext = mContext;
        this.chatList = chatList;
        this.cltChat = cltChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNameFriend.setText(chatList.get(position).getName());
        holder.tvContentChatFriend.setText(chatList.get(position).getContentMessage());
        switch (chatList.get(position).getImageMessage()) {
            case "default":
                break;
            default:
                Glide.with(mContext).load(chatList.get(position).getImageMessage()).into(holder.ivFriend);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameFriend;
        private TextView tvContentChatFriend;
        private ImageView ivFriend;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFriend = itemView.findViewById(R.id.tvNameFriendChat);
            tvContentChatFriend = itemView.findViewById(R.id.tvContentChatFriend);
            ivFriend = itemView.findViewById(R.id.ivFriendChat);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    cltChat.setVisibility(View.GONE);
                    FragmentManager fmManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fmTransaction = fmManager.beginTransaction();
                    Fragment mFragment = new FragmentChat(mContext,cltChat);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",chatList.get(position).getId());// gửi dữ liệu sang fragment
                    mBundle.putString("name",chatList.get(position).getName());// gửi dữ liệu sang fragment
                    mBundle.putString("image",chatList.get(position).getImageMessage());// gửi dữ liệu sang fragment
                    mFragment.setArguments(mBundle);
                    fmTransaction.add(R.id.flChat, mFragment);
                    fmTransaction.commit();
                }
            });
        }
    }

}
