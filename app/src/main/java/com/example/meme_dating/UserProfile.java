package com.example.meme_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    TextView usernameTextView, categoryTextView, pointsTextView;
    Button backButton;
    ArrayList<String> memeCategories;
    ArrayAdapter<String> adapter;
    String username;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // pobranie przekazanego ID użytkownika z intencji


        usernameTextView = findViewById(R.id.user_name);
        categoryTextView = findViewById(R.id.categoryTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        usernameTextView = findViewById(R.id.user_name);
        categoryTextView = findViewById(R.id.categoryTextView);
        pointsTextView = findViewById(R.id.pointsTextView);

// pobranie przekazanego ID użytkownika z intencji
        int userId = getIntent().getIntExtra("user_id", 0);

        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[1];
            field[0] = "u_id";
            String[] data = new String[1];
            data[0] = String.valueOf(userId);
            PutData putData = new PutData("https://meme-dating.one.pl/LoginRegister/getUserProfile.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        int points = 0;
                        String category = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            username = jsonObject.getString("username");
                            category += jsonObject.getString("title") + "\n";
                            points += jsonObject.getInt("points");
                        }
                        usernameTextView.setText(username);
                        categoryTextView.setText(category);
                        pointsTextView.setText(String.valueOf(points));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}


