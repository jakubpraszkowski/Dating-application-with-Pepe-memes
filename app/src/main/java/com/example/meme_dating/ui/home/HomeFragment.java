package com.example.meme_dating.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.Meme;
import com.example.meme_dating.R;
import com.example.meme_dating.RecylerViewAdapter;
import com.example.meme_dating.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecylerViewAdapter recylerViewAdapter;
    ArrayList<Meme> memesArrayList = new ArrayList<>();

    boolean isLoading = false;
    boolean memesEnd = false;
    int lowestId;
    int currentCatId = 0;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        populateData();
        initAdapter();
        initScrollListener();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void newMeme(){
        Meme newMeme = new Meme(1,"https://i.imgur.com/u4h4OoK.jpeg",1,"title", new Date(),1);
        memesArrayList.add(newMeme);
    }

    private void populateData() {
        int i = 0;
        while (i < 5) {
            //zapytanie do bazy

            newMeme();
            i++;
        }
    }
    // initAdapter() method initiates the RecyclerViewAdapter
    private void initAdapter() {

        recylerViewAdapter = new RecylerViewAdapter(memesArrayList);
        recyclerView.setAdapter(recylerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    // initScrollListener() method is the method where we are checking
    // the scrolled state of the RecyclerView and if bottom-most is visible
    // we are showing the loading view and populating the next list
    private void initScrollListener() {
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

    // LoadMore() method is used to implement
    // the functionality of load more
    private void loadMore() {

        if(memesEnd){return;}

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
        }, 2000);
    }
}