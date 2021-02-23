package vn.com.supertao.datting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import link.fls.swipestack.SwipeStack;
import vn.com.supertao.datting.model.object.ThongBao;
import vn.com.supertao.datting.model.adapter.SwiperStackAdapter;
import vn.com.supertao.datting.model.object.User;

public class FragmentDiscover extends Fragment implements SwipeStack.SwipeStackListener {
    private Context mContext;
    private SwipeStack swipeStackUser;
    private SwiperStackAdapter swiperStackAdapter;
    private List<User> mUsers;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDb;
    private String mIdUser;
    private ThongBao mThongBao;
    private TabLayout tlCategory;
    private Button btnMessage;

    public FragmentDiscover(Context mContext, TabLayout tlCategory) {
        this.mContext = mContext;
        this.tlCategory = tlCategory;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mIdUser = mAuth.getCurrentUser().getUid();// id người dùng hiện tại
        // data người dùng hiện tại
        mUserDb = FirebaseDatabase.getInstance().getReference().child("user");
        mUsers = new ArrayList<>();
        checkSexUser();
        Log.e("SzeSSSS", mUsers.size() + "");

        mThongBao = new ThongBao(tlCategory);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        btnMessage=v.findViewById(R.id.btnMessage);
          btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        swipeStackUser = v.findViewById(R.id.ssUser);
        swiperStackAdapter = new SwiperStackAdapter(mContext, mUsers);
        swipeStackUser.setAdapter(swiperStackAdapter);
        swipeStackUser.setListener(this);
        return v;
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String userId = swiperStackAdapter.getItem(position);
        mUserDb.child(userId).child("connections").child("nope").child(mIdUser).setValue(true);
        Toast.makeText(mContext, "bên trái " + userId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String userId = swiperStackAdapter.getItem(position);
        mUserDb.child(userId).child("connections").child("yeps").child(mIdUser).setValue(mIdUser);
        mUserDb.child(userId).child("connections").child("notification").setValue("true");
        isConnectionMatch(userId);
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

    @Override
    public void onStackEmpty() {

    }

    String userSex;
    String tempUserSex;

    public void checkSexUser() {
        //  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userdb = mUserDb.child(mIdUser);
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("sex").getValue() != null) {
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Nam":
                                tempUserSex = "Nữ";
                                break;
                            case "Nữ":
                                tempUserSex = "Nam";
                                break;
                        }
                        filterUsers();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void filterUsers() {
        mUserDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists()
                            && !dataSnapshot.child("connections").child("nope").hasChild(mIdUser)
                            && !dataSnapshot.child("connections").child("yeps").hasChild(mIdUser)
                            && dataSnapshot.child("sex").getValue().toString().equals(tempUserSex)) {
                        String image1 = "default";
                        if (!dataSnapshot.child("image1").getValue().equals("default")) {
                            image1 = dataSnapshot.child("image1").getValue().toString();
                        }
                        User m = new User(dataSnapshot.getKey()
                                , dataSnapshot.child("name").getValue().toString(), "12",
                                22, "65789", "1",
                                "2", dataSnapshot.child("chamNgonSong").getValue().toString()
                                , "d1", "123", image1, "ds",
                                "asd", "das");
                        mUsers.add(m);
                        swiperStackAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mThongBao.khoitao();
        mThongBao.thongbaoChuyenIcon();
    }
}
