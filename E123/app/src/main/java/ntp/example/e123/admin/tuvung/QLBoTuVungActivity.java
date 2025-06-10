package ntp.example.e123.admin.tuvung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import ntp.example.e123.admin.AdminActivity;
import ntp.example.e123.bohoctap.BoHocTap;
import ntp.example.e123.bohoctap.BoHocTapAdapter;

public class QLBoTuVungActivity extends AppCompatActivity {
    private ImageView imgBack;
    private ListView lvBoTuVung;
    private ArrayList<BoHocTap> listBoTuVung;
    private BoHocTapAdapter adapter;
    private DatabaseReference boTuVungRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlbo);

        lvBoTuVung = findViewById(R.id.listviewAdminBo);
        imgBack = findViewById(R.id.imgBackAdminBo);
        boTuVungRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        TextView title = findViewById(R.id.textView);
        title.setText("Quản lý bộ từ vựng");
        listBoTuVung = new ArrayList<>();
        adapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, listBoTuVung);
        lvBoTuVung.setAdapter(adapter);

        fetchBoTuVung();

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(QLBoTuVungActivity.this, AdminActivity.class);
            finishAffinity();
            startActivity(intent);
        });

        lvBoTuVung.setOnItemClickListener((parent, view, position, id) -> {
            String idBo = listBoTuVung.get(position).getIdBo(); // Firebase dùng String ID
            Intent intent = new Intent(QLBoTuVungActivity.this, QLTuVungActivity.class);
            intent.putExtra("idBoTuVung", idBo);
            finishAffinity();
            startActivity(intent);
        });
    }

    private void fetchBoTuVung() {
        boTuVungRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBoTuVung.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    listBoTuVung.add(boHocTap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLBoTuVungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
