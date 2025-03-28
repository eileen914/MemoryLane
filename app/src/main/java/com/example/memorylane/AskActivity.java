package com.example.memorylane;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memorylane.database.MemoryDatabaseHelper;

public class AskActivity extends AppCompatActivity {

    private MemoryDatabaseHelper dbHelper;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        // 사용자 이름 수신
        userName = getIntent().getStringExtra("USER_NAME");
        dbHelper = new MemoryDatabaseHelper(this);
        // UI 요소 연결, 키보드 자동 표시
        EditText editTextQuestion = findViewById(R.id.editTextQuestion);
        Button buttonSaveQuestion = findViewById(R.id.buttonSaveQuestion);
        editTextQuestion.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextQuestion, InputMethodManager.SHOW_IMPLICIT);
        // 질문 저장 버튼 클릭 이벤트
        buttonSaveQuestion.setOnClickListener(view -> {
            String question = editTextQuestion.getText().toString().trim();
            if (!question.isEmpty()) {
                // 데이터베이스에 질문 저장
                saveQuestionToDatabase(question);
                // Toast 메시지 표시
                Toast.makeText(this, "Question has been saved!", Toast.LENGTH_SHORT).show();
                // MenuActivity로 돌아가기
                Intent intent = new Intent(AskActivity.this, MenuActivity.class);
                intent.putExtra("USER_NAME", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please enter a question!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // 데이터베이스에 질문 저장
    private void saveQuestionToDatabase(String question) {
        dbHelper.getWritableDatabase().execSQL(
                "INSERT INTO UserMemory (userName, question, answer) VALUES (?, ?, ?);",
                new String[]{userName, question, ""}
        );
    }
}