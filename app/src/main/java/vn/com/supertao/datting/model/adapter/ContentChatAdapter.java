package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

import vn.com.supertao.datting.R;
import vn.com.supertao.datting.model.object.ContentChat;

public class ContentChatAdapter extends RecyclerView.Adapter<ContentChatAdapter.ViewHodler> {
    private Context mContext;
    private List<ContentChat> mContentChats;

    public ContentChatAdapter(Context mContext, List<ContentChat> mContentChats) {
        this.mContext = mContext;
        this.mContentChats = mContentChats;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_contentchat,parent,false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        holder.tvContentSend.setText(mContentChats.get(position).getContent());
        holder.tvContentTake.setText(mContentChats.get(position).getContent());
        switch (mContentChats.get(position).getImage()) {
            case "default":
                break;
            default:
                Glide.with(mContext).load(mContentChats.get(position).getImage()).into(holder.ivUserTake);
                break;
        }
        holder.tvTimeSend.setText(mContentChats.get(position).getTime());
        holder.tvTimeTake.setText(mContentChats.get(position).getTime());
        if(mContentChats.get(position).getCurrentUser()){
            holder.llTake.setVisibility(View.GONE);
            holder.llSend.setVisibility(View.VISIBLE);
            holder.tvContentSend.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvContentSend.setBackgroundResource(R.drawable.custom_chat_send);
        }else{
            holder.llTake.setVisibility(View.VISIBLE);
            holder.llSend.setVisibility(View.GONE);
            holder.tvContentTake.setTextColor(Color.parseColor("#404040"));
            holder.tvContentTake.setBackgroundResource(R.drawable.custom_chat_take);
        }
    }

    @Override
    public int getItemCount() {
        return mContentChats.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private LinearLayout llSend;
        private LinearLayout llTake;
        private ImageView ivUserTake;
        private TextView tvContentSend;
        private TextView tvTimeSend;
        private TextView tvContentTake;
        private TextView tvTimeTake;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            llSend=itemView.findViewById(R.id.llSend);
            llTake=itemView.findViewById(R.id.llTake);
            ivUserTake=itemView.findViewById(R.id.ivUserContentChatTake);
            tvContentSend=itemView.findViewById(R.id.tvContentChatSend);
            tvTimeSend=itemView.findViewById(R.id.tvTimeContentChatSend);
            tvContentTake=itemView.findViewById(R.id.tvContentChatTake);
            tvTimeTake=itemView.findViewById(R.id.tvTimeContentChatTake);
        }
    }
}
