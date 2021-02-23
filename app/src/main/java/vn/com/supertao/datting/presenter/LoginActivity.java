package vn.com.supertao.datting.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.com.supertao.datting.MainActivity;
import vn.com.supertao.datting.R;

public class LoginActivity extends AppCompatActivity {
    private Button btnBack;
    private Button btnLogin;
    private TextInputLayout edtEmail;
    private TextInputLayout edtPassWord;
    private TextView tvErrorMessage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthAuthStateListener;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewID();
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = edtEmail.getEditText().getText().toString().trim();
                final String password = edtPassWord.getEditText().getText().toString().trim();
                Toast.makeText(LoginActivity.this, "đăng nhập" + email + password, Toast.LENGTH_LONG).show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "đăng nhập thất bại", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
        firebaseAuthAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    SharedPreferences userTK= getApplication().getSharedPreferences("tk",MODE_PRIVATE);
                    SharedPreferences.Editor editor=userTK.edit();
                    editor.putString("taikhoan",edtEmail.getEditText().getText().toString().trim());
                    editor.putString("matkhau",edtPassWord.getEditText().getText().toString().trim());
                    editor.commit();
                    Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                   // mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mIntent);
                    finish();
                    return;
                }
            }
        };
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(LoginActivity.this, StartActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
            }
        });

    }

    public void findViewID() {
        btnBack = findViewById(R.id.btnBack);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassWord);
        btnLogin = findViewById(R.id.btnLogin);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
