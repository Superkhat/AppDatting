package vn.com.supertao.datting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentListChat extends Fragment {
    private Toolbar tbHeader;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private String mIdUser;
    private ImageView ivUser;
    private TextView tvNameUser;
    private Context mContext;

    public FragmentListChat(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_listchat,container,false);
        tbHeader = view.findViewById(R.id.toolbar_main);
        ivUser = view.findViewById(R.id.ivUser);
        tvNameUser = view.findViewById(R.id.tvNameUser);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbHeader);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        mIdUser = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(mIdUser);
        displayDataOnToolBar();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void displayDataOnToolBar() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(FragmentListChat.this)
                        .load(snapshot.child("image1")
                                .getValue()).into(ivUser);
                // tvNameUser.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
