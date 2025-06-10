package ntp.example.e123.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.MainActivity;
import ntp.example.e123.singletonpattern.MessageObject;
import ntp.example.e123.taikhoan.Login;
import ntp.example.e123.taikhoan.ThongTinTaikhoanActivity;
import ntp.example.e123.taikhoan.User;

public class AdminActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    private MessageObject messageObject = MessageObject.getInstance();
    ArrayList<String> adminList;
    AdminAdapter adapter;
    ListView admins;
    ImageView imgLogout;
    Boolean doubleBack = false;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");

        imgLogout = findViewById(R.id.imgLogoutAdmin);
        admins = findViewById(R.id.listviewAdmin);

        String userId = mAuth.getCurrentUser().getUid();
        userRef.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String hoTen = snapshot.child("hoTen").getValue(String.class);
                    int role = snapshot.child("role").getValue(Integer.class);
                        user = new User(userId, hoTen, 0, "", "", role);

                } else {

                    messageObject.ShowDialogMessage(Gravity.CENTER, AdminActivity.this, "Lỗi dữ liệu người dùng", 0, new Intent(this, MainActivity.class));
                    startActivity(new Intent(AdminActivity.this, MainActivity.class));
                }
            }
        });

        adminList = new ArrayList<>();
        adminList.add("Thông tin tài khoản");
        adminList.add("Học tập");
        adminList.add("Bộ học tập");
        adminList.add("Từ vựng");
        adminList.add("Trắc nghiệm");
        adminList.add("Điền khuyết");

        adapter = new AdminAdapter(AdminActivity.this, R.layout.row_admin, adminList);
        admins.setAdapter(adapter);
        admins.setOnItemClickListener((parent, view, position, id) -> handleMenuClick(position));

        imgLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(AdminActivity.this, Login.class));
        });
    }



    private void handleMenuClick(int position) {
        Class<?> activityClass;
        switch (position) {
            case 0:
                activityClass = ThongTinTaikhoanActivity.class;
                break;
            case 1:
                activityClass = MainActivity.class;
                break;
            case 2:
                activityClass = ntp.example.e123.admin.bohoctap.QLBoHocTapActivity.class;
                break;
            case 3:
                activityClass = ntp.example.e123.admin.tuvung.QLBoTuVungActivity.class;
                break;
          case 4:
                activityClass = ntp.example.e123.admin.tracnghiem.QLBoTracNghiemActivity.class;
              break;
            case 5:
                activityClass = ntp.example.e123.admin.dienkhuyet.QLBoDienKhuyetActivity.class;
                break;
            default:
                return;
        }
        startActivity(new Intent(AdminActivity.this, activityClass));
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            finish();
            moveTaskToBack(true);
        } else {
            doubleBack = true;
            Toast.makeText(getApplicationContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBack = false, 2000);
        }
    }
}
