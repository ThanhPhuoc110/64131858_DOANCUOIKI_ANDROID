package ntp.example.e123.taikhoan;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.singletonpattern.MessageObject;

public class SignupActivity extends AppCompatActivity {

    TextView tvDangNhap;
    EditText edtHoTen, edtEmail, edtSdt, edtMatKhau, edtXacNhan;
    Button btnSignUp;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private final MessageObject messageObject = MessageObject.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvDangNhap = findViewById(R.id.textView_login);
        edtHoTen = findViewById(R.id.editTextEmailNav);
        edtEmail = findViewById(R.id.editTextEmail);
        edtSdt = findViewById(R.id.editTextSdt);
        edtMatKhau = findViewById(R.id.editTextMatKhau);
        edtXacNhan = findViewById(R.id.editTextXacNhan);
        btnSignUp = findViewById(R.id.buttonSignUp);        mAuth = FirebaseAuth.getInstance();

        tvDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, Login.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> {
            String hoten = edtHoTen.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();
            String matkhau = edtMatKhau.getText().toString().trim();
            String xacnhanmatkhau = edtXacNhan.getText().toString().trim();

            if (TextUtils.isEmpty(hoten) || TextUtils.isEmpty(email) || TextUtils.isEmpty(sdt) || TextUtils.isEmpty(matkhau)) {
                messageObject.ShowDialogMessage(Gravity.CENTER, SignupActivity.this, "Vui lòng điền đầy đủ thông tin!", 0, new Intent(this, MainActivity.class));
                return;
            }

            if (!matkhau.equals(xacnhanmatkhau)) {
                messageObject.ShowDialogMessage(Gravity.CENTER, SignupActivity.this, "Mật khẩu không trùng khớp!", 0, new Intent(this, MainActivity.class));
                edtMatKhau.setText("");
                edtXacNhan.setText("");
                return;
            }

            // Đăng ký tài khoản với Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    messageObject.ShowDialogMessage(Gravity.CENTER, SignupActivity.this, "Đăng ký thành công!", 0, new Intent(this, MainActivity.class));

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("User");

                    User newUser = new User(mAuth.getCurrentUser().getUid(), hoten, 0, email, sdt, 0);
                    reference.child(mAuth.getCurrentUser().getUid()).setValue(newUser);

                    Intent intent = new Intent(SignupActivity.this, Login.class);
                    startActivity(intent);
                } else {
                    messageObject.ShowDialogMessage(Gravity.CENTER, SignupActivity.this, "Đăng ký thất bại!", 0, new Intent(this, MainActivity.class));
                }
            });
        });
    }


}
