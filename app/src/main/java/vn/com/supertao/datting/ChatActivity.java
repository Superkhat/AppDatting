package vn.com.supertao.datting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vn.com.supertao.datting.model.adapter.ChatAdapter;
import vn.com.supertao.datting.model.object.Chat;

public class ChatActivity extends AppCompatActivity {
    private Toolbar tbHeader;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private String mIdUser;
    private ImageView ivUser;
    private TextView tvNameUser;
    private ImageView ivLanch;
    private List<Chat> mChats;
    private ChatAdapter mChatAdapter;
    private LinearLayoutManager manager;
    private RecyclerView rvListChat;
    private ConstraintLayout ctlChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViewID();
        setSupportActionBar(tbHeader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        mIdUser = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(mIdUser);
        displayDataOnToolBar();
        mChats = new ArrayList<>();
        getUserMatchId();
        mChatAdapter = new ChatAdapter(this, mChats,ctlChat);
        manager = new LinearLayoutManager(this);
        rvListChat.setLayoutManager(manager);
        rvListChat.setAdapter(mChatAdapter);

    }

    public void findViewID() {
        ctlChat=findViewById(R.id.ctlChat);
        tbHeader = findViewById(R.id.toolbar_main);
        ivUser = findViewById(R.id.ivUser);
        tvNameUser = findViewById(R.id.tvNameUser);
        ivLanch = findViewById(R.id.ivLanch);
        ivLanch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/);
                startActivity(intent);
                finish();
            }
        });
        rvListChat = findViewById(R.id.rvListChat);
    }

    public void displayDataOnToolBar() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(ChatActivity.this)
                        .load(snapshot.child("image1")
                                .getValue()).into(ivUser);
              //  tvNameUser.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserMatchId() {
        mDatabaseReference.child("connections").child("matches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot match : dataSnapshot.getChildren()) {
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("image1").getValue() != null) {
                        profileImageUrl = dataSnapshot.child("image1").getValue().toString();
                    }
                    Chat obj = new Chat(userId,name, "okoko tao la dai ca cua cai lop nay m lam gi dc t", profileImageUrl);
                    mChats.add(obj);
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }


}
