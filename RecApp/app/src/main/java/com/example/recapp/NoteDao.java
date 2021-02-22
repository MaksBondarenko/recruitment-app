package com.example.recapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Query("SELECT imgReference FROM note WHERE id = (:noteId)")
    String findImgById(long noteId);

    @Query("SELECT * FROM note WHERE id = (:noteId)")
    Note findNoteById(long noteId);

    @Query("SELECT CASE\n" +
            "         WHEN EXISTS (SELECT 1\n" +
            "                      FROM   note\n" +
            "                      WHERE  note.id = (:noteId)) THEN 1\n" +
            "         ELSE 2\n" +
            "       END ")
    int noteExists(long noteId);

    @Insert
    long insert(Note note);

    @Update
    public void updateNotes(Note... notes);

    @Delete
    void delete(Note note);
}
