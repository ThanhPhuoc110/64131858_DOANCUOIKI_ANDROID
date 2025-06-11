package ntp.example.e123.dienkhuyet;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ntp.example.e123.R;
import ntp.example.e123.MainActivity;
import ntp.example.e123.hoctuvung.FinishHTVActivity;

public class FinishDKActivity extends AppCompatActivity {
    private TextView txtCongrats, txtFinalQTrue, txtFinalText, txtFinalScore;
    private Button btnReturn;
    private DatabaseReference userRef;
    private String userId;
    private int score, questionTrue, qCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_kt);

        txtFinalScore = findViewById(R.id.Points);
        txtCongrats = findViewById(R.id.txtcongrats);
        txtFinalQTrue = findViewById(R.id.txtquestiontrue);
        txtFinalText = findViewById(R.id.txtfinaltext);
        btnReturn = findViewById(R.id.btnReturn);

        Intent intent = getIntent();
        score = intent.getIntExtra("scoreDK", 0);
        questionTrue = intent.getIntExtra("questiontrueDK", 0);
        qCount = intent.getIntExtra("qcountDK", 0);
        userId = intent.getStringExtra("userId");

        userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

        txtFinalQTrue.setText(questionTrue + " / " + qCount);
        txtCongrats.setText("Your final result:");
        txtFinalScore.setText(" " + score);

        if (questionTrue >= 4) {
            txtFinalText.setText("Awesome!!");
        } else {
            txtFinalText.setText("Good luck next time!!");
        }


        updateUserScore();

        btnReturn.setOnClickListener(v -> {
            Intent returnIntent = new Intent(FinishDKActivity.this, MainActivity.class);
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


                userRef.child("point").setValue(newScore).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(FinishDKActivity.this, "Điểm đã cập nhật ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FinishDKActivity.this, "Lỗi cập nhật điểm", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FinishDKActivity.this, "Lỗi truy cập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
