package ntp.example.e123.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ntp.example.e123.R;

public class Forgot_PassActivity extends AppCompatActivity {
    private EditText EmailForgot, edtNewPassword, edtConfirmPassword,edtPassCu;
    private Button btnResetPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pass);
        EmailForgot = findViewById(R.id.edtEmail);
        edtNewPassword = findViewById(R.id.edtpassmoi);
        edtConfirmPassword = findViewById(R.id.edtpassconfirm);
        btnResetPass = findViewById(R.id.btnResetPass);
        TextView loginBack = findViewById(R.id.textView_login);
        auth = FirebaseAuth.getInstance();
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePass();
            }
        });
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_PassActivity.this,Login.class));
            }
        });
    }
    void updatePass(){
        String pass1 = edtNewPassword.getText().toString().trim();
        String pass2 = edtConfirmPassword.getText().toString().trim();
        if(pass1.isEmpty() || pass2.isEmpty()){
            Toast.makeText(Forgot_PassActivity.this,"Cần nhập đầy đủ thông tin ",Toast.LENGTH_LONG).show();
            return;
        }
        if(!pass1.equals(pass2)){
            Toast.makeText(Forgot_PassActivity.this,"Mật khẩu không khớp ",Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "MẬT_KHẨU_CŨ");
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.updatePassword(pass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Forgot_PassActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Forgot_PassActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }

    }
}