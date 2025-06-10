package ntp.example.e123.dienkhuyet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.bohoctap.BoHocTap;
import ntp.example.e123.bohoctap.BoHocTapAdapter;

public class DienKhuyetActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView imgBack;
    private ArrayList<BoHocTap> boHocTapArrayList;
    private BoHocTapAdapter boHocTapAdapter;
    private DatabaseReference boCauHoiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tu_vung);

        listView = findViewById(R.id.listviewHTV);
        imgBack = findViewById(R.id.imgVBackHTV);
        boCauHoiRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        TextView title = findViewById(R.id.textView);
        title.setText("Bộ điền khuyết");

        boHocTapArrayList = new ArrayList<>();
        boHocTapAdapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, boHocTapArrayList);
        listView.setAdapter(boHocTapAdapter);

        fetchBoCauHoi();

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(DienKhuyetActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            BoHocTap selectedBoHocTap = boHocTapArrayList.get(position);
            Intent quiz = new Intent(DienKhuyetActivity.this, DienVaoOsActivity.class);
            quiz.putExtra("BoDK", selectedBoHocTap.getIdBo());
            startActivity(quiz);
        });
    }

    private void fetchBoCauHoi() {
        boCauHoiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boHocTapArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    boHocTapArrayList.add(boHocTap);
                }
                boHocTapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DienKhuyetActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
