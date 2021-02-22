package com.example.recapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataLoader extends AsyncTask<String, String, JSONObject> {
    private static String LOG = "JSONLoader";


    private ProgressBar progressBar;
    private NoteAdapter adapter;
    private List<Bitmap> icons = new ArrayList<>();


    DataLoader(Context context, ProgressBar progressBar, NoteAdapter adapter) {
        this.progressBar = progressBar;
        this.adapter = adapter;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try{
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                String data = sb.toString();
                bufferedReader.close();
                JSONObject json = new JSONObject(data);
                walkThroughPosts(json);
                return json;
            }
        }catch (Exception e){
            Log.e(LOG, "JSON download error: " + e.getMessage());
        }
        return null;
    }

    //Create notes and download images
    private void walkThroughPosts(JSONObject data){
        try {
            JSONArray posts = data.getJSONArray("posts");
            List<Note> notes = DbAccess.getAllNotes();
            for(int i=0;i<posts.length();i++) {
                JSONObject json = posts.getJSONObject(i);
                Log.d(LOG," note id: "+DbAccess.noteExists(json.getLong("id")));
                if (DbAccess.noteExists(json.getInt("id"))==2) {
                    Note note = new Note(
                            json.getLong("id"),
                            json.getString("title"),
                            json.getString("description"),
                            json.getString("icon")
                    );
                    DbAccess.insert(note);
                    String url = json.getString("icon");
                    if (url.charAt(4) != 's') {
                        StringBuilder sb = new StringBuilder(url);
                        sb.insert(4, 's');
                        url = sb.toString();
                    }
                    URL imgURL = new URL(url);
                    InputStream in = new BufferedInputStream(imgURL.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while ((n = in.read(buf)) != -1) {
                        out.write(buf, 0, n);
                    }
                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0, response.length, null);
                    String imgRef = ImageUtils.saveToInternalStorage(note,bitmap);
                    note.setImgReference(imgRef);
                    DbAccess.updateNotes(note);
                }
            }
        } catch (Exception e) {
            Log.e(LOG, "Error occurred: "+e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(JSONObject data) {
        try {
            progressBar.setVisibility(View.GONE);
            List<Note> notes = DbAccess.getAllNotes();
            for(Note note : notes){
                Log.d(LOG," img ref = "+note.getImgReference());
                icons.add(ImageUtils.loadImageFromStorage(note.getImgReference()));
            }
            adapter.setIcons(icons);
            adapter.setNotes(notes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
