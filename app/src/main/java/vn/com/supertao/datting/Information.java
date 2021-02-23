package vn.com.supertao.datting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.supertao.datting.model.adapter.ImageAdapter;
import vn.com.supertao.datting.presenter.UpImage;

public class Information extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private String idUser;
    private ImageView image1;
    private ImageView image2;
    private RecyclerView rvImage;
    private TextView tvName;
    private TextView tvCNS;
    private EditText edtSdt;
    private EditText edtHobbies;
    private EditText edtBirthDay;
    private List<String> urlImage;
    private ImageAdapter imageAdapter;
    private LinearLayoutManager manager;
    private Button btnEditInfor;
    private Button btnEditImageBia;
    private Button btnEditImageAvata;
    private Intent upImage;
    private Map<String,Uri> mResultUri;
    private int temp=0;
    void findViewByID() {
        mResultUri=new HashMap<>();
        image2 = findViewById(R.id.ivEditImageBia);
        image1 = findViewById(R.id.ivEditAvata);
        rvImage = findViewById(R.id.rvImageInformation);
        tvName = findViewById(R.id.tvNameUser);
        edtSdt = findViewById(R.id.edtPhoneNumber);
        edtBirthDay = findViewById(R.id.edtBirthDay);
        edtHobbies = findViewById(R.id.edtHobbies);
        tvCNS = findViewById(R.id.tvCNS);
        urlImage = new ArrayList<>();
        manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvImage.setLayoutManager(manager);
        btnEditInfor=findViewById(R.id.btnEditInfor);
        btnEditImageAvata=findViewById(R.id.btnEditImageAvata);
        btnEditImageBia=findViewById(R.id.btnEditImageBia);
        btnEditInfor.setOnClickListener(this);
        btnEditImageBia.setOnClickListener(this);
        btnEditImageAvata.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnEditInfor:
                Intent intentEditInfor=new Intent(this,EditIformation.class);
                intentEditInfor.putExtra("id",idUser);
                startActivity(intentEditInfor);
                finish();
                break;
            case R.id.btnEditImageAvata:
                upImage=new Intent(Intent.ACTION_PICK);
                upImage.setType("image/*");
                startActivityForResult(upImage,1);
                temp=1;
                break;
            case R.id.btnEditImageBia:
                upImage=new Intent(Intent.ACTION_PICK);
                upImage.setType("image/*");
                startActivityForResult(upImage,2);
                temp=2;
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
            image1.setImageURI(ImageUri1);
            upAnhLenFireBase("iv1");
        }
        else if(requestCode==2&&resultCode== Activity.RESULT_OK)
        {
            final Uri ImageUri2=data.getData();
            mResultUri.put("iv2",ImageUri2);
            image2.setImageURI(ImageUri2);
            upAnhLenFireBase("iv2");
        }
    }
    /*

* up ảnh lên firebase
‐ @created_by ncthanh on 23/2/2021
*/
    private void upAnhLenFireBase(String key) {
// up anh len fire base
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("image").child(idUser).child(key);
        ref.putFile(mResultUri.get(key))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(Information.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Information.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    /*
    * hàm lưu url ảnh lên firebase
    ‐ @created_by ncthanh on 23/2/2021
    */
    public void saveUrlImage(final String KeyImage ,String nameImage)
    {
        // lay link anh
        StorageReference filepath = FirebaseStorage.getInstance()
                .getReference().child("image")
                .child(idUser ).child(nameImage);
        Log.e("IDUSER",idUser);
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Map userInfo = new HashMap();
                userInfo.put(KeyImage, uri.toString());
                Log.e("linkImage",uri.toString());
                mReference.updateChildren(userInfo);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        findViewByID();
        imageAdapter = new ImageAdapter(this, urlImage);
        rvImage.setAdapter(imageAdapter);
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getUid();
        mReference = FirebaseDatabase.getInstance().getReference().child("user").child(idUser);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image1").getValue() != null) {
                        String iv1 = snapshot.child("image1").getValue().toString();
                        String iv2 = snapshot.child("image2").getValue().toString();
                        String iv3 = snapshot.child("image3").getValue().toString();
                        String iv4 = snapshot.child("image4").getValue().toString();
                        String iv5 = snapshot.child("image5").getValue().toString();
                        String iv6 = snapshot.child("image6").getValue().toString();
                        tvCNS.setText(snapshot.child("chamNgonSong").getValue().toString());
                        tvName.setText(snapshot.child("name").getValue().toString());
                        Log.e("imageeád1", snapshot.child("image1").getValue().toString());
                        // Glide.with(getApplication()).load(snapshot.child("image1").getValue().toString()).into(image1);
                        displayImage(image1, iv1);
                        displayImage(image2, iv2);

                        urlImage.add(iv1);
                        urlImage.add(iv2);
                        urlImage.add(iv3);
                        urlImage.add(iv4);
                        urlImage.add(iv5);
                        urlImage.add(iv6);
                        for(int i=0;i<urlImage.size();++i)
                        {
                            if(urlImage.get(i).equals("default")) {
                                urlImage.remove(i);
                            i--;
                            }
                        }
                        Log.e("urlImageRV", urlImage.get(0));
                        imageAdapter.notifyDataSetChanged();

                        edtSdt.setText(snapshot.child("phonenumber").getValue().toString());
                        edtBirthDay.setText(snapshot.child("date").getValue().toString());
                        edtHobbies.setText(snapshot.child("hobbies").getValue().toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayImage(ImageView imageView, String value) {
        switch (value) {
            case "default":

                break;
            default:
                Glide.with(this).load(value).into(imageView);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    if(temp==1)
    {
        saveUrlImage("image1","iv1");
    }
        if(temp==2)
        {
            saveUrlImage("image2","iv2");
        }
    }
}
