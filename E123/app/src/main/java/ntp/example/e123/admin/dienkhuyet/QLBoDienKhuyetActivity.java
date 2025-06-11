package ntp.example.e123.admin.dienkhuyet;

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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.admin.AdminActivity;
import ntp.example.e123.bohoctap.BoHocTap;
import ntp.example.e123.bohoctap.BoHocTapAdapter;

public class QLBoDienKhuyetActivity extends AppCompatActivity {
    DatabaseReference boDienKhuyetRef;
    ImageView imgBack;
    ArrayList<BoHocTap> listBoDienKhuyet;
    BoHocTapAdapter adapter;
    ListView lvBoDienKhuyet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlbo);

        lvBoDienKhuyet = findViewById(R.id.listviewAdminBo);
        imgBack = findViewById(R.id.imgBackAdminBo);
        boDienKhuyetRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        TextView title = findViewById(R.id.textView);
        title.setText("Quản lý bộ điền khuyết");
        listBoDienKhuyet = new ArrayList<>();
        adapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, listBoDienKhuyet);
        lvBoDienKhuyet.setAdapter(adapter);


        fetchBoDienKhuyet();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLBoDienKhuyetActivity.this, AdminActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });

        lvBoDienKhuyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idBo = listBoDienKhuyet.get(position).getIdBo();
                Intent intent = new Intent(QLBoDienKhuyetActivity.this, QLDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", idBo);
                startActivity(intent);
            }
        });

    }
    private void fetchBoDienKhuyet() {
        boDienKhuyetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBoDienKhuyet.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    listBoDienKhuyet.add(boHocTap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLBoDienKhuyetActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}