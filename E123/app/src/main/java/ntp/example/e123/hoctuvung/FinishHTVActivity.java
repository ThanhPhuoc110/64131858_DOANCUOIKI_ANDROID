package ntp.example.e123.hoctuvung;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.MainActivity;
import ntp.example.e123.R;

public class FinishHTVActivity extends AppCompatActivity {
    private TextView txtCongrats, txtFinalQTrue, txtFinalText, txtFinalScore;
    private Button btnReturn;
    private DatabaseReference userRef;
    private String userId;
    private int score, questionTrue, qcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish_kt);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        questionTrue = intent.getIntExtra("questiontrue", 0);
        qcount = intent.getIntExtra("qcount", 0);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
        txtFinalScore = findViewById(R.id.Points);
        txtCongrats = findViewById(R.id.txtcongrats);
        txtFinalQTrue = findViewById(R.id.txtquestiontrue);
        txtFinalText = findViewById(R.id.txtfinaltext);
        btnReturn = findViewById(R.id.btnReturn);
        txtFinalQTrue.setText(questionTrue + " / " + qcount);
        txtCongrats.setText("Your final result:");
        txtFinalScore.setText(" " + score);

        if (questionTrue >= 4) {
            txtFinalText.setText("Almost there!!");
        } else {
            txtFinalText.setText("Good luck next time!!");
        }


        updateUserScore();


        btnReturn.setOnClickListener(v -> {
            Intent returnIntent = new Intent(FinishHTVActivity.this, MainActivity.class);
            startActivity(returnIntent);
            finish();
        });
    }

    private void updateUserScore() {
        userRef.child("point").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int currentScore = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                int newScore = currentScore + score;

                // Lưu điểm mới
                userRef.child("point").setValue(newScore).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(FinishHTVActivity.this, "Điểm đã cập nhật ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FinishHTVActivity.this, "Lỗi cập nhật điểm", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FinishHTVActivity.this, "Lỗi truy cập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
