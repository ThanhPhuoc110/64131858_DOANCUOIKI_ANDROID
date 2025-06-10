package ntp.example.e123.admin.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.admin.AdminActivity;
import ntp.example.e123.admin.bohoctap.QLBoHocTapActivity;
import ntp.example.e123.admin.dienkhuyet.QLBoDienKhuyetActivity;
import ntp.example.e123.admin.dienkhuyet.QLDienKhuyetActivity;
import ntp.example.e123.admin.tuvung.QLBoTuVungActivity;
import ntp.example.e123.bohoctap.BoHocTap;
import ntp.example.e123.bohoctap.BoHocTapAdapter;

public class QLBoTracNghiemActivity extends AppCompatActivity {
    DatabaseReference boTracNghiemRef;
    ImageView imgBack;
    ArrayList<BoHocTap> listBoTracNghiem;
    BoHocTapAdapter adapter;
    ListView lvBoTracNghiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qlbo);
        boTracNghiemRef= FirebaseDatabase.getInstance().getReference("BoHocTap");
        imgBack =  findViewById(R.id.imgBackAdminBo);
        lvBoTracNghiem = findViewById(R.id.listviewAdminBo);
        TextView title = findViewById(R.id.textView);
        title.setText("Quản lý bộ trắc nghiệm");
        listBoTracNghiem = new ArrayList<>();
        adapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, listBoTracNghiem);
        lvBoTracNghiem.setAdapter(adapter);
        fetchTracNghiem();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLBoTracNghiemActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        lvBoTracNghiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idBo = listBoTracNghiem.get(position).getIdBo();
                Intent intent = new Intent(QLBoTracNghiemActivity.this, QLTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idBo);
                startActivity(intent);
            }
        });



    }
    private void fetchTracNghiem(){
        boTracNghiemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBoTracNghiem.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    listBoTracNghiem.add(boHocTap);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLBoTracNghiemActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}