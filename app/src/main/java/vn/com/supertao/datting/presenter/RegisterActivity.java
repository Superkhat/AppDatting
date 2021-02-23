package vn.com.supertao.datting.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import vn.com.supertao.datting.R;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnBack;
    private Button btnCompleted;
    private Intent mIntent;
    private TextInputEditText edtName;
    private TextInputEditText edtPhone;
    private TextInputEditText edtChamNgonSong1;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPassWord;
    private DatePicker cldDate;
    private RadioGroup rbgSex;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private CheckBox cbXemPhim;
    private CheckBox cbChoiGame;
    private CheckBox cbDuLich;
    private CheckBox cbTheThao;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewID();
        btnBack.setOnClickListener(this);
        btnCompleted.setOnClickListener(this);
    }

    public void findViewID() {
        btnBack = findViewById(R.id.btnBack);
        btnCompleted = findViewById(R.id.btnCompleted);
        edtName = findViewById(R.id.edtNameUser);
        edtPhone = findViewById(R.id.edtPhone);
        edtChamNgonSong1 = findViewById(R.id.edtChamNgonSong);
        edtChamNgonSong1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edtPassWord = findViewById(R.id.edtPassWord);
        cbChoiGame = findViewById(R.id.cbChoiGame);
        cbDuLich = findViewById(R.id.cbDuLich);
        cbTheThao = findViewById(R.id.cbTheThao);
        cbXemPhim = findViewById(R.id.cbXemPhim);
        cldDate = findViewById(R.id.cldDate);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale = findViewById(R.id.rbMale);
        rbgSex = findViewById(R.id.rbg);
        // khởi tạo xác thực dăng nhập và database
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /*
    * hàm này có chức năng bắt sự kiện cho nút tở về và hoàn tất
    ‐ @created_by ncthanh on 23/1/2021
    */
    @Override
    public void onClick(View view) {
        final String name = edtName.getText().toString().trim();
        final String Date = cldDate.getDayOfMonth() + "/" + cldDate.getMonth() + "/" + cldDate.getYear();
        final long year=cldDate.getYear();
        final String phoneNumber = edtPhone.getText().toString();
        final String chamNgonSong = edtChamNgonSong1.getText().toString();
        final String sex;
        int selected = rbgSex.getCheckedRadioButtonId();
        RadioButton radioButtonSelected = findViewById(selected);
        sex = radioButtonSelected.getText().toString().trim();
        final List<String> hobbies = new ArrayList<>();
        final String email = edtEmail.getText().toString().trim();
        final String passWord = edtPassWord.getText().toString().trim();
        if (cbXemPhim.isChecked())
            hobbies.add(cbXemPhim.getText().toString().trim());
        if (cbTheThao.isChecked())
            hobbies.add(cbTheThao.getText().toString().trim());
        if (cbDuLich.isChecked())
            hobbies.add(cbDuLich.getText().toString().trim());
        if (cbChoiGame.isChecked())
            hobbies.add(cbChoiGame.getText().toString().trim());

        switch (view.getId()) {
            // nút trở về
            case R.id.btnBack:
                mIntent = new Intent(RegisterActivity.this, StartActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                break;
            // nút hoàn tất
            case R.id.btnCompleted:
                if (name.equals("") == true ||
                        Date.equals("") == true ||
                        phoneNumber.equals("") == true ||
                        chamNgonSong.equals("") == true ||
                        sex.equals("") == true ||
                        hobbies.size() == 0 || email.equals("") == true || passWord.equals("") == true) {
                    Toast.makeText(RegisterActivity.this, "Bạn cần hoàn thiện hết phần trống để hoàn tất", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(RegisterActivity.this,Date+" "+phoneNumber+" "+hobbies.get(0),Toast.LENGTH_LONG).show();
                mFirebaseAuth.createUserWithEmailAndPassword(email,passWord).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"đăng nhập thất bại",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String idUser=mFirebaseAuth.getCurrentUser().getUid();
                            mDatabaseReference=mDatabaseReference.child("user").child(idUser);
                            Map userInfo=new HashMap();
                            userInfo.put("name",name);
                            userInfo.put("date",Date);
                            userInfo.put("year",year);
                            userInfo.put("phonenumber",phoneNumber);
                            userInfo.put("sex",sex);
                            for(int i=0;i<hobbies.size();++i)
                            userInfo.put("hobbies",hobbies.get(i));
                            userInfo.put("chamNgonSong",chamNgonSong);
                            userInfo.put("image1","default");
                            userInfo.put("image2","default");
                            userInfo.put("image3","default");
                            userInfo.put("image4","default");
                            userInfo.put("image5","default");
                            userInfo.put("image6","default");
                            userInfo.put("Status","");
                            mDatabaseReference.updateChildren(userInfo);
                            Intent mIntent = new Intent(RegisterActivity.this, UpImage.class);
                            // mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mIntent);

                        }
                    }
                });
                    /*Intent mIntent = new Intent(RegisterActivity.this, UpImage.class);
                    // mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);*/
                }
                    break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    finish();
    }
}
