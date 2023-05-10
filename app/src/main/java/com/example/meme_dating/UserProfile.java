package com.example.meme_dating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    ListView categoryPointsListView;
    Button backButton;
    ArrayAdapter<String> adapter;
    List<String> categories = new ArrayList<>();
    List<Integer> points = new ArrayList<>();

    RadarChart radarChart;
    String username;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);



        userId = getIntent().getIntExtra("user_id", 0);

        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[1];
            field[0] = "u_id";
            String[] data = new String[1];
            data[0] = String.valueOf(userId);
            PutData putData = new PutData("https://meme-dating.one.pl/getUserProfile.php", "POST", field, data);

            radarChart = findViewById(R.id.radar_chart);


            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        ArrayList<String> categories = new ArrayList<>();
                        ArrayList<Integer> points = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            username = jsonObject.getString("username");
                            String category = jsonObject.optString("title", "Brak kategorii");
                            int pointsValue = jsonObject.optInt("points", 0);

                            categories.add(category);
                            points.add(pointsValue);
                        }
                        setTitle(username+"'s profile");
                        RadarChart radarChart = findViewById(R.id.radar_chart);

                        int min = findMin(points);
                        int max = findMax(points);
                        if(max!=0){
                            if(min > 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) - Math.abs(min)) * 100 / (max - Math.abs(min)));
                                }
                            }else if(min < 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) + Math.abs(min)) * 100 / (max + Math.abs(min)));
                                }
                            }else{
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i)) * 100 / (max + Math.abs(min)));
                                }
                            }
                        }else{
                            if(min > 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) - Math.abs(min)) * 100);
                                }
                            }else if(min < 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) + Math.abs(min)) * 100);
                                }
                            }
                        }

                        ArrayList<RadarEntry> entries = new ArrayList<>();
                        for (int i = 0; i < categories.size(); i++) {
                            entries.add(new RadarEntry(points.get(i)));
                        }

                        RadarDataSet dataSet = new RadarDataSet(entries, "Categories");
                        dataSet.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                        dataSet.setFillColor(ColorTemplate.COLORFUL_COLORS[0]);
                        dataSet.setDrawFilled(true);
                        dataSet.setFillAlpha(180);
                        dataSet.setLineWidth(2f);
                        dataSet.setDrawHighlightCircleEnabled(true);
                        dataSet.setDrawHighlightIndicators(false);

                        RadarData dataRadar = new RadarData(dataSet);

                        ArrayList<String> labels = new ArrayList<>();
                        for (int i = 0; i < categories.size(); i++) {
                            labels.add(categories.get(i));
                        }

                        XAxis xAxis = radarChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                        radarChart.getYAxis().setAxisMinimum(0);
                        radarChart.getYAxis().setAxisMaximum(100);
                        radarChart.getYAxis().setLabelCount(6, true);
                        radarChart.getYAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return value+"%";
                            }
                        });
                        radarChart.setData(dataRadar);
                        radarChart.getDescription().setEnabled(false);
                        radarChart.getLegend().setEnabled(false);
                        radarChart.invalidate();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_white_24);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(userId != SharedPreferencesManager.getInstance(getApplicationContext()).getUserID()){
            getMenuInflater().inflate(R.menu.profile_compare_menu, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }else if(item.getItemId() == R.id.compareProfileMenu) {
            //Toast.makeText(getBaseContext(), "open your profile here "+ SharedPreferencesManager.getInstance(getApplicationContext()).getUserID(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CompareProfile.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static Integer findMin(List<Integer> list)
    {

        // check list is empty or not
        if (list == null || list.size() == 0) {
            return Integer.MAX_VALUE;
        }

        // create a new list to avoid modification
        // in the original list
        List<Integer> sortedlist = new ArrayList<>(list);

        // sort list in natural order
        Collections.sort(sortedlist);

        // first element in the sorted list
        // would be minimum
        return sortedlist.get(0);
    }

    // function return maximum value in an unsorted
    // list in Java using Collection
    public static Integer findMax(List<Integer> list)
    {

        // check list is empty or not
        if (list == null || list.size() == 0) {
            return Integer.MIN_VALUE;
        }

        // create a new list to avoid modification
        // in the original list
        List<Integer> sortedlist = new ArrayList<>(list);

        // sort list in natural order
        Collections.sort(sortedlist);

        // last element in the sorted list would be maximum
        return sortedlist.get(sortedlist.size() - 1);
    }
}