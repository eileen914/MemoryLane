package com.example.memorylane;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memorylane.database.MemoryDatabaseHelper;

import java.util.List;
import java.util.Random;

public class ReplyActivity extends AppCompatActivity {

    private String currentQuestion;  // 현재 표시된 질문
    private String userName;         // 현재 사용자 이름
    private MemoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        // 사용자 이름 수신
        userName = getIntent().getStringExtra("USER_NAME");

        // 데이터베이스 초기화
        dbHelper = new MemoryDatabaseHelper(this);

        // UI 요소 연결
        TextView textViewQuestion = findViewById(R.id.textViewQuestion);
        EditText editTextAnswer = findViewById(R.id.editTextAnswer);
        Button buttonSaveAnswer = findViewById(R.id.buttonSaveAnswer);

        // 질문 리스트 로딩
        List<String> questionList = loadQuestionsFromDatabase();
        // 랜덤 질문 선택
        if (!questionList.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(questionList.size());
            currentQuestion = questionList.get(index);
            textViewQuestion.setText(currentQuestion);  // TextView에 질문 표시
        } else {
            textViewQuestion.setText("No more questions available!");
            buttonSaveAnswer.setEnabled(false);  // 저장 버튼 비활성화
        }

        // 키보드 강제 표시
        editTextAnswer.post(() -> {
            editTextAnswer.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextAnswer, InputMethodManager.SHOW_IMPLICIT);
        });

        // 답변 저장 버튼 클릭 이벤트
        buttonSaveAnswer.setOnClickListener(view -> {
            String answer = editTextAnswer.getText().toString().trim();

            if (!answer.isEmpty() && currentQuestion != null) {
                // 답변 저장
                saveAnswerToDatabase(currentQuestion, answer);
                // 안내 메시지 표시
                Toast.makeText(this, "Answer has been saved!", Toast.LENGTH_SHORT).show();
                // MenuActivity로 이동
                Intent intent = new Intent(ReplyActivity.this, MenuActivity.class);
                intent.putExtra("USER_NAME", userName);  // 사용자 이름 전달
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 이전 스택 정리
                startActivity(intent);
                finish();  // 현재 액티비티 종료
            } else {
                Toast.makeText(this, "Please enter your answer!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 데이터베이스에서 질문 로드 (기본 + 사용자 질문)
    private List<String> loadQuestionsFromDatabase() {
        return QuestionManager.getInstance(getApplicationContext()).getQuestionList(userName);
    }


    // SQLite 데이터베이스에 답변 저장 (트랜잭션 보장)
    private void saveAnswerToDatabase(String question, String answer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  // 트랜잭션 시작
        try {
            db.execSQL(
                    "INSERT OR REPLACE INTO UserMemory (userName, question, answer) VALUES (?, ?, ?);",
                    new String[]{userName, question, answer}
            );
            db.setTransactionSuccessful();  // 트랜잭션 커밋

            // 답변 저장 성공 시 MemoryLaneActivity로 직접 이동
            Intent intent = new Intent(ReplyActivity.this, MemoryLaneActivity.class);
            intent.putExtra("USER_NAME", userName);  // 사용자 이름 전달
            startActivity(intent);
            finish();  // 현재 ReplyActivity 종료
        } catch (Exception e) {
            Toast.makeText(this, "Error saving answer.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();  // 오류 로그
        } finally {
            db.endTransaction();  // 트랜잭션 종료
        }
    }
}