package vn.com.supertao.datting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import vn.com.supertao.datting.model.adapter.FragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private FragmentAdapter fragmentAdapter;
    private TabLayout tlCategory;
    private Toolbar tbHeader;
    private Button btnMessage;
    private ViewPager vpContent;
    private List<Fragment> mFragmentList;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private String IdUser;
    private int check=0;
    public void khoitao() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        IdUser = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(IdUser).child("connections").child("notification");
    }

    public void findviewID() {
        //tbHeader = findViewById(R.id.toolbar_main);
       // setSupportActionBar(tbHeader);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // btnMessage = findViewById(R.id.btnMessage);
        tlCategory = findViewById(R.id.tlCategory);
        vpContent = findViewById(R.id.vp_Content);
    }

    public void addFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragmentDiscover(this, tlCategory));
        mFragmentList.add(new FragmentDiary(this, tlCategory));
        mFragmentList.add(new FragmentNotification(this, tlCategory));
        mFragmentList.add(new FragmentSetting(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviewID();
        khoitao();
      /*  btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/
        addFragment();
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(mFragmentList);
        vpContent.setAdapter(fragmentAdapter);
        tlCategory.setupWithViewPager(vpContent);
        tlCategory.getTabAt(0).setIcon(R.drawable.ic_favorite);
        tlCategory.getTabAt(1).setIcon(R.drawable.ic_discover);
        tlCategory.getTabAt(2).setIcon(R.drawable.ic_notifications);
        tlCategory.getTabAt(3).setIcon(R.drawable.ic_settings);

        thongbaoChuyenIcon();

    }

    @Override
    protected void onStart() {
        super.onStart();
        thongbaoChuyenIcon();
    }

    public void thongbaoChuyenIcon() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().equals("true")) {
                        tlCategory.getTabAt(2).setIcon(R.drawable.ic_notifications_active);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                // Intent intent = new Intent(this, MainActivity.class);
                //     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //   startActivity(intent);
             //   Log.e("back", "back toi ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
