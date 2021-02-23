package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import vn.com.supertao.datting.R;
import vn.com.supertao.datting.model.object.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHodler> {
    private Context mContext;
    private List<Notification> mNotificationList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDb;
    private String mIdUser;

    public NotificationAdapter(Context mContext, List<Notification> mNotificationList) {
        this.mContext = mContext;
        this.mNotificationList = mNotificationList;
        mAuth = FirebaseAuth.getInstance();
        mIdUser = mAuth.getCurrentUser().getUid();// id người dùng hiện tại
        // data người dùng hiện tại
        mUserDb = FirebaseDatabase.getInstance().getReference().child("user");
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        switch (mNotificationList.get(position).getImageFriend()) {
            case "default":
                break;
            default:
                Glide.with(mContext).load(mNotificationList.get(position).getImageFriend()).into(holder.ivFrindNotification);
                break;
        }

        holder.tvNameNotification.setText(mNotificationList.get(position).getNameFriend());
        holder.tvTimeNotification.setText(mNotificationList.get(position).getTime());
        holder.tvContentNotification.setText(mNotificationList.get(position).getContent());
        if (mNotificationList.get(position).getTempCheck() == 0) {
            holder.btnAccepNotification.setVisibility(View.GONE);
            holder.btnDeleteNotification.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private ImageView ivFrindNotification;
        private TextView tvNameNotification;
        private TextView tvTimeNotification;
        private TextView tvContentNotification;
        private Button btnAccepNotification;
        private Button btnDeleteNotification;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ivFrindNotification = itemView.findViewById(R.id.ivImageFriendNotification);
            tvNameNotification = itemView.findViewById(R.id.tvNameFriendNotification);
            tvTimeNotification = itemView.findViewById(R.id.tvTimeNotification);
            tvContentNotification = itemView.findViewById(R.id.tvContentNotification);
            btnAccepNotification = itemView.findViewById(R.id.btnAcceptNotification);
            btnDeleteNotification = itemView.findViewById(R.id.btnDeleteNotification);
            btnAccepNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String idFriend = mNotificationList.get(position).getId();
                    isConnectionMatch(idFriend);
                    Toast.makeText(mContext, "click choa nhan", Toast.LENGTH_LONG).show();
                    Log.e("CLickAccepF", "ban tot");
                    btnAccepNotification.setVisibility(View.GONE);
                    btnDeleteNotification.setVisibility(View.GONE);
                    mUserDb.child(mIdUser).child("connections").child("yeps").child(idFriend).setValue("1");

                }
            });
            btnDeleteNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String userId = mNotificationList.get(position).getId();
                    mUserDb.child(userId).child("connections").child("nope").child(mIdUser).setValue(true);
                    Log.e("clickXoa", mNotificationList.get(position).getId());
                    btnAccepNotification.setVisibility(View.GONE);
                    btnDeleteNotification.setVisibility(View.GONE);
                    mUserDb.child(mIdUser).child("connections").child("yeps").child(userId).setValue("1");

                }
            });
        }
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = mUserDb.child(mIdUser).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                    // Tạo 1 key ngẫu nhiên
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    mUserDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(mIdUser).child("ChatId").setValue(key);
                    mUserDb.child(mIdUser).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
