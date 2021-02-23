package vn.com.supertao.datting.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import vn.com.supertao.datting.MainActivity;
import vn.com.supertao.datting.R;

public class WelcomeActivity extends AppCompatActivity {
    private Button btnAccept;
    private FirebaseAuth mAuth;
    private String IdUser;
    private DatabaseReference mDatabaseReference;
    private String KEY_IMAGE[]={"image1","image2","image3","image4","image5","image6"};
    private String NAME_IMAGE[]={"iv1","iv2","iv3","iv4","iv5","iv6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth=FirebaseAuth.getInstance();
        IdUser=mAuth.getCurrentUser().getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("user").child(IdUser);
        // lưu địa chỉ 6 ảnh vào firebase
        for(int i=0;i<KEY_IMAGE.length;++i)
        {
            saveUrlImage(KEY_IMAGE[i],NAME_IMAGE[i]);
        }
        btnAccept=findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    /*
    * hàm lưu url ảnh lên firebase
    ‐ @created_by ncthanh on 26/1/2021
    */
    public void saveUrlImage(final String KeyImage ,String nameImage)
    {
            // lay link anh
            StorageReference filepath = FirebaseStorage.getInstance()
                    .getReference().child("image")
                    .child(IdUser ).child(nameImage);
            Log.e("IDUSER",IdUser);
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Map userInfo = new HashMap();
                    userInfo.put(KeyImage, uri.toString());
                    Log.e("linkImage",uri.toString());
                    mDatabaseReference.updateChildren(userInfo);
                    // finish();
                    return;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("loiKhong",e.toString());
                    // finish();
                }
            });
        }
}
