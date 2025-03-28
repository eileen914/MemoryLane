package com.example.memorylane;

public class ArchiveItem {
    private final String question;
    private final String answer;

    public ArchiveItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}