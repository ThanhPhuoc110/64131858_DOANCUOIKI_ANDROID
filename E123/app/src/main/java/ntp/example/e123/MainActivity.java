package ntp.example.e123;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ntp.example.e123.admin.AdminActivity;
import ntp.example.e123.dienkhuyet.DienKhuyetActivity;
import ntp.example.e123.hoctuvung.HocTuVungActivity;
import ntp.example.e123.singletonpattern.MessageObject;
import ntp.example.e123.taikhoan.Login;
import ntp.example.e123.xephang.RankingActivity;
import ntp.example.e123.taikhoan.User;
import ntp.example.e123.tracnghiem.TracNghiemActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private User user;
    private boolean doubleBack = false;
    private String userId;
    private MessageObject messageObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        setupUI();
        fetchUser();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");
        messageObject = MessageObject.getInstance();

        if (mAuth.getCurrentUser() == null) {
            navigateToLogin();
            return;
        }
        userId = mAuth.getCurrentUser().getUid();
    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Giải đáp thắc mắc và hỗ trợ xin gửi về thanhphuoc26062003@gmail.com", Toast.LENGTH_SHORT).show();


            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.HocTuVung, R.id.TracNghiem, R.id.DienKhuyet,
                 R.id.XepHang, R.id.nav_admin, R.id.logout
        ).setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void fetchUser() {
        userRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    user = task.getResult().getValue(User.class);

                    NavigationView navigationView = findViewById(R.id.nav_view);
                    MenuItem adminItem = navigationView.getMenu().findItem(R.id.nav_admin);

                    if (user.getRole() == 1) {
                        adminItem.setVisible(true);
                    } else {
                        adminItem.setVisible(false);
                    }
                } else {
                    navigateToLogin();
                }
            }
        });

    }

    private void navigateToLogin() {
        Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Login.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if (id == R.id.nav_home) {
            navController.navigate(R.id.nav_home);
        } else if (id == R.id.HocTuVung) {
            startActivity(new Intent(MainActivity.this, HocTuVungActivity.class));
        } else if (id == R.id.TracNghiem) {
            startActivity(new Intent(MainActivity.this, TracNghiemActivity.class));
        } else if (id == R.id.DienKhuyet) {
            startActivity(new Intent(MainActivity.this, DienKhuyetActivity.class));
        } else if (id == R.id.XepHang) {
            startActivity(new Intent(MainActivity.this, RankingActivity.class));
        } else if (id == R.id.nav_admin) {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        } else if (id == R.id.logout) {
            logoutUser();
        }

        return true;
    }



    private void logoutUser() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            moveTaskToBack(true);
        } else {
            doubleBack = true;
            Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBack = false, 2000);
        }
    }
}
