package ntp.example.e123.admin.dienkhuyet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import ntp.example.e123.R;
import ntp.example.e123.dienkhuyet.CauDienKhuyet;

public class AddDienKhuyetActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd;
    EditText edtNoiDung, edtGoiY, edtDapAn;
    private DatabaseReference dienKhuyetRef;
    String idBoDienKhuyet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dien_khuyet);
        imgBack = (ImageView) findViewById(R.id.imgBackAddDK);
        imgAdd = (ImageView) findViewById(R.id.imgAddDK);
        edtNoiDung = (EditText) findViewById(R.id.edtCauHoiAddDK);
        edtGoiY = (EditText) findViewById(R.id.edtGoiYAddDK);
        edtDapAn = (EditText) findViewById(R.id.edtDapAnAddDK);
         idBoDienKhuyet = getIntent().getStringExtra("idBoDienKhuyet");
        dienKhuyetRef = FirebaseDatabase.getInstance().getReference("DienKhuyet");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDienKhuyetActivity.this, QLDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", idBoDienKhuyet);
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(v -> addDienKhuyet());

    }
    private void addDienKhuyet() {
        String noiDung = edtNoiDung.getText().toString().trim();
        String dapAn = edtDapAn.getText().toString().trim();
        String goiY = edtGoiY.getText().toString().trim();
        String idCau = dienKhuyetRef.push().getKey();
        CauDienKhuyet caudienkhuyet = new CauDienKhuyet(idCau,idBoDienKhuyet,noiDung,dapAn,goiY);
        if (noiDung.isEmpty() || dapAn.isEmpty() || goiY.isEmpty()) {
            Toast.makeText(this, "Chưa điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }




        dienKhuyetRef.child(idCau).setValue(caudienkhuyet).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddDienKhuyetActivity.this, QLDienKhuyetActivity.class);
                intent.putExtra("idBoDienKhuyet", idBoDienKhuyet);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}