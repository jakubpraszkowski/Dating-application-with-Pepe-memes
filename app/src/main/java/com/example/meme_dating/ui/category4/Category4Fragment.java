package com.example.meme_dating.ui.category4;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.R;
import com.example.meme_dating.databinding.FragmentMemeScrollingBinding;
import com.example.meme_dating.ui.categoryMain.CategoryMainFragment;
import com.example.meme_dating.ui.categoryMain.CategoryMainViewModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Category4Fragment extends CategoryMainFragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cat_id = 4;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field =  {"cat_id"};
                String[] data = {String.valueOf(cat_id)};
                PutData putData = new PutData("https://meme-dating.one.pl/memesInCategoryCheck.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        Log.d("meme", "memes in that category: "+putData.getResult());
                        memesInThatCategory = Integer.parseInt(putData.getResult());
                    }
                }
            }
        });
        CategoryMainViewModel categoryMainViewModel = new ViewModelProvider(this).get(CategoryMainViewModel.class);

        binding = FragmentMemeScrollingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        firstMemes();
        initAdapter();
        initScrollListener();

        return root;
    }
}