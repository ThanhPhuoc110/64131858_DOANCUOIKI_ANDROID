package ntp.example.e123.hoctuvung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.R;

public class DSTuVungActivity extends AppCompatActivity {

    private ImageView imgBack;
    private GridView gvTuVung;
    private Button btnOntap;
    private ArrayList<TuVung> DStuvung;
    private DSTuVungAdapter adapter;
    private DatabaseReference tuVungRef;
    private String idBoHocTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dstu_vung);

        Intent intent = getIntent();
        idBoHocTap = intent.getStringExtra("idbo");
        gvTuVung = findViewById(R.id.lgvTuVung);
        btnOntap = findViewById(R.id.btnOnTap);
        imgBack = findViewById(R.id.imgVBackDSTV);
        tuVungRef = FirebaseDatabase.getInstance().getReference("TuVung");

        DStuvung = new ArrayList<>();
        adapter = new DSTuVungAdapter(this, R.layout.row_dstuvung, DStuvung);
        gvTuVung.setAdapter(adapter);

        fetchTuVung();

        imgBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(DSTuVungActivity.this, HocTuVungActivity.class);
            startActivity(intent1);
        });
        btnOntap.setOnClickListener(v -> {
            Intent ontap = new Intent(DSTuVungActivity.this, OnTapActivity.class);
            ontap.putExtra("idbo", idBoHocTap);
            startActivity(ontap);
        });

    }

    private void fetchTuVung() {
        if (idBoHocTap == null || idBoHocTap.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID bộ học tập!", Toast.LENGTH_SHORT).show();
            return;
        }

        tuVungRef.orderByChild("idBoHocTap").equalTo(idBoHocTap).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DStuvung.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TuVung tuVung = data.getValue(TuVung.class);
                    DStuvung.add(tuVung);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSTuVungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
