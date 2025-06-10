package ntp.example.e123.admin.tuvung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.admin.tracnghiem.AddTracNghiemActivity;
import ntp.example.e123.admin.tracnghiem.QLTracNghiemActivity;
import ntp.example.e123.hoctuvung.TuVung;

public class AddTuVungActivity extends AppCompatActivity {
    private ImageView imgBack, imgAdd;
    private Button btnAdd;
    private EditText edtTuVung, edtNghia, edtImageUrl;
    private Spinner spnTuLoai;
    private DatabaseReference tuVungRef;
    private String idBoTuVung;
    private ArrayList<String> listTuLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_tu_vung);

        imgBack = findViewById(R.id.imgBackAddTV);
        imgAdd = findViewById(R.id.imgAddTV);
        edtTuVung = findViewById(R.id.edtTuVungAddTV);
        edtNghia = findViewById(R.id.edtNghiaAddTV);
        edtImageUrl = findViewById(R.id.edturlAddTV);
        spnTuLoai = findViewById(R.id.spnLoaiTuAddTV);

        listTuLoai = new ArrayList<>();
        listTuLoai.add("Danh từ");
        listTuLoai.add("Động từ");
        listTuLoai.add("Tính từ");
        listTuLoai.add("Trạng từ");
        listTuLoai.add("Giới từ");

        ArrayAdapter<String> tuLoaiAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listTuLoai);
        tuLoaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTuLoai.setAdapter(tuLoaiAdapter);

        idBoTuVung = getIntent().getStringExtra("idBoTuVung");

        if (idBoTuVung == null || idBoTuVung.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID bộ từ vựng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tuVungRef = FirebaseDatabase.getInstance().getReference("TuVung");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTuVungActivity.this, QLTuVungActivity.class);
                intent.putExtra("idBoTuVung", idBoTuVung);
                startActivity(intent);
                finish();
            }
        });       imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTuVung();
            }
        });
    }

    private void addTuVung() {
        String idTu = tuVungRef.push().getKey();
        String Tu = edtTuVung.getText().toString().trim();
        String nghia = edtNghia.getText().toString().trim();
        String loaitu = spnTuLoai.getSelectedItem().toString();
        String imageUrl = edtImageUrl.getText().toString().trim(); // URL ảnh

        if (idTu == null || Tu.isEmpty() || nghia.isEmpty() || loaitu.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        TuVung tuVung = new TuVung(idTu, Tu, nghia, loaitu, imageUrl, idBoTuVung);
        tuVungRef.child(idTu).setValue(tuVung).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, QLTuVungActivity.class);
                intent.putExtra("idBoTuVung", idBoTuVung);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
