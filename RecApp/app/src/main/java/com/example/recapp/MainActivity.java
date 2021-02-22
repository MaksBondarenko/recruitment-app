package com.example.recapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        ListView listView = findViewById(R.id.notesList);
        final NoteAdapter noteAdapter = new NoteAdapter(this);
        listView.setAdapter(noteAdapter);
        DbAccess.initiateDatabase(getApplicationContext());
        ImageUtils.setContext(getApplicationContext());
        DataLoader getDirectionsData = new DataLoader(getApplicationContext(),progressBar, noteAdapter);
        getDirectionsData.execute("https://run.mocky.io/v3/6125f2d0-0688-4547-aae8-0295d984f196");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg, View view, int position, long arg3) {
                Note note = (Note) noteAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, NoteInfoActivity.class);
                intent.putExtra("id",note.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    public void add(View view){
        startActivity(new Intent(MainActivity.this, NoteEditActivity.class));
        finish();
    }
}
