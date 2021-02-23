package vn.com.supertao.datting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import vn.com.supertao.datting.model.object.ThongBao;
import vn.com.supertao.datting.model.object.Diary;
import vn.com.supertao.datting.model.adapter.DiaryAdapter;

public class FragmentDiary extends Fragment implements View.OnClickListener {
    private Context mContext;
    private DiaryAdapter mDiaryAdapter;
    private List<Diary> mDiaryList;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private String IdUser;
    private ProgressBar pr;
    private RecyclerView rv;
    private ImageView ivUser;
    private LinearLayoutManager manager1;
    private Boolean temp = false;
    private TabLayout tlCategory;
    private ThongBao mThongBao;

    public FragmentDiary(Context mContext, TabLayout tlCategory) {
        this.mContext = mContext;
        this.tlCategory = tlCategory;
    }

    public void KhoiTaoFireBase() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        IdUser = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(IdUser);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ImageUserDiay = snapshot.child("image1").getValue().toString();
                    String nameUserDiary = snapshot.child("name").getValue().toString();
                    String ContentDiary = "";
                    String ImageCentenDiary = "";
                    if (snapshot.child("Status").exists()) {
                        ContentDiary = snapshot.child("Status").child("caption").getValue().toString();
                        ImageCentenDiary = snapshot.child("Status").child("imageStatus").getValue().toString();
                    }
                    String CommentDiary = "";
                    String mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                    if (ContentDiary.equals("null") != true && ImageCentenDiary.equals("null") != true) {
                        Diary m = new Diary(ImageUserDiay, nameUserDiary
                                , ContentDiary, ImageCentenDiary, CommentDiary, mydate);
                        mDiaryList.add(m);
                        return;
                        //Toast.makeText(mContext, "okokokokok" + diary.getNameUserDiary()+mDiaryList.size(), Toast.LENGTH_LONG).show();
                    }

                    // Toast.makeText(mContext, "okokokokok" +snapshot.child("Status").child("imageStatus").getValue(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void saveDiaryOfFriendInSQL() {

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Log.e("ChayVaoSave", "124325435");
                    String IdFriend = snapshot.getKey();
                    if (IdFriend.equals(IdUser)) {
                        Log.e("ChayVaoSave2", snapshot.getKey());
                        String ImageUserDiay = snapshot.child("image1").getValue().toString();
                        String nameUserDiary = snapshot.child("name").getValue().toString();
                        String ContentDiary = " ";
                        if (snapshot.child("Status").child("timeStatus").exists()) {
                            ContentDiary = snapshot.child("Status").child("caption").getValue().toString();
                            String ImageCentenDiary = "";
                            ImageCentenDiary = snapshot.child("Status").child("imageStatus").getValue().toString();
                            String mydate = "1 ngày";
                            mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                            Diary diary = new Diary(ImageUserDiay, nameUserDiary, ContentDiary, ImageCentenDiary, "", mydate);
                            mDiaryList.add(diary);
                            mDiaryAdapter.notifyDataSetChanged();
                        }

                    }
                    if (snapshot.child("connections").child("yeps").child(IdUser).exists() && snapshot.child("Status").child("timeStatus").exists()) {
                        String ImageUserDiay = snapshot.child("image1").getValue().toString();
                        String nameUserDiary = snapshot.child("name").getValue().toString();
                        String ContentDiary = "";
                        ContentDiary = snapshot.child("Status").child("caption").getValue().toString();
                        String ImageCentenDiary = "";
                        ImageCentenDiary = snapshot.child("Status").child("imageStatus").getValue().toString();
                        String mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                        Diary diary = new Diary(ImageUserDiay, nameUserDiary, ContentDiary, ImageCentenDiary, "", mydate);
                        mDiaryList.add(diary);
                        mDiaryAdapter.notifyDataSetChanged();
                        Log.e("IDBAn", IdFriend);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDiaryList = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        IdUser = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        // KhoiTaoFireBase();
        saveDiaryOfFriendInSQL();
        Log.e("SizeOKOKOKO", mDiaryList.size() + "");
        Toast.makeText(mContext, "okokokokok" + mDiaryList.size(), Toast.LENGTH_LONG).show();
        mDiaryAdapter = new DiaryAdapter(mContext, mDiaryList);
        mThongBao = new ThongBao(tlCategory);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary, container, false);
        ivUser = v.findViewById(R.id.ivImageUerDiaryStatus);
        mDatabaseReference.child(IdUser).child("image1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    Glide.with(mContext).load(snapshot.getValue().toString()).into(ivUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pr = v.findViewById(R.id.pbLoading);
        rv = v.findViewById(R.id.rvDiary);
        manager1 = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager1);
        rv.setAdapter(mDiaryAdapter);
        mDiaryAdapter.notifyDataSetChanged();
        Button btnStatus = v.findViewById(R.id.btnSatus);
        btnStatus.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, DialogStatus.class);
        mContext.startActivity(intent);

        Log.e("click", temp+"");
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences= mContext.getSharedPreferences("huyPost",Context.MODE_PRIVATE);
        temp=preferences.getBoolean("check",false);
        if (temp == true) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(mContext, rv, manager1, pr, mDiaryList, mDiaryAdapter);
            myAsyncTask.execute();
        }


        Log.e("sad", "onresum");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("sad", "onPau");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("sad", "onDistraoi");
    }

    @Override
    public void onStart() {
        super.onStart();
        mThongBao.khoitao();
        mThongBao.thongbaoChuyenIcon();
    }
}
