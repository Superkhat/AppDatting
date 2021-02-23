package vn.com.supertao.datting.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.supertao.datting.R;

public class UpImage extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivImage1;
    private ImageView ivImage2;
    private ImageView ivImage3;
    private ImageView ivImage4;
    private ImageView ivImage5;
    private ImageView ivImage6;
    private Intent mIntent;
    private Map<String,Uri> mResultUri;
    private int temp=0;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private  String userId;
    private Button btnHoanThanh;
    public UpImage() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_image);
        findViewID();
        mResultUri=new HashMap<>();
        mFirebaseAuth=FirebaseAuth.getInstance();
        userId=mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("user").child(userId);
        setOnclik();

    }
    public void findViewID()
    {
        ivImage1=findViewById(R.id.iv1);
        ivImage2=findViewById(R.id.iv2);
        ivImage3=findViewById(R.id.iv3);
        ivImage4=findViewById(R.id.iv4);
        ivImage5=findViewById(R.id.iv5);
        ivImage6=findViewById(R.id.iv6);
        btnHoanThanh=findViewById(R.id.btnHoanThanh);
    }
    public void setOnclik()
    {
        ivImage1.setOnClickListener(this);
        ivImage2.setOnClickListener(this);
        ivImage3.setOnClickListener(this);
        ivImage4.setOnClickListener(this);
        ivImage5.setOnClickListener(this);
        ivImage6.setOnClickListener(this);
        btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent =new Intent(UpImage.this,WelcomeActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv1:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,1);
                temp=1;
                break;
            case R.id.iv2:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,2);
                temp=2;
                break;
            case R.id.iv3:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,3);
                temp=3;
                break;
            case R.id.iv4:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,4);
                temp=4;
                break;
            case R.id.iv5:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,5);
                temp=5;
                break;
            case R.id.iv6:
                mIntent=new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,6);
                temp=6;
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri1=data.getData();
            mResultUri.put("iv1",ImageUri1);
            ivImage1.setImageURI(ImageUri1);
            upAnhLenFireBase("iv1");
        }
       else if(requestCode==2&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri2=data.getData();
            mResultUri.put("iv2",ImageUri2);
            ivImage2.setImageURI(ImageUri2);
            upAnhLenFireBase("iv2");
        }
        else if(requestCode==3&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri3=data.getData();
            mResultUri.put("iv3",ImageUri3);
            ivImage3.setImageURI(ImageUri3);
            upAnhLenFireBase("iv3");
        }
        else if(requestCode==4&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri4=data.getData();
            mResultUri.put("iv4",ImageUri4);
            ivImage4.setImageURI(ImageUri4);
            upAnhLenFireBase("iv4");
        }
        else if(requestCode==5&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri5=data.getData();
            mResultUri.put("iv5",ImageUri5);
            ivImage5.setImageURI(ImageUri5);
            upAnhLenFireBase("iv5");
        }
        else if(requestCode==6&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri6=data.getData();
            mResultUri.put("iv6",ImageUri6);
            ivImage6.setImageURI(ImageUri6);
            upAnhLenFireBase("iv6");
        }

    }
/*

* up ảnh lên firebase
‐ @created_by ncthanh on 25/1/2021
*/
    private void upAnhLenFireBase(String key) {
// up anh len fire base
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("image").child(userId).child(key);
        ref.putFile(mResultUri.get(key))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(UpImage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpImage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    //pickUriImage(1,"image1","iv1");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
