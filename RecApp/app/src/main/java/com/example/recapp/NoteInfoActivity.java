package com.example.recapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoteInfoActivity extends AppCompatActivity {
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);
        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.description);
        ImageView image = findViewById(R.id.imageView);
        note = DbAccess.findNoteById(getIntent().getLongExtra("id", 0));
        title.setText(note.getTitle());
        desc.setText(note.getDescription());
        title.setMovementMethod(new ScrollingMovementMethod());
        desc.setMovementMethod(new ScrollingMovementMethod());
        image.setImageBitmap(ImageUtils.loadImageFromStorage(note.getImgReference()));
    }

    public void edit(View view){
        Intent intent = new Intent(NoteInfoActivity.this, NoteEditActivity.class);
        intent.putExtra("id",note.getId());
        startActivity(intent);
        finish();
    }

    public void back(View view){
        startActivity(new Intent(NoteInfoActivity.this, MainActivity.class));
        finish();
    }
}
