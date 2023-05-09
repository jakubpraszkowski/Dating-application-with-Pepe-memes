package com.example.meme_dating;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddMeme extends AppCompatActivity {

    private int PICKFILE_RESULT_CODE;
    Spinner categorySpinner;
    Uri uri = null;

    public void addMeme(View view) {
        Categories selectedCategory = (Categories) categorySpinner.getSelectedItem();
        if( !edit.getText().toString().isEmpty() && uri != null){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {

                        new UploadFileTask(
                                getApplicationContext(),
                                String.valueOf(selectedCategory.id),
                                edit.getText().toString(),
                                String.valueOf(SharedPreferencesManager.getInstance(getApplicationContext()).getUserID()),
                                "https://meme-dating.one.pl/uploadMeme.php",
                                uri
                        ).doInBackground();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            Toast.makeText(getApplicationContext(), "Meme added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("added",true);
            setResult(2,intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Make sure to choose everything", Toast.LENGTH_SHORT).show();
            Log.d("edit", String.valueOf(!edit.toString().isEmpty()));
            Log.d("uri", String.valueOf(uri != null));
        }
    }

    private class Categories{
        public String name;
        public int id;
        public Categories(String name, int id){
            this.name = name;
            this.id = id;
        }
        @Override
        public String toString() {
            return this.name;
        }
    }
    EditText edit;
    List<Categories> categories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meme);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_white_24);
        setTitle("Add meme");
        categories.add(new Categories("IT", 1));
        categories.add(new Categories("Funny", 2));
        categories.add(new Categories("Anime", 3));
        categories.add(new Categories("Religious", 4));
        categories.add(new Categories("Dark Humor", 5));
        categories.add(new Categories("Academical", 7));
        categories.add(new Categories("Queer", 8));
        categories.add(new Categories("Gaming", 9));

        categorySpinner = findViewById(R.id.category_spinner);
        ArrayAdapter<Categories> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        Button chooseFileButton = findViewById(R.id.choose_file_button);
        edit = findViewById(R.id.title_edittext);

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // utworzenie nowego intentu dla wyboru pliku z galerii
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // typ pliku do wyświetlenia
                startActivityForResult(intent, PICKFILE_RESULT_CODE); // uruchomienie aktywności i oczekiwanie na wynik
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
        }
    }
}
