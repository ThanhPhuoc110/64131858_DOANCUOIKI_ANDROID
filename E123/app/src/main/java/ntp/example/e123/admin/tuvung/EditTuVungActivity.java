package ntp.example.e123.admin.tuvung;

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
import ntp.example.e123.admin.tracnghiem.EditTracNghiemActivity;
import ntp.example.e123.admin.tracnghiem.QLTracNghiemActivity;
import ntp.example.e123.hoctuvung.TuVung;

public class EditTuVungActivity extends AppCompatActivity {
    private ImageView imgBack, imgEdit;
    private EditText edtTuVung, edtNghia, edtImageUrl;
    private Spinner spnTuLoai;
    private DatabaseReference tuVungRef;
    private String idTuVung, idBoTuVung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tu_vung);

        idTuVung = getIntent().getStringExtra("ID_TV");
        idBoTuVung = getIntent().getStringExtra("idBoTuVung");



        tuVungRef = FirebaseDatabase.getInstance().getReference("TuVung");

        imgBack = findViewById(R.id.imgBackAddTV);
        imgEdit = findViewById(R.id.imgAddTV);
        edtTuVung = findViewById(R.id.edtTuVungAddTV);
        edtNghia = findViewById(R.id.edtNghiaAddTV);
        edtImageUrl = findViewById(R.id.edturlAddTV);
        spnTuLoai = findViewById(R.id.spnLoaiTuAddTV);
        TextView title = findViewById(R.id.textView);
        title.setText("Sửa từ vựng");

        ArrayList<String> listTuLoai = new ArrayList<>();
        listTuLoai.add("Danh từ");
        listTuLoai.add("Động từ");
        listTuLoai.add("Tính từ");
        listTuLoai.add("Trạng từ");
        listTuLoai.add("Giới từ");
        spnTuLoai.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTuLoai));

        fetchTuVung();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTuVungActivity.this, QLTuVungActivity.class);
                intent.putExtra("idBoTuVung", idBoTuVung);
                startActivity(intent);
                finish();
            }
        });

imgEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updateTuVung();
    }
});    }

    private void fetchTuVung() {
        tuVungRef.child(idTuVung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TuVung tuVung = snapshot.getValue(TuVung.class);
                    if (tuVung != null) {
                        edtTuVung.setText(tuVung.getTu());
                        edtNghia.setText(tuVung.getDichnghia());
                        edtImageUrl.setText(tuVung.getAnh());

                        switch (tuVung.getLoaitu()) {
                            case "Danh từ": spnTuLoai.setSelection(0); break;
                            case "Động từ": spnTuLoai.setSelection(1); break;
                            case "Tính từ": spnTuLoai.setSelection(2); break;
                            case "Trạng từ": spnTuLoai.setSelection(3); break;
                            case "Giới từ": spnTuLoai.setSelection(4); break;
                        }
                    }
                } else {
                    Toast.makeText(EditTuVungActivity.this, "Không tìm thấy từ vựng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditTuVungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTuVung() {
        String tu = edtTuVung.getText().toString().trim();
        String nghia = edtNghia.getText().toString().trim();
        String loaitu = spnTuLoai.getSelectedItem().toString();
        String imageUrl = edtImageUrl.getText().toString().trim();

        if (tu.isEmpty() || nghia.isEmpty() || loaitu.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        tuVungRef.child(idTuVung).child("tu").setValue(tu);
        tuVungRef.child(idTuVung).child("dichnghia").setValue(nghia);
        tuVungRef.child(idTuVung).child("loaitu").setValue(loaitu);
        tuVungRef.child(idTuVung).child("anh").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditTuVungActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditTuVungActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditTuVungActivity.this, "Lỗi Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
