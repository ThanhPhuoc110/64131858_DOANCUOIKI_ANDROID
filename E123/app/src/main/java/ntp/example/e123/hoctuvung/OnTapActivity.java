package ntp.example.e123.hoctuvung;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ntp.example.e123.R;

public class OnTapActivity extends AppCompatActivity {
     TextView textQuestion, tvScore, tvWord;
    EditText editText;
     Button btnOk, btnQuitHTV;
     ImageView imgView;
     DatabaseReference tuVungRef, userRef;
    String idBoHocTap, userId;
   ArrayList<TuVung> danhSachTuVung;
     int indexCauHoi = 0;
    int diemSo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_tap);

        Intent intent = getIntent();
        idBoHocTap = intent.getStringExtra("idbo");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tuVungRef = FirebaseDatabase.getInstance().getReference("TuVung");
        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        danhSachTuVung = new ArrayList<>();

        textQuestion = findViewById(R.id.textQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvWord = findViewById(R.id.tvWord);
        editText = findViewById(R.id.editText);
        btnOk = findViewById(R.id.btnOk);
        btnQuitHTV = findViewById(R.id.btnQuitHTV);
        imgView = findViewById(R.id.imgview);

        btnQuitHTV.setOnClickListener(v -> finish());
        btnOk.setOnClickListener(v -> kiemTraDapAn());

        fetchTuVung();
    }

    private void fetchTuVung() {
        if (idBoHocTap == null || idBoHocTap.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không có ID bộ học tập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tuVungRef.orderByChild("idBoHocTap").equalTo(idBoHocTap).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSachTuVung.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TuVung tuVung = data.getValue(TuVung.class);
                    danhSachTuVung.add(tuVung);
                }
                if (!danhSachTuVung.isEmpty()) {
                    Collections.shuffle(danhSachTuVung);
                    hienThiCauHoi();
                } else {
                    Toast.makeText(OnTapActivity.this, "Không có từ vựng để kiểm tra!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OnTapActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

     void hienThiCauHoi() {
        if (indexCauHoi >= danhSachTuVung.size()) {



            Intent intent = new Intent(OnTapActivity.this, FinishHTVActivity.class);
            intent.putExtra("score", diemSo);
            intent.putExtra("questiontrue", indexCauHoi);
            intent.putExtra("qcount", danhSachTuVung.size());
            intent.putExtra("userId", userId);
            startActivity(intent);

            return;
        }

        TuVung tuVung = danhSachTuVung.get(indexCauHoi);
        textQuestion.setText(tuVung.getLoaitu() + " - " + tuVung.getDichnghia());
        tvWord.setText("Word: " + (indexCauHoi + 1) + "/" + danhSachTuVung.size());
        Glide.with(this)
                .load(tuVung.getAnh())
                .placeholder(R.drawable.icboss)
                .error(R.drawable.ic_lock)
                .into(imgView);
        editText.setText("");
        editText.setTextColor(Color.BLACK);

    }

    void kiemTraDapAn() {
        editText.setTextColor(Color.BLACK);
        if (indexCauHoi >= danhSachTuVung.size()) {
            return;
        }
        TuVung tuVung = danhSachTuVung.get(indexCauHoi);
        String dapAnNguoiDung = editText.getText().toString().trim();
        if(dapAnNguoiDung.isEmpty()){
            editText.startAnimation(Baoloi());
            return;
        }


        if (dapAnNguoiDung.equalsIgnoreCase(tuVung.getTu())) {
            editText.setTextColor(Color.GREEN);
            indexCauHoi++;
            diemSo++;
            tvScore.setText("Score: " + diemSo);

        } else {
            editText.setTextColor(Color.RED);
            editText.startAnimation(Baoloi());


        }
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.setTextColor(Color.BLACK);
                hienThiCauHoi();
            }
        }, 2000);
    }
    TranslateAnimation Baoloi() {
        TranslateAnimation loi = new TranslateAnimation(0, 10, 0, 0);
        loi.setDuration(500);
        loi.setInterpolator(new CycleInterpolator(7));
        return loi;
    }


}
