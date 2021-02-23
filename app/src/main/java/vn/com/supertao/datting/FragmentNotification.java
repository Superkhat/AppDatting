package vn.com.supertao.datting;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.com.supertao.datting.model.adapter.NotificationAdapter;
import vn.com.supertao.datting.model.object.Notification;

public class FragmentNotification extends Fragment {
    private Context mContext;
    private RecyclerView rvNotification;
    private List<Notification> mNotificationList;
    private NotificationAdapter mNotificationAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private String IdUser;
    private TabLayout tlCategory;

    public FragmentNotification(Context mContext, TabLayout tlCategory) {
        this.tlCategory = tlCategory;
        this.mContext = mContext;
    }


    public void Khoitao() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        IdUser = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        mDatabaseReference.child(IdUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("connections").child("notification").exists())
                        if (snapshot.child("connections").child("notification").getValue().equals("true")) {
                            tlCategory.getTabAt(2).setIcon(R.drawable.ic_notifications);
                            mDatabaseReference.child("connections").child("notification").setValue("false");
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Khoitao();
        mNotificationList = new ArrayList<>();
        dataFriendNotifiaction();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        rvNotification = v.findViewById(R.id.rvNotification);

        mNotificationAdapter = new NotificationAdapter(mContext, mNotificationList);

        linearLayoutManager = new LinearLayoutManager(mContext);
        rvNotification.setLayoutManager(linearLayoutManager);
        rvNotification.setAdapter(mNotificationAdapter);
        mNotificationAdapter.notifyDataSetChanged();
        return v;
    }

    public void dataFriendNotifiaction() {
        mDatabaseReference.child(IdUser).child("connections").child("yeps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Log.e("dâtFriend", "cahy vaodataFriend");
                    if (snapshot.exists()) {
                        String idfriend = snapshot.getValue().toString();
                        Log.e("dâtFriend", idfriend);
                        if (!idfriend.equals("1"))
                            profileFriend(idfriend);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void profileFriend(final String id) {
        mDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue().toString();
                    String image = snapshot.child("image1").getValue().toString();
                    String time = "vài phút trước";
                    String content = "Gửi lời mời kết bạn";
                    Notification n = new Notification(name, image, time, content, 1, id);
                    mNotificationList.add(n);
                    mNotificationAdapter.notifyDataSetChanged();
                    Log.e("profileFriend", name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void profileFriendID1(final String id) {
        mDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int tempCheck = 1;
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue().toString();
                    String image = snapshot.child("image1").getValue().toString();
                    String time = "vài phút trước";
                    String content = "Gửi lời mời kết bạn";
                    Notification n = new Notification(name, image, time, content, 1, id);
                    mNotificationList.add(n);
                    mNotificationAdapter.notifyDataSetChanged();
                    Log.e("profileFriend", name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Khoitao();
    }

    @Override
    public void onStart() {
        super.onStart();
        Khoitao();
    }
}
