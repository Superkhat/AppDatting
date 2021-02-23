package vn.com.supertao.datting;

import android.content.Context;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.supertao.datting.model.adapter.ContentChatAdapter;
import vn.com.supertao.datting.model.object.ContentChat;


public class FragmentChat extends Fragment implements View.OnClickListener {
    private Toolbar tbHeader;
    private Context mContext;
    private ConstraintLayout ctlChatUser;
    private ImageView ivUser;
    private TextView tvName;
    private Button btnBack;
    private Button btnSend;
    private EditText edtContentText;
    private String matchId;
    private String image;
    private String name;
    private ConstraintLayout ctlChat;
    private RecyclerView rvMessage;
    private ContentChatAdapter mContentChatAdapter;
    private List<ContentChat> mContentChatList;
    private String currentUserID;
    private String chatId;
    DatabaseReference mDatabaseUser, mDatabaseChat;
    public FragmentChat(Context mContext,ConstraintLayout ctlChat) {
        this.mContext = mContext;
        this.ctlChat=ctlChat;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId= this.getArguments().getString("id");
        image= this.getArguments().getString("image");
        name= this.getArguments().getString("name");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("user")
                .child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");
        mContentChatList=new ArrayList<>();
        getChatId();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat,container,false);
        ctlChatUser=view.findViewById(R.id.ctlChatUser);
        tbHeader = view.findViewById(R.id.toolbar_main);
        ivUser=view.findViewById(R.id.ivUser);
        switch (image) {
            case "default":
                break;
            default:
                Glide.with(mContext).load(image).into(ivUser);
                break;
        }

        edtContentText=view.findViewById(R.id.edtContentText);
        rvMessage=view.findViewById(R.id.rvMessage);
        rvMessage.setNestedScrollingEnabled(false);
        rvMessage.setHasFixedSize(false);
        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        rvMessage.setLayoutManager(manager);
        mContentChatAdapter=new ContentChatAdapter(mContext,mContentChatList);
        rvMessage.setAdapter(mContentChatAdapter);

        tvName=view.findViewById(R.id.tvNameUser);
        tvName.setText(name);
        btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnSend=view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnBack:
                ctlChatUser.setVisibility(View.GONE);
                ctlChat.setVisibility(View.VISIBLE);
                onDestroy();
                break;
            case R.id.btnSend:
                sendMessage();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void sendMessage() {
        String sendMessageText = edtContentText.getText().toString().trim();
        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            newMessage.put("time",mydate);
            newMessageDb.setValue(newMessage);
        }
        edtContentText.setText(null);
    }

    private void getChatId(){
        Log.e("checknoooler","chay vao getChatID");
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
  /*  private void getChatMessages() {
       mDatabaseChat.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Log.e("checknoooler",snapshot.child("text").getValue().toString());
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
    }*/

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int a=0;
                a++;
                Log.e("checknoooler",a+"");
                if(dataSnapshot.exists()){

                    String message = null;
                    String createdByUser = null;
                    String time=null;
                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(dataSnapshot.child("time").getValue()!=null){
                        time = dataSnapshot.child("time").getValue().toString();
                    }
                    Boolean currentUserBoolean;
                    if(message!=null && createdByUser!=null){
                      currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        Log.e("checknoooler",currentUserBoolean.toString());
                        ContentChat newMessage = new ContentChat(matchId,image,message,time,currentUserBoolean);
                        mContentChatList.add(newMessage);
                        mContentChatAdapter.notifyDataSetChanged();
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

}
