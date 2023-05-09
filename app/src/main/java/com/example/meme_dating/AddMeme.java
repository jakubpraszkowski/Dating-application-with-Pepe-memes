package com.example.meme_dating;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AddMeme extends AppCompatActivity {

    private int PICKFILE_RESULT_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meme);

        Button chooseFileButton = findViewById(R.id.choose_file_button);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData(); // pobranie Uri wybranego pliku
            // przetworzenie pliku z wykorzystaniem pobranego Uri
            // ...
        }
    }



}
