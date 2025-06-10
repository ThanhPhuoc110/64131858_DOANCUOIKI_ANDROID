package ntp.example.e123.tracnghiem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ntp.example.e123.R;

public class DSTracNghiemActivity extends AppCompatActivity {
    private TextView txtScore, txtQuestCount, txtQuestion;
    private RadioGroup rdgChoices;
    private RadioButton btnOp1, btnOp2, btnOp3, btnOp4;
    private Button btnConfirm, btnQuit;
    private ArrayList<TracNghiem> cauTracNghiems;
    private DatabaseReference tracNghiemRef,userRef;
    private int indexCau = 0, questionTrue = 0, score = 0;
    private String idBoHocTap,userId;
    private int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dstrac_nghiem);

        idBoHocTap = getIntent().getStringExtra("IdBoHocTap");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (idBoHocTap == null || idBoHocTap.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID bộ câu hỏi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tracNghiemRef = FirebaseDatabase.getInstance().getReference("TracNghiem");
        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);


        txtScore = findViewById(R.id.txtscoreTN);
        txtQuestCount = findViewById(R.id.txtquestcountTN);
        txtQuestion = findViewById(R.id.txtquestionTN);
        rdgChoices = findViewById(R.id.radiochoices);
        btnOp1 = findViewById(R.id.op1);
        btnOp2 = findViewById(R.id.op2);
        btnOp3 = findViewById(R.id.op3);
        btnOp4 = findViewById(R.id.op4);
        btnConfirm = findViewById(R.id.btnconfirmTN);
        btnQuit = findViewById(R.id.btnQuitTN);

        cauTracNghiems = new ArrayList<>();
        fetchCauTracNghiem();

        btnConfirm.setOnClickListener(v -> {
            kiemTraDA();
            indexCau++;
            hienThiCauHoi();
        });

        btnQuit.setOnClickListener(v -> {
            Intent intent = new Intent(DSTracNghiemActivity.this, TracNghiemActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchCauTracNghiem() {
        tracNghiemRef.orderByChild("idBoHocTap").equalTo(idBoHocTap)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        cauTracNghiems.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            TracNghiem tracNghiem = data.getValue(TracNghiem.class);
                            cauTracNghiems.add(tracNghiem);
                        }
                        if (!cauTracNghiems.isEmpty()) {
                            hienThiCauHoi();
                        } else {
                            Toast.makeText(DSTracNghiemActivity.this, "Không có câu hỏi!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DSTracNghiemActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hienThiCauHoi() {
        if (indexCau >= cauTracNghiems.size()) {
            Intent intent = new Intent(DSTracNghiemActivity.this, FinishTNActivity.class);
            intent.putExtra("scoreTN", score);
            intent.putExtra("questiontrueTN", questionTrue);
            intent.putExtra("qcountTN", cauTracNghiems.size());
            intent.putExtra("userId", userId);

            startActivity(intent);
            finish();
        } else {
            TracNghiem currentQuestion = cauTracNghiems.get(indexCau);
            correctAnswer = Integer.parseInt(currentQuestion.getDapAnTrue());
            txtQuestCount.setText("Câu hỏi: " + (indexCau + 1) + "/" + cauTracNghiems.size());
            txtQuestion.setText(currentQuestion.getNoiDung());
            btnOp1.setText(currentQuestion.getDapAnA());
            btnOp2.setText(currentQuestion.getDapAnB());
            btnOp3.setText(currentQuestion.getDapAnC());
            btnOp4.setText(currentQuestion.getDapAnD());
            rdgChoices.clearCheck();
        }
    }

    private void kiemTraDA() {
        RadioButton dapAnNguoiDung = findViewById(rdgChoices.getCheckedRadioButtonId());
        if (rdgChoices.getCheckedRadioButtonId() == -1) {
            return;
        }
        if (btnOp1.isChecked() && correctAnswer == 1 || btnOp2.isChecked() && correctAnswer == 2 ||
                btnOp3.isChecked() && correctAnswer == 3 || btnOp4.isChecked() && correctAnswer == 4) {
            dapAnNguoiDung.setTextColor(Color.GREEN);
            questionTrue++;
            indexCau++;
            score += 5;
        }else {
            dapAnNguoiDung.setTextColor(Color.RED);
            dapAnNguoiDung.startAnimation(Baoloi());
        }

        txtScore.setText("Điểm: " + score);
        rdgChoices.postDelayed(new Runnable() {
            @Override
            public void run() {
                dapAnNguoiDung.setTextColor(Color.BLACK);
                hienThiCauHoi();

            }
        },2000);
    }
    TranslateAnimation Baoloi() {
        TranslateAnimation loi = new TranslateAnimation(0, 10, 0, 0);
        loi.setDuration(500);
        loi.setInterpolator(new CycleInterpolator(7));
        return loi;
    }
}
