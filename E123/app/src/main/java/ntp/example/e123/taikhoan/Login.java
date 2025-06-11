package ntp.example.e123.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.singletonpattern.MessageObject;

public class Login extends AppCompatActivity {
    Button btnDangnhap;
    TextView tvDangky, tvforgotPassword;
    EditText edttaikhoan, edtmatkhau;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    private final MessageObject messageObject = MessageObject.getInstance();
    public static Login instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        btnDangnhap = findViewById(R.id.buttonDangnhap);
        tvDangky = findViewById(R.id.textView_register);
        tvforgotPassword= findViewById(R.id.textView_forgotPassword);
        edttaikhoan = findViewById(R.id.editTextUser);
        edtmatkhau = findViewById(R.id.editTextPass);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");
        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Forgot_PassActivity.class));
            }
        });
btnDangnhap.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = edttaikhoan.getText().toString().trim();
        String matkhau = edtmatkhau.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            messageObject.ShowDialogMessage(Gravity.CENTER, Login.this, "Hãy nhập Email ", 0, new Intent(Login.this, MainActivity.class));
            return;
        }

        if (TextUtils.isEmpty(matkhau)) {
            messageObject.ShowDialogMessage(Gravity.CENTER, Login.this, "Hãy nhập mật khẩu ", 0, new Intent(Login.this, MainActivity.class));
            return;
        }
        mAuth.signInWithEmailAndPassword(email, matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    MessageObject.getInstance().ShowDialogMessage(Gravity.CENTER, Login.this, "Đăng nhập thành công", 1, new Intent(Login.this, MainActivity.class));


                } else {
                    messageObject.ShowDialogMessage(Gravity.CENTER, Login.this, "Sai Email hoặc mật khẩu!", 0, new Intent(Login.this, MainActivity.class));
                }
            }
        });

    }
});

        tvDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }




}
