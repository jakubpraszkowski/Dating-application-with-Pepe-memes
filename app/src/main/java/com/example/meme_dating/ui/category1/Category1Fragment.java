package com.example.meme_dating.ui.category1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.Meme;
import com.example.meme_dating.R;

import com.example.meme_dating.databinding.FragmentMemeScrollingBinding;
import com.example.meme_dating.ui.categoryMain.CategoryMainFragment;
import com.example.meme_dating.ui.categoryMain.CategoryMainViewModel;

import java.util.Date;

public class Category1Fragment extends CategoryMainFragment {


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
        Meme newMeme = new Meme(1,"https://i.imgur.com/q8d7HTp.jpeg",1,"cat"+memesArrayList.size(), new Date(),1);
        memesArrayList.add(newMeme);
    }


}