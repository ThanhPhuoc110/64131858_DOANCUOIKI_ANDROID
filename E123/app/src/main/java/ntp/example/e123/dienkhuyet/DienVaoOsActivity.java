package ntp.example.e123.dienkhuyet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
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

public class DienVaoOsActivity extends AppCompatActivity {
    private TextView txtScoreDK, txtQuestCountDK, txtQuestionDK, txtGoiy;
    private EditText edtAnswerDK;
    private Button btnConfirm, btnQuit;
    private DatabaseReference dienKhuyetRef, userRef;
    private ArrayList<CauDienKhuyet> cauDienKhuyets;
    private int indexCau = 0;
    private int questionTrue = 0;
    private int diemSo = 0;
    private String idBoDienKhuyet, answer, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_vao_os);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent intent = getIntent();
        idBoDienKhuyet = intent.getStringExtra("BoDK");

        txtScoreDK = findViewById(R.id.txtscoreDK);
        txtQuestCountDK = findViewById(R.id.txtquestcountDK);
        txtQuestionDK = findViewById(R.id.txtquestionDK);
        txtGoiy = findViewById(R.id.textviewGoiy);
        edtAnswerDK = findViewById(R.id.AnswerDK);
        btnConfirm = findViewById(R.id.btnconfirmDK);
        btnQuit = findViewById(R.id.btnQuitDK);

        dienKhuyetRef = FirebaseDatabase.getInstance().getReference("DienKhuyet");
        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        cauDienKhuyets = new ArrayList<>();
        fetchCauDienKhuyet();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemTraDapAn();
            }
        });
        btnQuit.setOnClickListener(v -> {
            Intent quitIntent = new Intent(DienVaoOsActivity.this, DienKhuyetActivity.class);
            startActivity(quitIntent);
        });
    }

    private void fetchCauDienKhuyet() {
        dienKhuyetRef.orderByChild("idBo").equalTo(idBoDienKhuyet).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cauDienKhuyets.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    CauDienKhuyet cauDienKhuyet = data.getValue(CauDienKhuyet.class);
                    cauDienKhuyets.add(cauDienKhuyet);
                }
                if (!cauDienKhuyets.isEmpty()) {
                    hienThiCauHoi();
                } else {
                    Toast.makeText(DienVaoOsActivity.this, "Không có câu điền khuyết để kiểm tra!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DienVaoOsActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiCauHoi() {
        if (indexCau >= cauDienKhuyets.size()) {
            Intent intent = new Intent(DienVaoOsActivity.this, FinishDKActivity.class);
            intent.putExtra("scoreDK", diemSo);
            intent.putExtra("questiontrueDK", questionTrue);
            intent.putExtra("qcountDK", cauDienKhuyets.size());
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
            return;
        }

        CauDienKhuyet cau = cauDienKhuyets.get(indexCau);
        answer = cau.getDapAn();
        txtQuestCountDK.setText("Question: " + (indexCau+ 1) + "/" + cauDienKhuyets.size());
        txtGoiy.setText(cau.getGoiY());
        txtQuestionDK.setText(cau.getNoiDung());
        edtAnswerDK.setText("");
        edtAnswerDK.setTextColor(Color.BLACK);
        edtAnswerDK.setBackgroundResource(R.drawable.bgbtn);    }


    private void kiemTraDapAn() {
        if (indexCau >= cauDienKhuyets.size()) {
            return;
        }
        CauDienKhuyet dsduyenKhuyet = cauDienKhuyets.get(indexCau);
        String dapAnNguoiDung = edtAnswerDK.getText().toString().trim();

        if(dapAnNguoiDung.isEmpty()){
            edtAnswerDK.startAnimation(Baoloi());

            return;
        }
        if (dapAnNguoiDung.equalsIgnoreCase(dsduyenKhuyet.getDapAn())) {
            edtAnswerDK.setTextColor(Color.GREEN);
            questionTrue++;
            diemSo += 5;
            txtScoreDK.setText("Score: " + diemSo);
        } else {
            edtAnswerDK.setTextColor(Color.RED);
            edtAnswerDK.startAnimation(Baoloi());
        }
        edtAnswerDK.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtAnswerDK.setTextColor(Color.BLACK);
                indexCau++;
                hienThiCauHoi();
            }
        }, 2000);
    }

    private TranslateAnimation Baoloi() {
        TranslateAnimation loi = new TranslateAnimation(0, 10, 0, 0);
        loi.setDuration(500);
        loi.setInterpolator(new CycleInterpolator(7));
        return loi;
    }
}
