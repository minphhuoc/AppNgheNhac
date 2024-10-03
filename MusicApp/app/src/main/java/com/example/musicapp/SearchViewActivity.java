package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.musicapp.adapter.SongListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchViewActivity extends AppCompatActivity {
    SearchView search;
    Button backHome;
    RecyclerView recyclerView;
    SongListAdapter songListAdapter;
    List<String> songIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        addControls();
        setUpRecyclerView();

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchViewActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSongs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSongs(newText);
                return false;
            }
        });
    }

    private void addControls() {
        search=findViewById(R.id.searchView);
        backHome=findViewById(R.id.backHome);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpRecyclerView() {
        songListAdapter = new SongListAdapter(songIdList);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchViewActivity.this));
        recyclerView.setAdapter(songListAdapter);
    }

    private void searchSongs(String query) {
        // Chuẩn hóa truy vấn: Chuyển tất cả về chữ thường và loại bỏ dấu
        String normalizedQuery = removeVietnameseAccent(query.toLowerCase());

        FirebaseFirestore.getInstance().collection("songs2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            songIdList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Chuẩn hóa dữ liệu từ Firestore: Chuyển tất cả về chữ thường và loại bỏ dấu
                                String normalizedTitle = removeVietnameseAccent(document.getString("title2").toLowerCase());

                                if (normalizedTitle.contains(normalizedQuery)) {
                                    songIdList.add(document.getId());
                                }
                            }
                            songListAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("SearchViewActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Hàm chuẩn hóa dấu tiếng Việt
    private String removeVietnameseAccent(String str) {
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[àáảãạăắằẵặâấầẩẫậ]", "a");
        str = str.replaceAll("[èéẻẽẹêềếểễệ]", "e");
        str = str.replaceAll("[ìíỉĩị]", "i");
        str = str.replaceAll("[òóỏõọôồốổỗộơờớởỡợ]", "o");
        str = str.replaceAll("[ùúủũụưừứửữự]", "u");
        str = str.replaceAll("[ỳýỷỹỵ]", "y");
        return str;
    }

}

