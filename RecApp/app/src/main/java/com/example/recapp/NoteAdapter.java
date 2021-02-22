package com.example.recapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private List<Note> notes;
    private Context context;
    private List<Bitmap> icons;

    public void setIcons(List<Bitmap> icons) {
        this.icons = icons;
    }

    public NoteAdapter(Context context) {
        this.context = context;
        notes = new ArrayList<>();
        icons = new ArrayList<>();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notes.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.note_row, viewGroup, false);
        TextView placeName = row.findViewById(R.id.noteName);
        final ImageView imageView = row.findViewById(R.id.noteImg);
        placeName.setText(notes.get(position).getTitle());
        imageView.setImageBitmap(icons.get(position));
        return row;
    }
}
