package com.example.meme_dating.ui.categoryMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryMainViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public CategoryMainViewModel() {
        mText = new MutableLiveData<>();
    }
}