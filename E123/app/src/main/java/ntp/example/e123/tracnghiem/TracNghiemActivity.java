package ntp.example.e123.tracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.bohoctap.BoHocTap;
import ntp.example.e123.bohoctap.BoHocTapAdapter;

public class TracNghiemActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<BoHocTap> boHocTapArrayList;
    private BoHocTapAdapter boHocTapAdapter;
    private ImageView imgBack;
    private DatabaseReference boHocTapRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tu_vung);

        boHocTapRef = FirebaseDatabase.getInstance().getReference("BoHocTap");
        listView = findViewById(R.id.listviewHTV);
        imgBack = findViewById(R.id.imgVBackHTV);
        TextView title = findViewById(R.id.textView);
        title.setText("Bộ Trắc Nghiệm");
        boHocTapArrayList = new ArrayList<>();
        boHocTapAdapter = new BoHocTapAdapter(this, R.layout.row_bohoctap, boHocTapArrayList);
        listView.setAdapter(boHocTapAdapter);

        fetchBoHocTap();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            BoHocTap selectedBoHocTap = boHocTapArrayList.get(position);
            Intent quizIntent = new Intent(TracNghiemActivity.this, DSTracNghiemActivity.class);
            quizIntent.putExtra("IdBoHocTap", selectedBoHocTap.getIdBo());
            startActivity(quizIntent);
        });

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(TracNghiemActivity.this, MainActivity.class);
            intent.putExtras(getIntent());
            startActivity(intent);
            finish();
        });
    }

    private void fetchBoHocTap() {
        boHocTapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boHocTapArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BoHocTap boHocTap = data.getValue(BoHocTap.class);
                    if (boHocTap != null) {
                        boHocTapArrayList.add(boHocTap);
                    }
                }
                boHocTapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TracNghiemActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
