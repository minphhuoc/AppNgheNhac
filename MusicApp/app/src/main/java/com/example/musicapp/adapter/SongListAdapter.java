package com.example.musicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.MyExoplayer;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.databinding.SongListRecyclerRow2Binding;
import com.example.musicapp.model.SongModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {
    private List<String> songIdList;


    public SongListAdapter(List<String> songIdList) {
        this.songIdList = songIdList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private SongListRecyclerRow2Binding binding;

        public MyViewHolder(SongListRecyclerRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(String songId) {
            binding.songTitleTextView2.setText(songId);
            FirebaseFirestore.getInstance().collection("songs2")
                    .document(songId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            SongModel song = documentSnapshot.toObject(SongModel.class);
                            if (song != null) {
                                binding.songTitleTextView2.setText(song.getTitle2());
                                Glide.with(binding.songCoverImageView2)
                                        .load(song.getCoverUrl2())
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                                        .into(binding.songCoverImageView2);
                                binding.getRoot().setOnClickListener(view -> {
                                    Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                                    intent.putStringArrayListExtra("songIdList", new ArrayList<>(songIdList));
                                    intent.putExtra("currentSongIndex", getAdapterPosition());
                                    view.getContext().startActivity(intent);
                                });
                            }
                        }
                    });
        }
    }

    @NonNull
    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SongListRecyclerRow2Binding binding = SongListRecyclerRow2Binding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.MyViewHolder holder, int position) {
        String songId = songIdList.get(position);
        holder.bindData(songId);
    }

    @Override
    public int getItemCount() {
        return songIdList.size();
    }
}
