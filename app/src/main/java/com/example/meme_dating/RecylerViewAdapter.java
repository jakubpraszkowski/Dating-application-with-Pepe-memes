package com.example.meme_dating;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final Activity myActivity;
    private List<Meme> mItemList;
    private Context context;

    public RecylerViewAdapter(List<Meme> itemList, Activity activity) {
        mItemList = itemList;
        myActivity = activity;
    }

    private static String hoursDifference(LocalDateTime date1, LocalDateTime date2) {
        long diffInMin = ChronoUnit.MINUTES.between(date2, date1);
        if (diffInMin < 60) {
            return diffInMin + "min";
        } else if ((diffInMin / 60) < 24) {
            return ChronoUnit.HOURS.between(date2, date1) + "h";
        } else {
            return ChronoUnit.DAYS.between(date2, date1) + "d";
        }
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

    private void showLoadingView(LoadingviewHolder viewHolder, int position) {
        // Progressbar would be displayed
    }

    @SuppressLint("RestrictedApi")
    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        viewHolder.textID.setText(String.valueOf(mItemList.get(position).u_id));
        viewHolder.titleTextView.setText(mItemList.get(position).title);
        viewHolder.authorTextView.setText(mItemList.get(position).u_name);
        viewHolder.categoryText.setText(mItemList.get(position).cat_name + "/");
        viewHolder.dateTextView.setText(hoursDifference(LocalDateTime.now(ZoneId.of("UTC+2")), mItemList.get(position).uploadDate));
        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));

        if (mItemList.get(position).reaction == 1) { // liked
            viewHolder.likes.setBackgroundResource(R.drawable.like_button_bg_selected);
        } else if (mItemList.get(position).reaction == -1) { // disliked
            viewHolder.dislikes.setBackgroundResource(R.drawable.like_button_bg_selected);
        }
        String imageUri = mItemList.get(position).url;
        Picasso.get().load(imageUri).into(viewHolder.imageViewMeme);

        viewHolder.imageButtonMenuPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, viewHolder.imageButtonMenuPopup);
                menu.inflate(R.menu.popup_meme_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_download) {
                            if (isStoragePermissionGranted()) {
                                String state = Environment.getExternalStorageState();
                                if (Environment.MEDIA_MOUNTED.equals(state)) {
                                    Picasso.get().load(imageUri).into(new Target() {
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
                                } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {

                                    Toast.makeText(context, "Storage is read only", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(context, "Storage does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (item.getItemId() == R.id.action_share) {
                            Picasso.get().load(imageUri).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        values.put(MediaStore.Images.Media.IS_PENDING, 1);
                                    }
                                    Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                    try {
                                        OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                        outputStream.close();
                                        values.clear();
                                        values.put(MediaStore.Images.Media.IS_PENDING, 0);
                                        context.getContentResolver().update(uri, values, null, null);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");
                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    startActivity(context, Intent.createChooser(share, "Select"), null);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            });
                        }
                        return false;
                    }
                });
                menu.setForceShowIcon(true);
                menu.show();
            }
        });
        viewHolder.imageViewMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = LayoutInflater.from(context).inflate(R.layout.show_full_image_popup, null, false);
                ImageView imageView = popupView.findViewById(R.id.imageView2);
                ImageButton closeButton = popupView.findViewById(R.id.imageButton3);
                ;
                Picasso.get().load(imageUri).into(imageView);

                int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        viewHolder.authorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfile.class);
                intent.putExtra("user_id", mItemList.get(position).u_id);
                view.getContext().startActivity(intent);
//        Toast
//            .makeText(view.getContext(),
//                      "open profile of user with id: " +
//                          mItemList.get(position).u_id,
//                      Toast.LENGTH_SHORT)
//            .show();
            }
        });
        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemList.get(position).reaction == 1) { // remove like
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
                                        mItemList.get(position).likes = mItemList.get(position).likes - 1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        viewHolder.likes.setBackgroundResource(R.drawable.like_button_bg);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                } else if (mItemList.get(position).reaction == -1) { // change from dislike to like
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
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes - 1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        viewHolder.dislikes.setBackgroundResource(R.drawable.like_button_bg);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                    Handler handler2 = new Handler();
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id", "reaction"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()), "1"};
                            PutData putData = new PutData("https://meme-dating.one.pl/addReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).likes = mItemList.get(position).likes + 1;
                                        mItemList.get(position).reaction = 1;
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        viewHolder.likes.setBackgroundResource(R.drawable.like_button_bg_selected);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                } else { // add like
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
                                        mItemList.get(position).likes = mItemList.get(position).likes + 1;
                                        mItemList.get(position).reaction = 1;
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        viewHolder.likes.setBackgroundResource(R.drawable.like_button_bg_selected);
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
                if (mItemList.get(position).reaction == -1) { // remove dislike
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
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes - 1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        viewHolder.dislikes.setBackgroundResource(R.drawable.like_button_bg);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                } else if (mItemList.get(position).reaction == 1) { // change from like to dislike
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
                                        mItemList.get(position).likes = mItemList.get(position).likes - 1;
                                        mItemList.get(position).reaction = 3;
                                        viewHolder.likes.setText(String.valueOf(mItemList.get(position).likes));
                                        viewHolder.likes.setBackgroundResource(R.drawable.like_button_bg);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                    Handler handler2 = new Handler();
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id", "reaction"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()), "-1"};
                            PutData putData = new PutData("https://meme-dating.one.pl/addReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes + 1;
                                        mItemList.get(position).reaction = 0;
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        viewHolder.dislikes.setBackgroundResource(R.drawable.like_button_bg_selected);
                                        Log.d("likes", putData.getData());
                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }
                    });
                } else { // add dislike
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id", "reaction"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()), "-1"};
                            PutData putData = new PutData("https://meme-dating.one.pl/addReaction.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    try {
                                        mItemList.get(position).dislikes = mItemList.get(position).dislikes + 1;
                                        mItemList.get(position).reaction = 0;
                                        viewHolder.dislikes.setText(String.valueOf(mItemList.get(position).dislikes));
                                        viewHolder.dislikes.setBackgroundResource(R.drawable.like_button_bg_selected);
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("LOG", "Permission granted");
                return true;
            } else {
                Log.v("LOG", "Permission revoked");
                ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("LOG", "Permission is granted");
            return true;
        }
    }

    public void saveImageToDownloadFolder(String imageFile, Bitmap ibitmap) {
        try {
            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), imageFile);
            int counter = 1;
            String newFileName = imageFile;
            String extension = newFileName.substring(newFileName.lastIndexOf("."));
            while (filePath.exists()) {
                newFileName = imageFile.substring(0, imageFile.lastIndexOf('.')) + "(" + counter + ")" + extension;
                filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), newFileName);
                counter++;
            }
            OutputStream outputStream = new FileOutputStream(filePath);
            ibitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(context, "Saved " + newFileName + " in Download Folder", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeme;
        TextView titleTextView, authorTextView, dateTextView, categoryText, textID;
        Button likes, dislikes;
        ImageButton imageButtonMenuPopup;

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
            imageButtonMenuPopup = itemView.findViewById(R.id.imageButtonMenuPopup);
        }
    }

    private class LoadingviewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingviewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
