package com.example.meme_dating;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.example.meme_dating.ui.SharedPreferencesManager;
import com.squareup.picasso.Picasso;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.SimpleDateFormat;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private List<Meme> mItemList;
    private Context context;
    public RecylerViewAdapter(List<Meme> itemList) {
        mItemList = itemList;
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
        TextView titleTextView, authorTextView, dateTextView, categoryText, textID, logedUserID;
        Button likes, dislikes;

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
            logedUserID = itemView.findViewById(R.id.logedUserID);
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
        viewHolder.logedUserID.setText(String.valueOf(SharedPreferencesManager.getInstance(context).getUserID()));
        viewHolder.textID.setText(String.valueOf(mItemList.get(position).u_id));
        viewHolder.titleTextView.setText(mItemList.get(position).title);
        viewHolder.authorTextView.setText(mItemList.get(position).u_name);
        viewHolder.categoryText.setText(mItemList.get(position).cat_name+"/");
        viewHolder.dateTextView.setText(hoursDifference(LocalDateTime.now(), mItemList.get(position).uploadDate));
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
        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemList.get(position).reaction == 1){ //remove like
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = {"m_id", "u_id"};
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(viewHolder.logedUserID.getText())};
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
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(viewHolder.logedUserID.getText()), "1"};
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
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(viewHolder.logedUserID.getText())};
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
                            String[] data = {String.valueOf(mItemList.get(position).m_id), String.valueOf(viewHolder.logedUserID.getText()), "0"};
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
}
