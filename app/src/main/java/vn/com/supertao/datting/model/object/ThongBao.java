package vn.com.supertao.datting.model.object;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.com.supertao.datting.R;

public class ThongBao {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private String IdUser;
    private TabLayout tlCategory;

    public ThongBao(TabLayout tlCategory) {
        this.tlCategory = tlCategory;
    }

    public void khoitao() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        IdUser = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(IdUser).child("connections").child("notification");
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
}
