package ntp.example.e123.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.admin.AdminActivity;

public class ThongTinTaikhoanActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference userRef;
    ImageView imghome;
    EditText tvHoten, tvEmail, tvSdt, tvUID;
    TextView tvtaikhoan, tvTen, tvPoint;
    Button btnCapNhat;
    String userId;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_taikhoan);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");

        tvHoten = findViewById(R.id.textIntEdtHoten);
        tvEmail = findViewById(R.id.textIntEdtEmail);
        tvSdt = findViewById(R.id.textIntEdtSdt);
        tvUID = findViewById(R.id.textIntEdtUID);
        tvtaikhoan = findViewById(R.id.tVusername);
        tvTen = findViewById(R.id.textViewTen);
        tvPoint = findViewById(R.id.textviewPoint);
        btnCapNhat = findViewById(R.id.buttonCapNhat);
        imghome = findViewById(R.id.imgHOME);

        tvUID.setEnabled(false);
        tvEmail.setEnabled(false);
        userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ThongTinTaikhoanActivity.this, Login.class));
            finish();
            return;
        }

        FetchUser();

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapNhatThongTin();
                finish();
            }
        });
        imghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && user.getRole() == 0) {
                    startActivity(new Intent(ThongTinTaikhoanActivity.this, AdminActivity.class));
                } else {
                    startActivity(new Intent(ThongTinTaikhoanActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }



    private void FetchUser() {
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    TruyenThongTin();
                } else {
                    Toast.makeText(ThongTinTaikhoanActivity.this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ThongTinTaikhoanActivity.this, Login.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinTaikhoanActivity.this, "Lỗi kết nối với Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void TruyenThongTin() {
        if (user != null) {
            tvHoten.setText(user.getHoTen());
            tvTen.setText(user.getHoTen());
            tvtaikhoan.setText(user.getEmail());
            tvPoint.setText(String.valueOf(user.getPoint()));
            tvEmail.setText(user.getEmail());
            tvSdt.setText(user.getSDT());
            tvUID.setText(user.getIduser());

        }
    }

    private void CapNhatThongTin() {
        String hoten = tvHoten.getText().toString().trim();
        String sdt = tvSdt.getText().toString().trim();

        if (hoten.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef.child(userId).child("hoTen").setValue(hoten);
        userRef.child(userId).child("sdt").setValue(sdt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ThongTinTaikhoanActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThongTinTaikhoanActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
