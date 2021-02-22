package com.example.recapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NoteEditActivity extends AppCompatActivity {
    private Note note;
    private TextView title;
    private TextView desc;
    private ImageView image;
    private boolean newOne = true;
    private static final int PICK_IMAGE = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        image = findViewById(R.id.imageView);
        note = DbAccess.findNoteById(getIntent().getLongExtra("id", -1));
        if(note!=null){
            title.setText(note.getTitle());
            desc.setText(note.getDescription());
            bitmap = ImageUtils.loadImageFromStorage(note.getImgReference());
            image.setImageBitmap(bitmap);
            newOne = false;
        }
    }

    public void ok(View view){
        if(title.getText().equals("")||desc.getText().equals("")||bitmap==null){
            error().show();
        }
        else{
            if(newOne)
                note = new Note();
            note.setTitle(title.getText().toString());
            note.setDescription(desc.getText().toString());
            String imgRef = ImageUtils.saveToInternalStorage(note,bitmap);
            note.setImgReference(imgRef);
            if(newOne)
                note.setId(DbAccess.insert(note));
            else
                DbAccess.updateNotes(note);
            Intent intent = new Intent(NoteEditActivity.this, NoteInfoActivity.class);
            intent.putExtra("id",note.getId());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE&& resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(ImageUtils.cropBitmapToSquare(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectIMG(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void back(View view){
        if(newOne)
            startActivity(new Intent(NoteEditActivity.this, MainActivity.class));
        else {
            Intent intent = new Intent(NoteEditActivity.this, NoteInfoActivity.class);
            intent.putExtra("id",note.getId());
            startActivity(intent);
        }
        finish();
    }

    public android.app.AlertDialog.Builder error() {
        return new android.app.AlertDialog.Builder(NoteEditActivity.this)
                .setTitle("Error")
                .setMessage("Please enter name, description and select an image")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }
}
