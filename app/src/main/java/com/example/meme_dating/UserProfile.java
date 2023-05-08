package com.example.meme_dating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

public class UserProfile extends AppCompatActivity {

    TextView usernameTextView;
    ListView categoryPointsListView;
    Button backButton;
    ArrayAdapter<String> adapter;
    List<String> categories = new ArrayList<>();
    List<Integer> points = new ArrayList<>();
    String username;

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

        usernameTextView = findViewById(R.id.user_name);
        categoryPointsListView = findViewById(R.id.listViewMemes);


        int userId = getIntent().getIntExtra("user_id", 0);

        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[1];
            field[0] = "u_id";
            String[] data = new String[1];
            data[0] = String.valueOf(userId);
            PutData putData = new PutData("http://10.0.2.2/getUserProfile.php", "POST", field, data);


            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            username = jsonObject.getString("username");
                            String category = jsonObject.optString("title", "Brak kategorii");
                            int point = jsonObject.optInt("points", -1);
                            if(point > -1)
                                categories.add("Kategoria: " + category + "\nZdobyte punkty:" + point );
                            else categories.add("Brak osiągnięć!");

                        }
                        usernameTextView.setText(username);

                        adapter = new ArrayAdapter<>(UserProfile.this,
                                android.R.layout.simple_list_item_1, categories);
                        categoryPointsListView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}


