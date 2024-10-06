package com.example.musicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.SongListActivity;
import com.example.musicapp.databinding.CategoryItemsRecyclerRowPlaylistBinding;
import com.example.musicapp.model.PlaylistModel;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<PlaylistModel> playlists;

    public PlaylistAdapter(List<PlaylistModel> playlists) {
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemsRecyclerRowPlaylistBinding binding =
                CategoryItemsRecyclerRowPlaylistBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private CategoryItemsRecyclerRowPlaylistBinding binding;

        public PlaylistViewHolder(@NonNull CategoryItemsRecyclerRowPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlaylistModel playlist) {
            binding.txtPlaylistName.setText(playlist.getName());
            binding.txtSongCount.setText(playlist.getSongIds().size() + " bài hát");

            Glide.with(binding.getRoot())
                    .load(playlist.getCoverUrl())
                    .apply(new RequestOptions().transform(new RoundedCorners(8)))
                    .into(binding.coverImgViewPlaylist);

            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SongListActivity.class);
                intent.putExtra("PLAYLIST_DATA", playlist);
                v.getContext().startActivity(intent);
            });
        }
    }
}