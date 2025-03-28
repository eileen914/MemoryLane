package com.example.memorylane;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 뷰 초기화
        editTextUserName = findViewById(R.id.editTextUserName);
        Button buttonClick = findViewById(R.id.buttonClick);
        // MemoryLane 텍스트뷰에 커스텀 폰트 적용
        TextView textView = findViewById(R.id.textView);
        try {
            // assets 폴더에서 폰트 로드
            Typeface customFont = Typeface.createFromAsset(getAssets(),
                    "font/notoserif_extracondensed_semiboldItalic.ttf");
            textView.setTypeface(customFont);
        } catch (Exception e) {
            Toast.makeText(this, "Font not found!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // 버튼 클릭 이벤트
        buttonClick.setOnClickListener(view -> {
            String userName = editTextUserName.getText().toString().trim();

            if (!userName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("USER_NAME", userName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}