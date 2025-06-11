package ntp.example.e123.admin.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ntp.example.e123.R;
import ntp.example.e123.tracnghiem.TracNghiem;

public class QLTracNghiemActivity extends AppCompatActivity {
    private ImageView imgBack, imgAdd;
    private ArrayList<TracNghiem> listTracNghiem;
    private QLTracNghiemAdapter adapter;
    private ListView lvTracNghiem;
    private DatabaseReference tracNghiemRef;
    private String idBoHocTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_all);

        idBoHocTap = getIntent().getStringExtra("idBoTracNghiem");
        if (idBoHocTap == null || idBoHocTap.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID bộ câu hỏi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tracNghiemRef = FirebaseDatabase.getInstance().getReference("TracNghiem");

        lvTracNghiem = findViewById(R.id.listviewQL);
        imgBack = findViewById(R.id.imgBackQL);
        imgAdd = findViewById(R.id.imgAddQL);
        TextView title = findViewById(R.id.textView);
        title.setText("Quản lý trắc nghiệm");

        listTracNghiem = new ArrayList<>();
        adapter = new QLTracNghiemAdapter(this, listTracNghiem);
        lvTracNghiem.setAdapter(adapter);

        fetchTracNghiem();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QLTracNghiemActivity.this, QLBoTracNghiemActivity.class));
                finish();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLTracNghiemActivity.this, AddTracNghiemActivity.class);
                intent.putExtra("idBoTracNghiem", idBoHocTap);
                startActivity(intent);
            }
        });

    }

    private void fetchTracNghiem() {
        tracNghiemRef.orderByChild("idBoHocTap").equalTo(idBoHocTap)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listTracNghiem.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            TracNghiem tracNghiem = data.getValue(TracNghiem.class);
                            if (tracNghiem != null) {
                                listTracNghiem.add(tracNghiem);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QLTracNghiemActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
