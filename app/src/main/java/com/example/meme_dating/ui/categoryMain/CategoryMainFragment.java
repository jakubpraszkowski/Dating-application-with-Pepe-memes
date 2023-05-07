package com.example.meme_dating.ui.categoryMain;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meme_dating.Meme;
import com.example.meme_dating.R;
import com.example.meme_dating.RecylerViewAdapter;
import com.example.meme_dating.databinding.FragmentMemeScrollingBinding;
import com.example.meme_dating.ui.SharedPreferencesManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class CategoryMainFragment extends Fragment {
    public RecyclerView recyclerView;
    public RecylerViewAdapter recylerViewAdapter;
    public ArrayList<Meme> memesArrayList = new ArrayList<>();
    boolean isLoading = false;
    public FragmentMemeScrollingBinding binding;
    public int cat_id;
    public int memesInThatCategory;
    public int latestMemeId;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cat_id = 0;
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
    public void newMemes(int count){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                if(memesArrayList.get(memesArrayList.size()-1)== null){
                    memesArrayList.remove(memesArrayList.size()-1);
                    recylerViewAdapter.notifyDataSetChanged();
                }
                if(memesArrayList.size()>=memesInThatCategory){return;}
                String[] field = {"u_id", "cat_id", "lastMemeId", "count"};
                String[] data = {
                        String.valueOf(SharedPreferencesManager.getInstance(getContext()).getUserID()),
                        String.valueOf(cat_id),
                        String.valueOf(latestMemeId),
                        String.valueOf(count)
                };
                PutData putData = new PutData("https://meme-dating.one.pl/getNewMemes.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            Log.d("newMemes", putData.getResult());
                            JSONArray jsonarray = new JSONArray(putData.getResult());
                            for (int i = 0; i < jsonarray.length(); i++) {
                                if(memesArrayList.size()>=memesInThatCategory){return;}
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Log.d("newMemes", obj.toString());

                                memesArrayList.add(new Meme(
                                    obj.getInt("m_id"),
                                    obj.getString("url"),
                                    obj.getString("cat_title"),
                                    obj.getString("title"),
                                        LocalDateTime.parse(obj.getString("add_date"), formatter),
                                    obj.getInt("u_id"),
                                    obj.getString("username"),
                                    obj.getInt("likes"),
                                    obj.getInt("dislikes"),
                                    obj.getInt("reaction")));
                                latestMemeId = obj.getInt("m_id");
                                recylerViewAdapter.notifyItemInserted(memesArrayList.size() - 1);
                            }
                        } catch (Throwable t) {
                            Log.e("newMemes", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }
                    }
                }
            }
        });

    }
    public void firstMemes() {
        memesArrayList.add(null);
        final Meme[] meme = new Meme[1];
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = {"cat_id", "u_id"};
                String[] data = {String.valueOf(cat_id), String.valueOf(SharedPreferencesManager.getInstance(getContext()).getUserID())};
                PutData putData = new PutData("https://meme-dating.one.pl/getLatestMeme.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        try {
                            JSONObject obj = new JSONObject(putData.getResult());
                            Log.d("first meme", putData.getData());
                            meme[0] = new Meme(
                                    obj.getInt("m_id"),
                                    obj.getString("url"),
                                    obj.getString("cat_title"),
                                    obj.getString("title"),
                                    LocalDateTime.parse(obj.getString("add_date"), formatter),
                                    obj.getInt("u_id"),
                                    obj.getString("username"),
                                    obj.getInt("likes"),
                                    obj.getInt("dislikes"),
                                    obj.getInt("reaction"));
                            latestMemeId = obj.getInt("m_id");
                            memesArrayList.remove(0);
                            recylerViewAdapter.notifyItemRemoved(0);
                            memesArrayList.add(meme[0]);
                            recylerViewAdapter.notifyItemInserted(memesArrayList.size() - 1);
                        } catch (Throwable t) {
                            Log.e("first meme", "Could not parse malformed JSON: \"" + putData.getResult() + "\"");
                        }
                    }
                }
            }
        });
        newMemes(4);
        isLoading = false;
    }
    public void loadMore() {

        if(memesArrayList.size()>=memesInThatCategory){return;}

        memesArrayList.add(null);
        recylerViewAdapter.notifyItemInserted(memesArrayList.size() - 1);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                newMemes(3);
                isLoading = false;
            }
        });
    }
    public void initAdapter() {
        recylerViewAdapter = new RecylerViewAdapter(memesArrayList, getActivity());
        recyclerView.setAdapter(recylerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
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
                     if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == memesArrayList.size() - 1 && memesArrayList.get(0)!=null) {
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