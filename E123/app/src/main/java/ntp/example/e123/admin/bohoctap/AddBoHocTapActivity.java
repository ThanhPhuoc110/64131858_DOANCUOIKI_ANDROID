package ntp.example.e123.admin.bohoctap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.R;
import ntp.example.e123.bohoctap.BoHocTap;

public class AddBoHocTapActivity extends AppCompatActivity {
    private ImageView imgBack, imgAdd;
    private EditText edtBoHocTap;
    private DatabaseReference boHocTapRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bo_hoc_tap);
        imgBack = findViewById(R.id.imgBackAddBHT);
        imgAdd = findViewById(R.id.imgAddBHT);
        edtBoHocTap = findViewById(R.id.edtAddBoHocTap);
        boHocTapRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        imgBack.setOnClickListener(v -> startActivity(new Intent(AddBoHocTapActivity.this, QLBoHocTapActivity.class)));
        imgAdd.setOnClickListener(v -> {
            String tenBo = edtBoHocTap.getText().toString().trim();
            if (tenBo.isEmpty()) {
                Toast.makeText(AddBoHocTapActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                addBoHocTap(tenBo);
            }
        });
    }
    private void addBoHocTap(String tenBo) {
        boHocTapRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int stt = (int) snapshot.getChildrenCount() + 1;
                String idBo = boHocTapRef.push().getKey();
                if (idBo != null) {
                    BoHocTap boHocTap = new BoHocTap(idBo, stt, tenBo);
                    boHocTapRef.child(idBo).setValue(boHocTap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddBoHocTapActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddBoHocTapActivity.this, QLBoHocTapActivity.class));
                        } else {
                            Toast.makeText(AddBoHocTapActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddBoHocTapActivity.this, "Lỗi lấy dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}