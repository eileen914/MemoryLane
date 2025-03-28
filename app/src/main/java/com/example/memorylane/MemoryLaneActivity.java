package com.example.memorylane;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorylane.database.MemoryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MemoryLaneActivity extends AppCompatActivity {

    private static final String TAG = "MemoryLaneActivity";
    private MemoryDatabaseHelper dbHelper;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_lane);

        // 사용자 이름 수신
        userName = getIntent().getStringExtra("USER_NAME");
        dbHelper = new MemoryDatabaseHelper(this);

        // RecyclerView 설정
        RecyclerView recyclerViewArchive = findViewById(R.id.recyclerViewArchive);
        recyclerViewArchive.setLayoutManager(new LinearLayoutManager(this));
        // 데이터 로딩
        List<ArchiveItem> archivedList = loadArchivedData();
        if (!archivedList.isEmpty()) {
            ArchiveAdapter adapter = new ArchiveAdapter(archivedList);
            recyclerViewArchive.setAdapter(adapter);  // RecyclerView 연결
            Toast.makeText(this, "Records loaded successfully.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Records loaded successfully for user: " + userName);
        } else {
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No records found for user: " + userName);
        }
    }


    // 데이터베이스에서 질문-답변 로드 (답변이 있는 항목만 로드)
    private List<ArchiveItem> loadArchivedData() {
        List<ArchiveItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT question, answer FROM UserMemory WHERE userName = ? AND answer IS NOT NULL AND answer != '';",
                new String[]{userName}
        )) {
            if (cursor.moveToFirst()) {
                do {
                    String question = cursor.getString(0);
                    String answer = cursor.getString(1);
                    list.add(new ArchiveItem(question, answer));
                    Log.d(TAG, "Loaded record: " + question + " -> " + answer);
                } while (cursor.moveToNext());
            } else {
                Log.d(TAG, "Cursor is empty for user: " + userName);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading records: ", e);
            Toast.makeText(this, "Database error occurred.", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}