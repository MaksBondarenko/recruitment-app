package com.example.recapp;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

public class DbAccess {
    private static NoteDatabase db;
    private static NoteDao noteDao;

    public static void initiateDatabase(Context context){
        db = Room.databaseBuilder(context,
                NoteDatabase.class, "notes-database").allowMainThreadQueries().build();
        noteDao = db.noteDao();
    }

    static List<Note> getAllNotes(){
        return noteDao.getAllNotes();
    }

    static int noteExists(long noteId){
        return noteDao.noteExists(noteId);
    }

    static Note findNoteById(long noteId){
        return noteDao.findNoteById(noteId);
    }


    static long insert(Note note){
        return noteDao.insert(note);
    }


    public static void updateNotes(Note... notes){
        noteDao.updateNotes(notes);
    }
}
