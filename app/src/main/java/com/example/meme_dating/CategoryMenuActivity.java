package com.example.meme_dating;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meme_dating.databinding.ActivityCategoryMenuBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class CategoryMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.meme_dating.databinding.ActivityCategoryMenuBinding binding = ActivityCategoryMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarCategoryMenu.toolbar);
        binding.appBarCategoryMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "adding memes here", Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_main, R.id.nav_category1)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_category_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field =  {"username"};
                String[] data = {String.valueOf(SharedPreferencesManager.getInstance(getApplicationContext()).getUsername())};
                PutData putData = new PutData("https://meme-dating.one.pl/getUserID.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        SharedPreferencesManager.getInstance(getApplicationContext()).setUserID(Integer. parseInt(putData.getResult())) ;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_my_profile) {
            Toast.makeText(getBaseContext(), "open your profile here", Toast.LENGTH_SHORT).show();
            return true;
        }else if (item.getItemId() == R.id.action_my_instruction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("How to use");
            builder.setMessage("App allows users to both upload and download memes. You can share best memes you found on the internet and on the app. In home you can scroll all the way down looking for funny memes and like them or dislike.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }
            );
            builder.show();
            return true;

        }else if (item.getItemId() == R.id.action_logout) {
            SharedPreferencesManager.getInstance(getApplicationContext()).logOut();
            Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_category_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}