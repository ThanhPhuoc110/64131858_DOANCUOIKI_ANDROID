package ntp.example.e123.admin.tuvung;

import android.content.Intent;
import android.os.Bundle;
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

import ntp.example.e123.R;
import ntp.example.e123.hoctuvung.TuVung;

public class QLTuVungActivity extends AppCompatActivity {
    private ImageView imgBack, imgAdd;
    private ListView lvTuVung;
    private ArrayList<TuVung> listTuVung;
    private QLTuVungAdapter adapter;
    private DatabaseReference tuVungRef;
    private String idBoTuVung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_all);

        idBoTuVung = getIntent().getStringExtra("idBoTuVung");
        lvTuVung = findViewById(R.id.listviewQL);
        imgBack = findViewById(R.id.imgBackQL);
        imgAdd = findViewById(R.id.imgAddQL);
        TextView title= findViewById(R.id.textView);
        title.setText("Quản lý từ vựng");

        tuVungRef = FirebaseDatabase.getInstance().getReference("TuVung");

        listTuVung = new ArrayList<>();
        adapter = new QLTuVungAdapter(this, listTuVung);
        lvTuVung.setAdapter(adapter);

        fetchTuVung();

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(QLTuVungActivity.this, QLBoTuVungActivity.class);
            finishAffinity();
            startActivity(intent);
        });

        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(QLTuVungActivity.this, AddTuVungActivity.class);
            intent.putExtra("idBoTuVung", idBoTuVung);
            finishAffinity();
            startActivity(intent);
        });
    }

    private void fetchTuVung() {
        tuVungRef.orderByChild("idBoHocTap").equalTo(idBoTuVung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTuVung.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TuVung tuVung = data.getValue(TuVung.class);
                    listTuVung.add(tuVung);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QLTuVungActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
