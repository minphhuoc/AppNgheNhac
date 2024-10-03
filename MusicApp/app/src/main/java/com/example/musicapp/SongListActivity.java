package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.adapter.SongListAdapter;
import com.example.musicapp.databinding.ActivitySongListBinding;
import com.example.musicapp.model.CategoryModel2;
import com.example.musicapp.model.CategoryModel3;
import com.example.musicapp.model.CategoryModel4;
import com.example.musicapp.model.SongModel;

import java.util.List;

public class SongListActivity extends AppCompatActivity {
    private ActivitySongListBinding binding;
    private SongListAdapter songListAdapter;
    private View back; // Khai báo biến back ở đây
    private List<String> songIdList; // Thêm biến thành viên để lưu trữ danh sách ID bài hát

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        binding = ActivitySongListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CategoryModel2 category2 = (CategoryModel2) getIntent().getSerializableExtra("CATEGORY2_DATA");
        CategoryModel3 category3 = (CategoryModel3) getIntent().getSerializableExtra("CATEGORY3_DATA");
        CategoryModel4 category4 = (CategoryModel4) getIntent().getSerializableExtra("CATEGORY4_DATA");

        if (category2 != null) {
            binding.nameTextView2.setText(category2.getNametitle2());
            binding.nameTitle2.setText(category2.getName2());
            Glide.with(binding.coverImgView2)
                    .load(category2.getCoverUrl2())
                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                    .into(binding.coverImgView2);
            songIdList = category2.getSongs2(); // Lưu trữ danh sách ID bài hát
            setUpSongListRecyclerView(songIdList);
        } else if (category3 != null) {
            binding.nameTextView2.setText(category3.getName2());
            binding.nameTitle2.setText(category3.getNametitle2());
            Glide.with(binding.coverImgView2)
                    .load(category3.getCoverUrl2())
                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                    .into(binding.coverImgView2);
            songIdList = category3.getSongs2(); // Lưu trữ danh sách ID bài hát
            setUpSongListRecyclerView(songIdList);
        }else if (category4 != null) {
            binding.nameTextView2.setText(category4.getName4());
            binding.nameTitle2.setText(category4.getTitle4());
            Glide.with(binding.coverImgView2)
                    .load(category4.getCoverUrl4())
                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                    .into(binding.coverImgView2);
            songIdList = category4.getSongs2(); // Lưu trữ danh sách ID bài hát
            setUpSongListRecyclerView(songIdList);
        }

        addControl();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void addControl() {
        back = findViewById(R.id.back);
    }

    private void setUpSongListRecyclerView(List<String> songIdList) {
        songListAdapter = new SongListAdapter(songIdList);
        binding.songListRecyclerView2.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
        binding.songListRecyclerView2.setAdapter(songListAdapter);
    }
}
