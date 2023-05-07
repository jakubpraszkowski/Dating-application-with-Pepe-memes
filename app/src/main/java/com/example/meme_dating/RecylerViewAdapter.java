package com.example.meme_dating;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.SimpleDateFormat;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private List<Meme> mItemList;
    private Context context;
    private Activity myActivity;
    public RecylerViewAdapter(List<Meme> itemList, Activity activity) {
        mItemList = itemList;
        myActivity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meme, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meme_loading, parent, false);
            return new LoadingviewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingviewHolder) {
            showLoadingView((LoadingviewHolder) holder, position);
        }
    }

    // getItemCount() method returns the size of the list
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    // getItemViewType() method is the method where we check each element
    // of the list. If the element is NULL we set the view type as 1 else 0
    public int getItemViewType(int position) {
        int VIEW_TYPE_LOADING = 1;
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeme;
        TextView titleTextView, authorTextView, dateTextView, categoryText, textID;
        Button likes, dislikes;
        ImageButton imageButton, imageButton2;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeme = itemView.findViewById(R.id.imageViewMeme);
            titleTextView = itemView.findViewById(R.id.titleText);
            authorTextView = itemView.findViewById(R.id.authorLink);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            categoryText = itemView.findViewById(R.id.categoryText);
            textID = itemView.findViewById(R.id.textID);
            likes = itemView.findViewById(R.id.likeButton);
            dislikes = itemView.findViewById(R.id.dislikeButton);
            imageButton = itemView.findViewById(R.id.imageButton);
            imageButton2 = itemView.findViewById(R.id.imageButton2);
        }
    }
    private class LoadingviewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingviewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
    private void showLoadingView(LoadingviewHolder viewHolder, int position) {
        // Progressbar would be displayed
    }
    private static String hoursDifference(LocalDateTime date1, LocalDateTime date2) {
        long diffInMin = ChronoUnit.MINUTES.between(date2, date1);
        if(diffInMin < 60 ){
            return diffInMin+"min";
        }else if((diffInMin/60) < 24){
            return ChronoUnit.HOURS.between(date2, date1)+"h";
        }else {
            return ChronoUnit.DAYS.between(date2, date1)+"d";
        }
    }
    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        viewHolder.textID.setText(String.valueOf(mItemList.get(position).u_id));
        viewHolder.titleTextView.setText(mItemList.get(position).title);
        viewHolder.authorTextView.setText(mItemList.get(position).u_name);
        viewHolder.categoryText.setText(mItemList.get(position).cat_name+"/");
        viewHolder.dateTextView.setText(hoursDifference(LocalDateTime.now(ZoneId.of("UTC+2")), mItemList.get(position).uploadDate));
        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));

        if(mItemList.get(position).reaction == 1){ //liked
            viewHolder.dislikes.setEnabled(false);
        }else if(mItemList.get(position).reaction == 0){ //disliked
            viewHolder.likes.setEnabled(false);
        }
        String imageUri = mItemList.get(position).url;
        Picasso.get().load(imageUri).into(viewHolder.imageViewMeme);
        viewHolder.authorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "open profile of user with id: "+mItemList.get(position).u_id, Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted()){
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)){
                        Picasso.get()
                                .load(imageUri)
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        saveImageToDownloadFolder(Uri.parse(imageUri).getLastPathSegment(), bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });
                    } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){

                        Toast.makeText(context, "Storage is read only", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(context, "Storage does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(view.getContext(), "download", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imageButton2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent shareIntent = new Intent();
                   shareIntent.setAction(Intent.ACTION_SEND);
                   shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
                   shareIntent.setType("image/png");
                   startActivity(context, Intent.createChooser(shareIntent, "share"), null);
               }
           });
        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemList.get(position).reaction == 1){ //remove like
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID())};
                            PutData putData = new PutData("https://meme-dating.one.pl/removeReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).likes = mItemList.get(position).likes-1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.dislikes.setEnabled(true);
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                }else { //add like
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id", "reaction"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()), "1"};
                            PutData putData = new PutData("https://meme-dating.one.pl/addReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).likes = mItemList.get(position).likes+1;
                                        mItemList.get(position).reaction = 1;
                                        viewHolder.dislikes.setEnabled(false);
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {

                                    }

                                }
                            }
                        }
                    });
                }
            }
        });
        viewHolder.dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemList.get(position).reaction == 0){ //remove dislike
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID())};
                            PutData putData = new PutData("https://meme-dating.one.pl/removeReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes-1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.likes.setEnabled(true);
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                }else { //dislike
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id", "reaction"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()), "0"};
                            PutData putData = new PutData("https://meme-dating.one.pl/addReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes+1;
                                        mItemList.get(position).reaction = 0;
                                        viewHolder.likes.setEnabled(false);
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    public boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.v("LOG", "Permission granted");
                return true;
            } else {
                Log.v("LOG", "Permission revoked");
                ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else{
            Log.v("LOG", "Permission is granted");
            return true;
        }
    }
    public void saveImageToDownloadFolder(String imageFile, Bitmap ibitmap){
        try {
            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), imageFile);
            int counter = 1;
            String newFileName = imageFile;
            String extension = newFileName.substring(newFileName.lastIndexOf("."));
            while(filePath.exists()){
                newFileName = imageFile.substring(0, imageFile.lastIndexOf('.') )+"("+counter+")"+extension;
                filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), newFileName);
                counter++;
            }
            OutputStream outputStream = new FileOutputStream(filePath);
            ibitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context,  "Saved "+newFileName+" in Download Folder", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
