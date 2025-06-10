package ntp.example.e123.admin.bohoctap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

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
import ntp.example.e123.bohoctap.BoHocTap;

public class QLBoHocTapActivity extends AppCompatActivity {
    private ArrayList<BoHocTap> listBHT;
    private QLBoHocTapAdapter adapter;
    private ImageView imgBack, imgAdd;
    private ListView listViewBHT;
    private DatabaseReference boHocTapRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qlbo_hoc_tap);
        listViewBHT = (ListView) findViewById(R.id.listviewAdminBHT);
        imgBack = (ImageView) findViewById(R.id.imgBackAdminBHT);
        imgAdd = (ImageView) findViewById(R.id.imgAddBHT);
        boHocTapRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        listBHT = new ArrayList<>();
        adapter = new QLBoHocTapAdapter(this, listBHT);
        listViewBHT.setAdapter(adapter);
        fetchBoHocTap();
        imgBack.setOnClickListener(v -> startActivity(new Intent(QLBoHocTapActivity.this, AdminActivity.class)));
        imgAdd.setOnClickListener(v -> startActivity(new Intent(QLBoHocTapActivity.this, AddBoHocTapActivity.class)));
    }
    private void fetchBoHocTap() {
        boHocTapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBHT.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    listBHT.add(boHocTap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}