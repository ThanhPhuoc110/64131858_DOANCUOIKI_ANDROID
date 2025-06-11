package ntp.example.e123.admin.dienkhuyet;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.R;
import ntp.example.e123.dienkhuyet.CauDienKhuyet;

public class EditDienKhuyetActivity extends AppCompatActivity {
    private ImageView imgBack, imgEdit;
    private EditText edtNoiDung, edtGoiY, edtDapAn;
    private DatabaseReference dienKhuyetRef;
    private String idCau, idBoDienKhuyet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dien_khuyet);

        idCau = getIntent().getStringExtra("ID_DK");
        idBoDienKhuyet = getIntent().getStringExtra("ID_Bo");

        if (idCau == null || idBoDienKhuyet == null || idCau.isEmpty() || idBoDienKhuyet.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID câu điền khuyết!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dienKhuyetRef = FirebaseDatabase.getInstance().getReference("DienKhuyet").child(idCau);

        imgBack = findViewById(R.id.imgBackAddDK);
        imgEdit = findViewById(R.id.imgAddDK);
        edtNoiDung = findViewById(R.id.edtCauHoiAddDK);
        edtGoiY = findViewById(R.id.edtGoiYAddDK);
        edtDapAn = findViewById(R.id.edtDapAnAddDK);
        TextView title =findViewById(R.id.textView);
        title.setText("Sửa câu điền khuyết");
        fetchDienKhuyet();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDienKhuyetActivity.this, QLDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", idBoDienKhuyet);
                startActivity(intent);
                finish();
            }
        });


        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDienKhuyet();
            }
        });
    }

    private void fetchDienKhuyet() {
        dienKhuyetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CauDienKhuyet dienKhuyet = snapshot.getValue(CauDienKhuyet.class);
                    if (dienKhuyet != null) {
                        edtNoiDung.setText(dienKhuyet.getNoiDung());
                        edtGoiY.setText(dienKhuyet.getGoiY());
                        edtDapAn.setText(dienKhuyet.getDapAn());
                    }
                } else {
                    Toast.makeText(EditDienKhuyetActivity.this, "Không tìm thấy câu điền khuyết!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditDienKhuyetActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDienKhuyet() {
        String noiDung = edtNoiDung.getText().toString().trim();
        String dapAn = edtDapAn.getText().toString().trim();
        String goiY = edtGoiY.getText().toString().trim();

        if (noiDung.isEmpty() || dapAn.isEmpty() || goiY.isEmpty()) {
            Toast.makeText(this, "Chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        dienKhuyetRef.child("noiDung").setValue(noiDung);
        dienKhuyetRef.child("dapAn").setValue(dapAn);
        dienKhuyetRef.child("goiY").setValue(goiY)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditDienKhuyetActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditDienKhuyetActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
