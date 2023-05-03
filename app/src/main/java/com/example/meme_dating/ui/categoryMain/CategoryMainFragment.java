package com.example.meme_dating.ui.categoryMain;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.Meme;
import com.example.meme_dating.R;
import com.example.meme_dating.RecylerViewAdapter;
import com.example.meme_dating.databinding.FragmentMemeScrollingBinding;

import java.util.ArrayList;
import java.util.Date;

public class CategoryMainFragment extends Fragment {
    public RecyclerView recyclerView;
    public RecylerViewAdapter recylerViewAdapter;
    public ArrayList<Meme> memesArrayList = new ArrayList<>();
    boolean isLoading = false;
    public FragmentMemeScrollingBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CategoryMainViewModel categoryMainViewModel = new ViewModelProvider(this).get(CategoryMainViewModel.class);

        binding = FragmentMemeScrollingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        populateData();
        initAdapter();
        initScrollListener();

        return root;
    }
    public void newMeme(){
        Meme newMeme = new Meme(1,"https://i.imgur.com/u4h4OoK.jpeg",1,"main"+memesArrayList.size(), new Date(),1);
        memesArrayList.add(newMeme);
    }

    public void populateData() {
        int i = 0;
        while (i < 5) {
            newMeme();
            i++;
        }
    }
    // LoadMore() method is used to implement
    // the functionality of load more
    public void loadMore() {

        memesArrayList.add(null);
        recylerViewAdapter.notifyItemInserted(memesArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                memesArrayList.remove(memesArrayList.size() - 1);
                int scrollPosition = memesArrayList.size();
                recylerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;

                // Next load more option is to be shown after every 3 items.
                int nextLimit = currentSize + 3;

                while (currentSize - 1 < nextLimit) {

                    newMeme();
                    currentSize++;
                }
                recylerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 0);
    }
    // initAdapter() method initiates the RecyclerViewAdapter
    public void initAdapter() {
        recylerViewAdapter = new RecylerViewAdapter(memesArrayList);
        recyclerView.setAdapter(recylerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    // initScrollListener() method is the method where we are checking
    // the scrolled state of the RecyclerView and if bottom-most is visible
    // we are showing the loading view and populating the next list
    public void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                             @Override
                                             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                 super.onScrollStateChanged(recyclerView, newState);
                                             }
                                             @Override
                                             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                 super.onScrolled(recyclerView, dx, dy);

                                                 LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                                 if (!isLoading) {
                                                     if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == memesArrayList.size() - 1) {
                                                         // bottom of list!
                                                         loadMore();
                                                         isLoading = true;
                                                     }
                                                 }
                                             }
                                         }
        );
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        memesArrayList.clear();
        binding = null;
    }
}