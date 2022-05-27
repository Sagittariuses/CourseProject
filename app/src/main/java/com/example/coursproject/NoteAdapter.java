package com.example.coursproject;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private View item;
    private List<Model> mModelList;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    NoteAdapter(List<Model> modelList, View itemView) {
        item = itemView;
        mModelList = modelList;
    }
    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Model model = mModelList.get(position);
        holder.TitleTV.setText(model.getTitle());
        holder.NoteTV.setText(model.getNote());
        holder.DateTV.setText(model.getDate());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        int len = mModelList.size();
        if (len > 0)
        {
            return mModelList.size();
        } else{
            return 0;
        }
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TitleTV;
        TextView NoteTV;
        TextView DateTV;
        TextView idTV;

        ViewHolder(View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.idTV);
            TitleTV = itemView.findViewById(R.id.TitleTV);
            NoteTV = itemView.findViewById(R.id.NoteTV);
            DateTV = itemView.findViewById(R.id.DateTV);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    Integer getItem(int position) {
        final Model model = mModelList.get(position);
        return model.getid();
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
