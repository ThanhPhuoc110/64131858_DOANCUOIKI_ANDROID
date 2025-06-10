package ntp.example.e123.admin.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.tracnghiem.TracNghiem;

public class EditTracNghiemActivity extends AppCompatActivity {
    private ImageView imgEdit, imgBack;
    private EditText edtNoiDung, edtDapAnA, edtDapAnB, edtDapAnC, edtDapAnD;
    private Spinner spnDapAnTrue;
    private String idCau, idTracNghiem;
    private DatabaseReference tracNghiemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trac_nghiem);

        tracNghiemRef = FirebaseDatabase.getInstance().getReference("TracNghiem");

        idCau = getIntent().getStringExtra("ID_TN");
        idTracNghiem = getIntent().getStringExtra("idBoHocTap");



        imgBack = findViewById(R.id.imgBackAddTN);
        imgEdit = findViewById(R.id.imgAddTN);
        edtNoiDung = findViewById(R.id.edtCauHoiAddTN);
        edtDapAnA = findViewById(R.id.edtDapAnAAddTN);
        edtDapAnB = findViewById(R.id.edtDapAnBAddTN);
        edtDapAnC = findViewById(R.id.edtDapAnCAddTN);
        edtDapAnD = findViewById(R.id.edtDapAnDAddTN);
        spnDapAnTrue = findViewById(R.id.spnDapAnTrueAddTN);
        TextView title =findViewById(R.id.textView);
        title.setText("Sửa câu trắc nghiệm");
        ArrayList<String> listDapAn = new ArrayList<>();
        listDapAn.add("A");
        listDapAn.add("B");
        listDapAn.add("C");
        listDapAn.add("D");
        spnDapAnTrue.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listDapAn));

        fetchTracNghiem();
    imgBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditTracNghiemActivity.this, QLTracNghiemActivity.class);
            intent.putExtra("idBoTracNghiem", idTracNghiem);
            startActivity(intent);
            finish();
        }
    });


        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTracNghiem();
                finish();
            }
        });
    }

    private void fetchTracNghiem() {
        tracNghiemRef.child(idCau).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TracNghiem tracNghiem = snapshot.getValue(TracNghiem.class);
                    if (tracNghiem != null) {
                        edtNoiDung.setText(tracNghiem.getNoiDung());
                        edtDapAnA.setText(tracNghiem.getDapAnA());
                        edtDapAnB.setText(tracNghiem.getDapAnB());
                        edtDapAnC.setText(tracNghiem.getDapAnC());
                        edtDapAnD.setText(tracNghiem.getDapAnD());

                        switch (tracNghiem.getDapAnTrue()) {
                            case "1": spnDapAnTrue.setSelection(0); break;
                            case "2": spnDapAnTrue.setSelection(1); break;
                            case "3": spnDapAnTrue.setSelection(2); break;
                            case "4": spnDapAnTrue.setSelection(3); break;
                        }
                    }
                } else {
                    Toast.makeText(EditTracNghiemActivity.this, "Không tìm thấy câu hỏi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditTracNghiemActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTracNghiem() {
        String noiDung = edtNoiDung.getText().toString().trim();
        String dapAnA = edtDapAnA.getText().toString().trim();
        String dapAnB = edtDapAnB.getText().toString().trim();
        String dapAnC = edtDapAnC.getText().toString().trim();
        String dapAnD = edtDapAnD.getText().toString().trim();
        String dapAnTrue = String.valueOf(spnDapAnTrue.getSelectedItemPosition() + 1);

        if (noiDung.isEmpty() || dapAnA.isEmpty() || dapAnB.isEmpty() || dapAnC.isEmpty() || dapAnD.isEmpty()) {
            Toast.makeText(this, "Chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        tracNghiemRef.child(idCau).child("noiDung").setValue(noiDung);
        tracNghiemRef.child(idCau).child("dapAnA").setValue(dapAnA);
        tracNghiemRef.child(idCau).child("dapAnB").setValue(dapAnB);
        tracNghiemRef.child(idCau).child("dapAnC").setValue(dapAnC);
        tracNghiemRef.child(idCau).child("dapAnD").setValue(dapAnD);
        tracNghiemRef.child(idCau).child("dapAnTrue").setValue(dapAnTrue)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditTracNghiemActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditTracNghiemActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditTracNghiemActivity.this, "Lỗi Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
