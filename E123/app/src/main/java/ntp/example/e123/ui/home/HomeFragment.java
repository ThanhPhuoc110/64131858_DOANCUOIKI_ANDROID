package ntp.example.e123.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ntp.example.e123.R;
import ntp.example.e123.dienkhuyet.DienKhuyetActivity;
import ntp.example.e123.hoctuvung.HocTuVungActivity;

import ntp.example.e123.xephang.RankingActivity;
import ntp.example.e123.tracnghiem.TracNghiemActivity;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private CardView cardViewHocTuVung, cardViewTracNghiem, cardViewSapXepCau, cardViewLuyenNghe, cardViewDienKhuyet, cardViewXepHang;
    private ImageView imgView;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initFirebase();
        initUI(root);
        setupNavigation();

        return root;
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("User");

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
            fetchUserData();
        }
    }

    private void initUI(View root) {
        cardViewHocTuVung = root.findViewById(R.id.cardViewHocTuVung);
        cardViewDienKhuyet = root.findViewById(R.id.cardViewDienKhuyet);
        cardViewTracNghiem = root.findViewById(R.id.cardViewTracNghiem);
       cardViewXepHang = root.findViewById(R.id.cardViewXepHang);
        imgView = root.findViewById(R.id.imageViewProfile);
    }

    private void setupNavigation() {
        cardViewHocTuVung.setOnClickListener(v -> navigateTo(HocTuVungActivity.class));
        cardViewDienKhuyet.setOnClickListener(v -> navigateTo(DienKhuyetActivity.class));
        cardViewTracNghiem.setOnClickListener(v -> navigateTo(TracNghiemActivity.class));
        cardViewXepHang.setOnClickListener(v -> navigateTo(RankingActivity.class));
    }

    private void navigateTo(Class<?> activityClass) {
        startActivity(new Intent(getActivity(), activityClass));
    }

    private void fetchUserData() {
        userRef.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String hoTen = snapshot.child("hoTen").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImage").getValue(String.class);

                    // Hiển thị ảnh profile (Nếu có)
                    if (profileImageUrl != null) {
                        // Picasso hoặc Glide có thể được sử dụng để tải ảnh từ Firebase
                    }

                    // Log để kiểm tra
                    System.out.println("Tên người dùng: " + hoTen);
                }
            }
        });
    }
}
