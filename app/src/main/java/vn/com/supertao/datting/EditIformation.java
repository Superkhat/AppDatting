package vn.com.supertao.datting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditIformation extends AppCompatActivity implements View.OnClickListener {
    private Button btnCancel;
    private Button btnSave;
    private EditText edtNameUserEdit;
    private EditText edtPhone;
    private EditText edtChamNgonSong;
    private EditText cldDate;
    private CheckBox cbXemPhim;
    private CheckBox cbChoiGame;
    private CheckBox cbDuLich;
    private CheckBox cbTheThao;
    private CheckBox cbKhac;
    private DatabaseReference mDatabase;
    private String id;
    private String xp, tt, dl, k, cg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_iformation);
        mapping();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(id);
        putDataInView();
    }

    public void mapping() {
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        edtPhone = findViewById(R.id.edtPhone);
        edtNameUserEdit = findViewById(R.id.edtNameUserEdit);
        edtChamNgonSong = findViewById(R.id.edtChamNgonSong);
        cldDate = findViewById(R.id.cldDate);
        cbXemPhim = findViewById(R.id.cbXemPhim);
        xp = cbXemPhim.getText().toString();
        cbChoiGame = findViewById(R.id.cbChoiGame);
        cg = cbChoiGame.getText().toString();
        cbDuLich = findViewById(R.id.cbDuLich);
        dl = cbDuLich.getText().toString();
        cbTheThao = findViewById(R.id.cbTheThao);
        tt = cbTheThao.getText().toString();
        cbKhac = findViewById(R.id.cbKhac);
        k = cbKhac.getText().toString();

    }

    // đẩy data từ firebase vào view
    public void putDataInView() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    edtNameUserEdit.setText(snapshot.child("name").getValue().toString());
                    edtChamNgonSong.setText(snapshot.child("chamNgonSong").getValue().toString());
                    edtPhone.setText(snapshot.child("phonenumber").getValue().toString());
                    String hobbies = snapshot.child("hobbies").getValue().toString();
                    if (xp.equals(hobbies))
                        cbXemPhim.setChecked(true);
                    if (cg.equals(hobbies))
                        cbChoiGame.setChecked(true);
                    if (k.equals(hobbies))
                        cbKhac.setChecked(true);
                    if (dl.equals(hobbies))
                        cbDuLich.setChecked(true);
                    if (tt.equals(hobbies))
                        cbTheThao.setChecked(true);
                    cldDate.setText(snapshot.child("date").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Update data
    private List<String> hobbies;
    public void updateData()
    {
        Map map=new HashMap();
        hobbies=new ArrayList<>();
        String name=edtNameUserEdit.getText().toString();
        String date=cldDate.getText().toString();
        String  cns=edtChamNgonSong.getText().toString();
        String phone=edtPhone.getText().toString();
        if(cbChoiGame.isChecked())
            hobbies.add(cbChoiGame.getText().toString());
        if(cbDuLich.isChecked())
            hobbies.add(cbDuLich.getText().toString());
        if(cbXemPhim.isChecked())
            hobbies.add(cbXemPhim.getText().toString());
        if(cbTheThao.isChecked())
            hobbies.add(cbTheThao.getText().toString());
        if(cbKhac.isChecked())
            hobbies.add(cbKhac.getText().toString());
        for(int ii=0;ii<hobbies.size();++ii)
        {
            map.put("hobbies",hobbies.get(ii));
        }
        map.put("name",name);
        map.put("name",name);
        map.put("date",date);
        map.put("phonenumber",phone);
        map.put("chamNgonSong",cns);
        mDatabase.updateChildren(map);
    }
    @Override
    public void onClick(View view) {
    switch (view.getId())
    {
        case R.id.btnCancel:
            Intent f=new Intent(this,Information.class);
            f.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(f);
            finish();
            break;
        case R.id.btnSave:
            updateData();
            Intent f1=new Intent(this,Information.class);
            f1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(f1);
            finish();
            break;
    }
    }
}
