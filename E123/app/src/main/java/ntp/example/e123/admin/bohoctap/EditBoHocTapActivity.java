package ntp.example.e123.admin.bohoctap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.R;
import ntp.example.e123.bohoctap.BoHocTap;

public class EditBoHocTapActivity extends AppCompatActivity {
    ImageView imgBack, imgEdit;
 EditText edtBoHocTap;
 DatabaseReference boHocTapRef;
 String idBHT;
  BoHocTap boHocTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bo_hoc_tap);

        imgBack = findViewById(R.id.imgBackAddBHT);
        imgEdit = findViewById(R.id.imgAddBHT);
        edtBoHocTap = findViewById(R.id.edtAddBoHocTap);
        TextView title = findViewById(R.id.textView);
        title.setText("Sửa bộ học tập");

        idBHT = getIntent().getStringExtra("ID_BHT");
        boHocTapRef = FirebaseDatabase.getInstance().getReference("BoHocTap").child(idBHT);

        fetchBoHocTap();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditBoHocTapActivity.this, QLBoHocTapActivity.class));
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtBoHocTap.getText().toString().trim();
                if (ten.isEmpty()) {
                    Toast.makeText(EditBoHocTapActivity.this, "Chưa điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    updateBoHocTap(ten);
                }
            }
        });

    }

    private void fetchBoHocTap() {
        boHocTapRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boHocTap = snapshot.getValue(BoHocTap.class);
                    if (boHocTap != null) {
                        edtBoHocTap.setText(boHocTap.getTenBo());
                    }
                } else {
                    Toast.makeText(EditBoHocTapActivity.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditBoHocTapActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBoHocTap(String tenBo) {
        boHocTapRef.child("tenBo").setValue(tenBo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditBoHocTapActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditBoHocTapActivity.this, QLBoHocTapActivity.class));
                } else {
                    Toast.makeText(EditBoHocTapActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
