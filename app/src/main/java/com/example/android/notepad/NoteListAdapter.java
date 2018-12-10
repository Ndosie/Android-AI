package com.example.android.notepad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private final LayoutInflater mInflater;
    private List<NoteEntry> mNotes; // Cached copy of words
    private static ClickListener clickListener;

    NoteListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        if (mNotes != null) {
            NoteEntry current = mNotes.get(position);
            holder.noteItemView.setText(current.getContent());
        } else {
            // Covers the case of data not being ready yet.
            holder.noteItemView.setText(R.string.empty_no_content);
        }
    }

    void setNotes(List<NoteEntry> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mNotes has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }

    public NoteEntry getNoteAtPosition (int position) {
        return mNotes.get(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteItemView;

        private NoteViewHolder(View itemView) {
            super(itemView);
            noteItemView = itemView.findViewById(R.id.note);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        NoteListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
