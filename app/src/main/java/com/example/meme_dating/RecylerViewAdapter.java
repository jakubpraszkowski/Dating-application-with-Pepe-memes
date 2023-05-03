package com.example.meme_dating;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Meme> mItemList;
    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
    public RecylerViewAdapter(List<Meme> itemList) {
        mItemList = itemList;
    }

    // Based on the View type we are instantiating the
    // ViewHolder in the onCreateViewHolder() method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meme, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meme_loading, parent, false);
            return new LoadingviewHolder(view);
        }
    }

    // Inside the onBindViewHolder() method we
    // are checking the type of ViewHolder
    // instance and populating the row accordingly
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
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeme;
        TextView titleTextView;
        TextView authorTextView;
        TextView dateTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeme = itemView.findViewById(R.id.imageViewMeme);
            titleTextView = itemView.findViewById(R.id.titleText);
            authorTextView = itemView.findViewById(R.id.authorLink);
            dateTextView= itemView.findViewById(R.id.dateTextView);
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

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        viewHolder.titleTextView.setText(mItemList.get(position).title);
        viewHolder.authorTextView.setText("author "+mItemList.get(position).u_id);
        viewHolder.dateTextView.setText(DateFor.format(mItemList.get(position).uploadDate));

        String imageUri = mItemList.get(position).url;

        Picasso.get().load(imageUri).into(viewHolder.imageViewMeme);
    }
}
