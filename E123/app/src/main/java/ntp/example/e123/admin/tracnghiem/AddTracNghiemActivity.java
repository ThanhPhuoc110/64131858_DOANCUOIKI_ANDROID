package ntp.example.e123.admin.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.tracnghiem.TracNghiem;

public class AddTracNghiemActivity extends AppCompatActivity {
    private ImageView imgBack, imgAdd;
    private EditText edtNoiDung, edtDapAnA, edtDapAnB, edtDapAnC, edtDapAnD;
    private Spinner spnDapAnTrue;
    private DatabaseReference tracNghiemRef;
    private String idBoTracNghiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trac_nghiem);

        idBoTracNghiem = getIntent().getStringExtra("idBoTracNghiem");


        tracNghiemRef = FirebaseDatabase.getInstance().getReference("TracNghiem");

        imgAdd = findViewById(R.id.imgAddTN);
        imgBack = findViewById(R.id.imgBackAddTN);
        edtNoiDung = findViewById(R.id.edtCauHoiAddTN);
        edtDapAnA = findViewById(R.id.edtDapAnAAddTN);
        edtDapAnB = findViewById(R.id.edtDapAnBAddTN);
        edtDapAnC = findViewById(R.id.edtDapAnCAddTN);
        edtDapAnD = findViewById(R.id.edtDapAnDAddTN);
        spnDapAnTrue = findViewById(R.id.spnDapAnTrueAddTN);

        ArrayList<String> listDapAn = new ArrayList<>();
        listDapAn.add("A");
        listDapAn.add("B");
        listDapAn.add("C");
        listDapAn.add("D");
        spnDapAnTrue.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listDapAn));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTracNghiemActivity.this, QLTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idBoTracNghiem);
                startActivity(intent);
                finish();
            }
        });


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTracNghiem();
            }
        });    }

    private void addTracNghiem() {
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

        String idCau = tracNghiemRef.push().getKey();
        if (idCau == null) {
            Toast.makeText(this, "Lỗi tạo ID câu hỏi!", Toast.LENGTH_SHORT).show();
            return;
        }
        TracNghiem tracNghiem = new TracNghiem(idCau, idBoTracNghiem, noiDung, dapAnA, dapAnB, dapAnC, dapAnD, dapAnTrue);
        tracNghiemRef.child(idCau).setValue(tracNghiem)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddTracNghiemActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddTracNghiemActivity.this, QLTracNghiemActivity.class);
                        intent.putExtra("idBoTracNghiem", idBoTracNghiem);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddTracNghiemActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddTracNghiemActivity.this, "Lỗi Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
