package ntp.example.e123.hoctuvung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class HocTuVungActivity extends AppCompatActivity {
    private ListView botuvung;
    private ImageView imgback;
    private ArrayList<BoHocTap> boTuVungs;
    private BoHocTapAdapter adapter;
    private DatabaseReference boTuVungRef;
    private String idbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tu_vung);

        botuvung = findViewById(R.id.listviewHTV);
        imgback = findViewById(R.id.imgVBackHTV);
        boTuVungRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        boTuVungs = new ArrayList<>();
        adapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, boTuVungs);
        botuvung.setAdapter(adapter);

        fetchBoTuVung();

        botuvung.setOnItemClickListener((parent, view, position, id) -> {
            idbo = boTuVungs.get(position).getIdBo();
            Intent dstv = new Intent(HocTuVungActivity.this, DSTuVungActivity.class);
            dstv.putExtra("idbo", idbo);
            startActivity(dstv);
        });

        imgback.setOnClickListener(v -> startActivity(new Intent(HocTuVungActivity.this, MainActivity.class)));
    }

    private void fetchBoTuVung() {
        boTuVungRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boTuVungs.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    boHocTap.setIdBo(data.getKey()); // Lưu ID Firebase
                    boTuVungs.add(boHocTap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HocTuVungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
