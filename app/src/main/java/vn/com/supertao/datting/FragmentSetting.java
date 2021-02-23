package vn.com.supertao.datting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.com.supertao.datting.model.object.ContentChat;
import vn.com.supertao.datting.presenter.LoginActivity;

public class FragmentSetting extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Context mContext;
    private DatabaseReference mFirebaseDatabase;
    private String idUser;
    private Button btnLogOut;
    private Button btnYouNearHear;
    private Button btnMovie;
    private Button btnNewSpaper;
    private Button btnHelp;
    private LinearLayout llInformation;

    public FragmentSetting(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(idUser);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_setting, container, false);
        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TextView tvName = v.findViewById(R.id.tvNameUserSetting);
                    tvName.setText(snapshot.child("name").getValue().toString());

                    switch (snapshot.child("image1").getValue().toString()) {
                        case "default":
                            break;
                        default:
                            ImageView ivU = v.findViewById(R.id.ivUser);
                            Glide.with(mContext).load(snapshot.child("image1").getValue().toString()).into(ivU);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnHelp = v.findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);
        btnMovie = v.findViewById(R.id.btnMovie);
        btnMovie.setOnClickListener(this);
        btnNewSpaper = v.findViewById(R.id.btnNewSpaper);
        btnNewSpaper.setOnClickListener(this);
        btnYouNearHear = v.findViewById(R.id.btnYouNearHear);
        btnYouNearHear.setOnClickListener(this);
        llInformation = v.findViewById(R.id.llInformation);
        llInformation.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogOut:
                mAuth.signOut();
                SharedPreferences mySPrefs = getActivity().getSharedPreferences("tk",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("taikhoan");
                editor.apply();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btnHelp:
                break;
            case R.id.btnNewSpaper:
                Intent intentNewSpaper=new Intent(mContext,NewsPapper.class);
                startActivity(intentNewSpaper);
                break;
            case R.id.btnYouNearHear:
                break;
            case R.id.btnMovie:
                Intent intentMovie=new Intent(mContext,Movie.class);
                startActivity(intentMovie);
                break;
            case R.id.llInformation:
                Intent intent1 =new Intent(mContext,Information.class);
                startActivity(intent1);
                break;
        }
    }
}
