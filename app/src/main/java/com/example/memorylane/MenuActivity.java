package com.example.memorylane;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private String userName;  // 사용자 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // 사용자 이름 수신
        userName = getIntent().getStringExtra("USER_NAME");
        // 버튼 연결
        Button buttonAsk = findViewById(R.id.btnAsk);
        Button buttonReply = findViewById(R.id.btnReply);
        Button buttonViewArchive = findViewById(R.id.btnMemoryLane);
        // 질문 입력 화면으로 이동
        buttonAsk.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, AskActivity.class);
            intent.putExtra("USER_NAME", userName);  // 사용자 이름 전달
            startActivity(intent);
        });
        // 답변 입력 화면으로 이동
        buttonReply.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, ReplyActivity.class);
            intent.putExtra("USER_NAME", userName);  // 사용자 이름 전달
            startActivity(intent);
        });
        // 아카이브 보기 화면으로 이동
        buttonViewArchive.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, MemoryLaneActivity.class);
            // SQLite 아카이빙 데이터 전달
            intent.putExtra("archivedList", new ArrayList<>(
                    QuestionManager.getInstance(getApplicationContext()).getArchivedList(userName)
            ));
            intent.putExtra("USER_NAME", userName);  // 사용자 이름 전달
            startActivity(intent);
        });
    }
}