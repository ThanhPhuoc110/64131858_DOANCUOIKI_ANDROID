package ntp.example.e123.admin.dienkhuyet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import ntp.example.e123.dienkhuyet.CauDienKhuyet;

public class QLDienKhuyetActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd;
    ArrayList<CauDienKhuyet> listDienKhuyet;
    QLDienKhuyetAdapter adapter;
    ListView lvDienKhuyet;
    private DatabaseReference dienKhuyetRef;
    private String idBoDienKhuyet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ql_all);
        Intent idIntent = getIntent();
        idBoDienKhuyet = idIntent.getStringExtra("idBoDienKhuyet");
        lvDienKhuyet = findViewById(R.id.listviewQL);
        imgBack = findViewById(R.id.imgBackQL);
        imgAdd = findViewById(R.id.imgAddQL);
        TextView title = findViewById(R.id.textView);
        title.setText("Quản lý điền khuyết");
        if (idBoDienKhuyet == null || idBoDienKhuyet.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID bộ điền khuyết!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dienKhuyetRef = FirebaseDatabase.getInstance().getReference("DienKhuyet");
        listDienKhuyet = new ArrayList<>();
        adapter = new QLDienKhuyetAdapter(this, listDienKhuyet);
        lvDienKhuyet.setAdapter(adapter);

        fetchDienKhuyet();
        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(QLDienKhuyetActivity.this, QLBoDienKhuyetActivity.class));
            finish();
        });
        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(QLDienKhuyetActivity.this, AddDienKhuyetActivity.class);
            intent.putExtra("idBoDienKhuyet", idBoDienKhuyet);
            startActivity(intent);
        });


    }
    private void fetchDienKhuyet() {
        dienKhuyetRef.orderByChild("idBo").equalTo(idBoDienKhuyet).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDienKhuyet.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    CauDienKhuyet cauDienKhuyet = data.getValue(CauDienKhuyet.class);
                    listDienKhuyet.add(cauDienKhuyet);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLDienKhuyetActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}