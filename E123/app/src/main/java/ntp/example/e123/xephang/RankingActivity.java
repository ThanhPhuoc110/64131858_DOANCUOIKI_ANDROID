package ntp.example.e123.xephang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;
import ntp.example.e123.xephang.AdapterRank;
import ntp.example.e123.xephang.UserRanking;

public class RankingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterRank adapterRank;
    private TextView tvPointrank1, tvPointrank2, tvPointrank3;
    private ImageView imgBack;
    private ArrayList<UserRanking> list;
    private Query database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.userranklist);
        tvPointrank1 = findViewById(R.id.tvpointrank1);
        tvPointrank2 = findViewById(R.id.tvpointrank2);
        tvPointrank3 = findViewById(R.id.tvpointrank3);
        imgBack = findViewById(R.id.imgVBackRank);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterRank = new AdapterRank(this, list);
        recyclerView.setAdapter(adapterRank);

        database = FirebaseDatabase.getInstance().getReference("User").orderByChild("point");

        fetchRankingData();

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(RankingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchRankingData() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserRanking userRanking = dataSnapshot.getValue(UserRanking.class);
                    list.add(userRanking);
                }

                Collections.reverse(list);

                if (list.size() >= 3) {
                    tvPointrank1.setText(String.valueOf(list.get(0).getPoint()));
                    tvPointrank2.setText(String.valueOf(list.get(1).getPoint()));
                    tvPointrank3.setText(String.valueOf(list.get(2).getPoint()));
                } else {
                    Toast.makeText(RankingActivity.this, "Chưa đủ người chơi để xếp hạng!", Toast.LENGTH_SHORT).show();
                }

                adapterRank.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RankingActivity.this, "Lỗi tải dữ liệu xếp hạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
