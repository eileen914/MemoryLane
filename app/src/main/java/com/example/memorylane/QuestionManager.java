package com.example.memorylane;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.memorylane.database.MemoryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager {

    // Singleton 인스턴스
    private static QuestionManager instance;

    // 기본 질문 리스트 및 데이터베이스 관리자
    private final List<String> questionList;
    private final MemoryDatabaseHelper dbHelper;

    // 생성자 - Singleton 보호
    private QuestionManager(Context context) {
        questionList = new ArrayList<>();
        dbHelper = new MemoryDatabaseHelper(context.getApplicationContext());

        // 기본 질문을 데이터베이스에 추가
        initializeDefaultQuestions();
    }

    // Singleton 인스턴스 반환
    public static synchronized QuestionManager getInstance(Context context) {
        if (instance == null) {
            instance = new QuestionManager(context);
        }
        return instance;
    }

    // 기본 질문을 데이터베이스에 추가하는 메서드
    private void initializeDefaultQuestions() {
        if (!areQuestionsInitialized()) {
            addDefaultQuestionsToDatabase();
        }
    }

    // 데이터베이스에 기본 질문 추가
    private void addDefaultQuestionsToDatabase() {
        String[] defaultQuestions = {
                "What do you want to achieve this week?",
                "What motivates you to keep going?",
                "What is your biggest dream in life?",
                "How do you define success?",
                "What is something new you want to learn?",
                "What are you most grateful for today?",
                "What habit would you like to develop?",
                "Who inspires you the most and why?",
                "What makes you feel truly alive?",
                "How do you handle stress effectively?",
                "What is your proudest achievement so far?",
                "How do you want to be remembered?",
                "If you could have any superpower, what would it be?",
                "What’s the weirdest food you’ve ever tried?",
                "If you could time travel, where would you go?",
                "What’s a silly fear you have?",
                "What does happiness mean to you?",
                "What’s the most unusual thing on your bucket list?",
                "What’s your favorite childhood memory?",
                "If you could swap lives with anyone for a day, who would it be?"
        };

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (String question : defaultQuestions) {
            db.execSQL("INSERT INTO UserMemory (userName, question, answer) VALUES (?, ?, ?);",
                    new String[]{"default", question, ""});
        }
    }

    // 기본 질문이 이미 데이터베이스에 있는지 확인
    private boolean areQuestionsInitialized() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM UserMemory WHERE userName = 'default';", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    // 질문 리스트 반환 (기본 질문 + 사용자 질문 포함)
    public List<String> getQuestionList(String userName) {
        List<String> questionList = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT question FROM UserMemory WHERE userName = ? OR userName = 'default'",
                new String[]{userName});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                questionList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return questionList;
    }

    // 사용자별 아카이빙된 질문-답변 리스트 반환
    public List<String> getArchivedList(String userName) {
        List<String> archiveList = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT question, answer FROM UserMemory WHERE userName = ?;",
                new String[]{userName});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String question = cursor.getString(0);
                String answer = cursor.getString(1);
                archiveList.add("Q: " + question + "\nA: " + answer);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return archiveList;
    }

    // 특정 질문 삭제
    public void removeQuestion(String userName, String question) {
        dbHelper.getWritableDatabase().execSQL(
                "DELETE FROM UserMemory WHERE (userName = ? OR userName = 'default') AND question = ?;",
                new String[]{userName, question});
    }
}