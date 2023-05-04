package com.example.meme_dating.ui.categoryMain;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class CategoryMainFragment extends Fragment {
    public RecyclerView recyclerView;
    public RecylerViewAdapter recylerViewAdapter;
    public ArrayList<Meme> memesArrayList = new ArrayList<>();
    boolean isLoading = false;
    public FragmentMemeScrollingBinding binding;
    public int cat_id;
    public int memesInThatCategory;
    public int userID = 7;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cat_id = 0;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field =  {"cat_id"};
                String[] data = {String.valueOf(cat_id)};
                PutData putData = new PutData("http://10.0.2.2/memesInCategoryCheck.php", "POST", field, data);
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
    public void newMeme(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"cat_id", "lastMemeId"};
                String[] data = {String.valueOf(cat_id), memesArrayList.get(memesArrayList.size()-1).m_idTostring()};
                PutData putData = new PutData("http://10.0.2.2/getNewMeme.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            JSONObject obj = new JSONObject(putData.getResult());

                            Log.d("new meme", obj.toString());

                            memesArrayList.add(
                                new Meme(
                                    obj.getInt("m_id"),
                                    obj.getString("url"),
                                    obj.getString("cat_title"),
                                    obj.getString("title"),
                                    new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("add_date") ),
                                    obj.getInt("u_id"),
                                    obj.getString("username"),
                                        0,0,5
                                )
                            );
                            //recylerViewAdapter.notifyDataSetChanged();
                        } catch (Throwable t) {
                            Log.e("new meme", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }

                    }
                }
            }
        });
        Handler handler2 = new Handler();
        handler2.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"m_id", "u_id"};
                String[] data = {String.valueOf(memesArrayList.get(memesArrayList.size()-1).m_id), String.valueOf(userID)};
                PutData putData = new PutData("http://10.0.2.2/getMemeReaction.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            JSONObject obj = new JSONObject(putData.getResult());
                            Log.d("likes", obj.toString());
                            memesArrayList.get(memesArrayList.size()-1).likes = obj.getInt("likes");
                            memesArrayList.get(memesArrayList.size()-1).dislikes = obj.getInt("dislikes");
                            memesArrayList.get(memesArrayList.size()-1).reaction = obj.getInt("reaction");
                            recylerViewAdapter.notifyDataSetChanged();
                        } catch (Throwable t) {
                            Log.e("first meme", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }
                    }
                }
            }
        });
    }

    public void firstMemes() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"cat_id"};
                String[] data = {String.valueOf(cat_id)};
                PutData putData = new PutData("http://10.0.2.2/getLatestMeme.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            JSONObject obj = new JSONObject(putData.getResult());
                            Log.d("first meme", obj.toString());
                            memesArrayList.add(new Meme(obj.getInt("m_id"), obj.getString("url"), obj.getString("cat_title"), obj.getString("title"), new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("add_date") ), obj.getInt("u_id"), obj.getString("username"), 0,0,3));
                            //recylerViewAdapter.notifyDataSetChanged();
                        } catch (Throwable t) {
                            Log.e("first meme", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }
                    }
                }
            }
        });
        Handler handler2 = new Handler();
        handler2.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"m_id", "u_id"};
                String[] data = {String.valueOf(memesArrayList.get(0).m_id), String.valueOf(userID)};
                PutData putData = new PutData("http://10.0.2.2/getMemeReaction.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            JSONObject obj = new JSONObject(putData.getResult());
                            Log.d("likes", obj.toString());
                            memesArrayList.get(memesArrayList.size()-1).likes = obj.getInt("likes");
                            memesArrayList.get(memesArrayList.size()-1).dislikes = obj.getInt("dislikes");
                            memesArrayList.get(memesArrayList.size()-1).reaction = obj.getInt("reaction");
                            recylerViewAdapter.notifyDataSetChanged();
                        } catch (Throwable t) {
                            Log.e("first meme", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }
                    }
                }
            }
        });
        int i = 0;
        while (i < 4){
            newMeme();
            i++;
        }
    }
    // LoadMore() method is used to implement
    // the functionality of load more
    public void loadMore() {

        if(memesArrayList.size()>=memesInThatCategory){return;}

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

                    if(memesArrayList.size()>=memesInThatCategory){return;}

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
        isLoading = false;
        binding = null;
    }
}