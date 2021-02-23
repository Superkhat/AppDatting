package vn.com.supertao.datting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.com.supertao.datting.model.object.Diary;

public class DialogStatus extends AppCompatActivity {
    private EditText edtCaption;
    private ImageView ivImagePost;
    private FirebaseAuth mAuth;
    private String IdUser;
    private DatabaseReference mDatabaseReference;
    private Map<String, Uri> mResultUri;
    private Button btnCancel;
    private Button btnPost;
    private Diary diary;
    private int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_post);
        mResultUri = new HashMap<>();

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences userTK= getApplication().getSharedPreferences("huyPost",MODE_PRIVATE);
                SharedPreferences.Editor editor=userTK.edit();
                editor.putBoolean("check",false);
                editor.commit();
                finish();
            }
        });
        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences userTK= getApplication().getSharedPreferences("huyPost",MODE_PRIVATE);
                SharedPreferences.Editor editor=userTK.edit();
                editor.putBoolean("check",true);
                editor.commit();
                if(temp==1) {
                    saveUrlImage("imageStatus", "iv1");
                }
                else
                {
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    Map userInfo = new HashMap();
                    userInfo.put("imageStatus","default");
                    userInfo.put("timeStatus",mydate);
                    userInfo.put("caption", edtCaption.getText().toString().trim());
                    mDatabaseReference.updateChildren(userInfo);
                }

                finish();
            }
        });
        ivImagePost = findViewById(R.id.ivImagePost);

        ivImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent, 1);
                temp=1;
            }
        });


        mAuth = FirebaseAuth.getInstance();
        IdUser = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(IdUser).child("Status");
        edtCaption = findViewById(R.id.edtCaption);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri ImageUri = data.getData();
            mResultUri.put("iv1", ImageUri);
            ivImagePost.setImageURI(ImageUri);
            upAnhLenFireBase("iv1");
        }
    }

    private void upAnhLenFireBase(String key) {
// up anh len fire base
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("imageStatus").child(IdUser).child(key);
        ref.putFile(mResultUri.get(key))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(DialogStatus.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DialogStatus.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }

    /*
    *Ghi lên link ảnh và caption lên  Firebase
    * keyImage là key
    * nameImage là value
    ‐ @created_by ncthanh on 31//1/2021
    */
    public void saveUrlImage(final String KeyImage, String nameImage) {
        // lay link anh
        StorageReference filepath = FirebaseStorage.getInstance()
                .getReference().child("imageStatus")
                .child(IdUser).child(nameImage);
        Log.e("IDUSER", IdUser);
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Map userInfo = new HashMap();
                userInfo.put("timeStatus",mydate);
                userInfo.put(KeyImage, uri.toString());
                userInfo.put("caption", edtCaption.getText().toString().trim());
                Log.e("linkImage", uri.toString());
                mDatabaseReference.updateChildren(userInfo);
                // finish();
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("loiKhong", e.toString());
                // finish();
            }
        });
    }


}
