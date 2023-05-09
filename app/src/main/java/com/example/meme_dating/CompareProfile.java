package com.example.meme_dating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class CompareProfile extends AppCompatActivity {

    ListView categoryPointsListView;
    Button backButton;
    ArrayAdapter<String> adapter;
    List<String> categories = new ArrayList<>();
    List<Integer> points = new ArrayList<>();

    RadarChart radarChart;
    String username1, username2;
    RadarDataSet radarDataSet1, radarDataSet2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_profile);

        int userId = getIntent().getIntExtra("user_id", 0);

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
                            username1 = jsonObject.getString("username");
                            String category = jsonObject.optString("title", "Brak kategorii");
                            int pointsValue = jsonObject.optInt("points", 0);

                            categories.add(category);
                            points.add(pointsValue);
                        }

                        int min = findMin(points);
                        int max = findMax(points);
                        if(max!=0){
                            if(min > 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) - Math.abs(min)) * 100 / max);
                                }
                            }else if(min < 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) + Math.abs(min)) * 100 / max);
                                }
                            }else{
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i)) * 100 / max);
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

                        radarDataSet1 = new RadarDataSet(entries, username1);
                        radarDataSet1.setColor(Color.rgb(0,255,127));
                        radarDataSet1.setFillColor(Color.rgb(0,255,127));
                        radarDataSet1.setDrawFilled(true);
                        radarDataSet1.setFillAlpha(50);
                        radarDataSet1.setLineWidth(2f);
                        radarDataSet1.setDrawHighlightCircleEnabled(true);
                        radarDataSet1.setDrawHighlightIndicators(false);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Handler handler2 = new Handler();
        handler2.post(() -> {
            String[] field = new String[1];
            field[0] = "u_id";
            String[] data = new String[1];
            data[0] = String.valueOf(SharedPreferencesManager.getInstance(this).getUserID());
            PutData putData = new PutData("https://meme-dating.one.pl/getUserProfile.php", "POST", field, data);

            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        ArrayList<String> categories = new ArrayList<>();
                        ArrayList<Integer> points = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            username2 = jsonObject.getString("username");
                            String category = jsonObject.optString("title", "Brak kategorii");
                            int pointsValue = jsonObject.optInt("points", 0);

                            categories.add(category);
                            points.add(pointsValue);
                        }

                        int min = findMin(points);
                        int max = findMax(points);
                        if(max!=0){
                            if(min > 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) - Math.abs(min)) * 100 / max);
                                }
                            }else if(min < 0){
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i) + Math.abs(min)) * 100 / max);
                                }
                            }else{
                                for (int i = 0; i < points.size(); i++) {
                                    points.set(i, (points.get(i)) * 100 / max);
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

                        radarDataSet2 = new RadarDataSet(entries, username2);
                        radarDataSet2.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                        radarDataSet2.setFillColor(ColorTemplate.COLORFUL_COLORS[0]);
                        radarDataSet2.setDrawFilled(true);
                        radarDataSet2.setFillAlpha(50);
                        radarDataSet2.setLineWidth(2f);
                        radarDataSet2.setDrawHighlightCircleEnabled(true);
                        radarDataSet2.setDrawHighlightIndicators(false);

                        RadarData radarData = new RadarData();
                        radarData.addDataSet(radarDataSet1);
                        radarData.addDataSet(radarDataSet2);

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

                        radarChart.setData(radarData);
                        radarChart.getDescription().setEnabled(false);
                        radarChart.getLegend().setEnabled(true);
                        radarChart.invalidate();
                        setTitle(username1+ " vs " + username2);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
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