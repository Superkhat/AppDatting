package vn.com.supertao.datting;

import android.content.Context;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.com.supertao.datting.model.object.Diary;
import vn.com.supertao.datting.model.adapter.DiaryAdapter;

public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
    private ProgressBar progressBar;
    private List<Diary> mDiaryList;
    DiaryAdapter diaryAdapter;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private String IdUser;
    private Context mContext;
    private RecyclerView rv;
    private LinearLayoutManager manager1;

    public MyAsyncTask(Context mContext, RecyclerView rv, LinearLayoutManager manager1, ProgressBar progressBar
            , List<Diary> mDiaryList
            , DiaryAdapter diaryAdapter) {
        this.mContext = mContext;
        this.progressBar = progressBar;
        this.mDiaryList = new ArrayList<>();
        this.diaryAdapter = diaryAdapter;
        this.rv = rv;
        this.manager1 = manager1;

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
                    if (ContentDiary.equals("null") != true && ImageCentenDiary.equals("null") != true) {
                        String mydate = "";
                        mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                        Diary m = new Diary(ImageUserDiay, nameUserDiary
                                , ContentDiary, ImageCentenDiary, CommentDiary, mydate);
                        //  mDiaryList.add(m);
                        // diaryAdapter.notifyDataSetChanged();
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
        //Toast.makeText(contextParent, "Start", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Log.e("Start", "bawts dau ");
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Hàm được được hiện tiếp sau hàm onPreExecute()
        //Hàm này thực hiện các tác vụ chạy ngầm
        //Tuyệt đối k vẽ giao diện trong hàm này
        for (int i = 0; i <= 40; i++) {
            SystemClock.sleep(100);
            //khi gọi hàm này thì onProgressUpdate sẽ thực thi
            publishProgress(i);
        }
        KhoiTaoFireBase();
        saveDiaryOfFriendInSQL();
        Log.e("doInBackground", "doInBackgroundsad");
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Hàm thực hiện update giao diện khi có dữ liệu từ hàm doInBackground gửi xuống
        super.onProgressUpdate(values);
        //Thông qua contextCha để lấy được control trong MainActivity
        //  progressBar = (ProgressBar) contextParent.findViewById(R.id.pbLoading);
        //vì publishProgress chỉ truyền 1 đối số
        //nên mảng values chỉ có 1 phần tử
        int number = values[0];
        //tăng giá trị của Progressbar lên
        progressBar.setProgress(number);
        //đồng thời hiện thị giá trị là % lên TextView

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        //  Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
        diaryAdapter = new DiaryAdapter(mContext, mDiaryList);
        manager1 = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager1);
        rv.setAdapter(diaryAdapter);
        diaryAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
        Log.e("End", "Ket Thuc");
    }

    public void saveDiaryOfFriendInSQL() {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
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
                        String ContentDiary = "";
                        ContentDiary = snapshot.child("Status").child("caption").getValue().toString();
                        String ImageCentenDiary = "";
                        ImageCentenDiary = snapshot.child("Status").child("imageStatus").getValue().toString();
                        String mydate = "";
                        mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                        Diary diary = new Diary(ImageUserDiay, nameUserDiary, ContentDiary, ImageCentenDiary, "", mydate);
                        mDiaryList.add(diary);
                        diaryAdapter.notifyDataSetChanged();
                    }
                    if (snapshot.child("connections").child("yeps").child(IdUser).exists() && snapshot.child("Status").child("timeStatus").exists()) {
                        String ImageUserDiay = snapshot.child("image1").getValue().toString();
                        String nameUserDiary = snapshot.child("name").getValue().toString();

                        String ContentDiary = "";
                        ContentDiary = snapshot.child("Status").child("caption").getValue().toString();
                        String ImageCentenDiary = "";
                        ImageCentenDiary = snapshot.child("Status").child("imageStatus").getValue().toString();
                        String mydate = mydate = snapshot.child("Status").child("timeStatus").getValue().toString();
                        Diary diary = new Diary(ImageUserDiay, nameUserDiary, ContentDiary, ImageCentenDiary, "", mydate);
                        mDiaryList.add(diary);
                        diaryAdapter.notifyDataSetChanged();
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

}
