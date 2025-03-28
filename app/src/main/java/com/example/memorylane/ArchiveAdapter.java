package com.example.memorylane;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder> {

    private final List<ArchiveItem> archivedList;

    // 어댑터 생성자
    public ArchiveAdapter(List<ArchiveItem> archivedList) {
        this.archivedList = archivedList;
    }

    @NonNull
    @Override
    public ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_archive, parent, false);
        return new ArchiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveViewHolder holder, int position) {
        ArchiveItem item = archivedList.get(position);
        holder.textViewQuestionCard.setText("Q: " + item.getQuestion());
        holder.textViewAnswerCard.setText("A: " + item.getAnswer());
    }

    @Override
    public int getItemCount() {
        return archivedList.size();
    }

    // 뷰홀더 클래스
    static class ArchiveViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestionCard;
        TextView textViewAnswerCard;

        public ArchiveViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestionCard = itemView.findViewById(R.id.textViewQuestionCard);
            textViewAnswerCard = itemView.findViewById(R.id.textViewAnswerCard);
        }
    }
}