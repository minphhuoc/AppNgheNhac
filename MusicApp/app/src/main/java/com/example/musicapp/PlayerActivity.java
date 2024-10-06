package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media3.common.Player;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musicapp.databinding.ActivityPlayerBinding;
import com.example.musicapp.model.CategoryModel4;
import com.example.musicapp.model.PlaylistModel;
import com.example.musicapp.model.SongModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    private List<String> songIdList;
    private int currentSongIndex;
    private ActivityPlayerBinding binding;
    AudioManager audioManager;
    MediaPlayer Musicplayer;
    ExoPlayer exoPlayer;
    ImageView iconprevious, iconnext,back;
     TextView timestart,timeend;
     TextView song_title_text_view;
    SeekBar seekBar;
    Drawable iconPlay,iconPause;
    @SuppressLint("UseCompatLoadingForDrawables")
    @OptIn(markerClass = UnstableApi.class) @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        songIdList = getIntent().getStringArrayListExtra("songIdList");
        currentSongIndex = getIntent().getIntExtra("currentSongIndex", 0);
        startPlayingCurrentSong();
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        iconPlay = getResources().getDrawable(R.drawable.iconplay);
        iconPause = getResources().getDrawable(R.drawable.iconpause);
        SongModel currentSong = MyExoplayer.getCurrentSong();
        binding.iconsadd.setOnClickListener(v -> showAddToPlaylistDialog());
        addControl();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //audio//
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setMax(maxVolume);
        TextView song_title_text_view = (TextView) findViewById(R.id.song_title_text_view);
        song_title_text_view.setSelected(true);
        seekBar2.setProgress(currentVolume);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(audioManager.STREAM_MUSIC , progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // het audio//

        String duration =millisecondsToString((int) exoPlayer.getDuration());

        seekBar.setMax((int)(exoPlayer.getDuration() / 1)); // <--


        // Tạo một Runnable để cập nhật thanh trượt thời gian và hiển thị thời gian

        //tvDuration va tvTime
        final Handler  handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(exoPlayer != null){
                    long mCurrentPosition = exoPlayer.getCurrentPosition();
                    seekBar.setProgress((int) (mCurrentPosition / 1));
                    timestart.setText(millisecondsToString((int) mCurrentPosition));
                    handler.postDelayed(this, 1);
                    String duration = millisecondsToString((int) exoPlayer.getDuration());
                    timeend.setText(duration);
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        final boolean[] isUserSeeking = {false};
        seekBar.setProgress((int) exoPlayer.getCurrentPosition());
        seekBar.setMax((int) exoPlayer.getDuration());


        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY && exoPlayer.getPlayWhenReady() && !isUserSeeking[0]) {
                    seekBar.setMax((int) exoPlayer.getDuration());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isUserSeeking[0]) {
                                seekBar.setProgress((int) exoPlayer.getCurrentPosition());
                            }
                            handler.postDelayed(this, 1000);
                        }
                    }, 0);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    exoPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    // exoPlayer is ready, now we can get the duration
                    long duration = exoPlayer.getDuration();
                    Log.d("ExoPlayer Duration", "Duration: " + duration);
                }
            }
        });
    }

    private void showAddToPlaylistDialog() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào playlist", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseFirestore.getInstance().collection("playlists")
                .whereEqualTo("createdBy", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PlaylistModel> playlists = queryDocumentSnapshots.toObjects(PlaylistModel.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    String[] options = new String[playlists.size() + 1];
                    for (int i = 0; i < playlists.size(); i++) {
                        options[i] = playlists.get(i).getName();
                    }
                    options[playlists.size()] = "Tạo playlist mới";

                    builder.setTitle("Thêm vào playlist")
                            .setItems(options, (dialog, which) -> {
                                if (which == playlists.size()) {
                                    showCreatePlaylistDialog();
                                } else {
                                    PlaylistModel selectedPlaylist = playlists.get(which);
                                    addSongToPlaylist(selectedPlaylist.getPlaylistId());
                                }
                            });
                    builder.create().show();
                });
    }
    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setHint("Nhập tên playlist");

        builder.setTitle("Tạo playlist mới")
                .setView(input)
                .setPositiveButton("Tạo", (dialog, which) -> {
                    String playlistName = input.getText().toString().trim();
                    if (!playlistName.isEmpty()) {
                        createPlaylist(playlistName);
                    }
                })
                .setNegativeButton("Hủy", null);
        builder.create().show();
    }
    @OptIn(markerClass = UnstableApi.class) private void createPlaylist(String name) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        PlaylistModel newPlaylist = new PlaylistModel();
        newPlaylist.setName(name);
        newPlaylist.setCreatedBy(userId);

        FirebaseFirestore.getInstance().collection("playlists")
                .add(newPlaylist)
                .addOnSuccessListener(documentReference -> {
                    String playlistId = documentReference.getId();
                    newPlaylist.setPlaylistId(playlistId);

                    // Cập nhật lại playlistId trong Firestore
                    documentReference.update("playlistId", playlistId)
                            .addOnSuccessListener(aVoid -> {
                                // Thêm bài hát vào playlist
                                addSongToPlaylist(playlistId);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("PlayerActivity", "Error updating playlistId", e);
                            });
                });
    }

    @OptIn(markerClass = UnstableApi.class)
    private void addSongToPlaylist(String playlistId) {
        if (playlistId == null || playlistId.isEmpty()) {
            Log.e("PlayerActivity", "playlistId null or empty");
            return;
        }

        SongModel currentSong = MyExoplayer.getCurrentSong();
        if (currentSong == null) {
            Log.e("PlayerActivity", "currentSong is null");
            return;
        }

        String songId = currentSong.getId2();
        if (songId == null || songId.isEmpty()) {
            Log.e("PlayerActivity", "songId is null or empty");
            return;
        }

        FirebaseFirestore.getInstance().collection("playlists")
                .document(playlistId)
                .update("songIds", FieldValue.arrayUnion(songId))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã thêm bài hát vào playlist", Toast.LENGTH_SHORT).show();
                    Log.d("PlayerActivity", "Song added successfully: " + songId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thể thêm bài hát", Toast.LENGTH_SHORT).show();
                    Log.e("PlayerActivity", "Error adding song", e);
                });
    }
    public String millisecondsToString(int time){
        String elapsedTime ="";
        int minutes= time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds <10){
            elapsedTime +="0";
        }
        elapsedTime += seconds;
        return elapsedTime;
    }

    private void startPlayingCurrentSong() {
        if(songIdList!=null && currentSongIndex>=0&&currentSongIndex<songIdList.size()){
            FirebaseFirestore.getInstance().collection("songs2")
                    .document(songIdList.get(currentSongIndex)).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            SongModel song = documentSnapshot.toObject(SongModel.class);
                            if (song != null) {
                                binding.songTitleTextView.setText(song.getTitle2());
                                binding.songSubtitleTextView.setText(song.getSubtitle2());
                                Glide.with(binding.songCoverImgView2)
                                        .asBitmap()
                                        .load(song.getCoverUrl2())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                binding.songCoverImgView2.setImageBitmap(resource);
                                                updateBackgroundColor(resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });

                                MyExoplayer.startPlaying(PlayerActivity.this, song);
                            }
                        }
                    });
        }
    }

    private void updateBackgroundColor(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int defaultColor=0x000000;
                int dominantColor= palette.getDominantColor(defaultColor);
                float[] hsv= new float[3];
                Color.colorToHSV(dominantColor,hsv);
                hsv[2]*=0.1;
                int darkerDominantColor=Color.HSVToColor(hsv);
                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{dominantColor,darkerDominantColor});
                binding.getRoot().setBackground(gradientDrawable);
            }
        });
    }



    private void addControl() {
       back=findViewById(R.id.btnback);
        timestart=findViewById(R.id.timestart);
        timeend=findViewById(R.id.timeend);
        iconnext=findViewById(R.id.iconnext);
        iconprevious=findViewById(R.id.iconprevious);
        seekBar=findViewById(R.id.seekBar);
       ImageView iconpause =findViewById(R.id.iconpause);
        // Update the seek bar as music plays
        exoPlayer = MyExoplayer.getInstance(this);
        final boolean[] isUserSeeking = {false};
        final Handler handler = new Handler();

// Update the seek bar as music plays

// Rewind 10 seconds


// Play or pause the music
        iconpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                    iconpause.setImageDrawable(iconPlay);
                    ScaleAnimation anim = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(300); // Duration of the animation in milliseconds

                    // Start the animation
                    binding.songCoverImgView2.startAnimation(anim);
                } else {
                    exoPlayer.play();
                    iconpause.setImageDrawable(iconPause); // Change back to 'iconPause' when music is playing

                    // Create an animation to zoom in the image
                    ScaleAnimation anim = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(300); // Duration of the animation in milliseconds

                    // Start the animation
                    binding.songCoverImgView2.startAnimation(anim);
                }
            }
        });


// Fast forward 10 seconds
        iconnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex < songIdList.size()-1 ) {
                    currentSongIndex++;
                    startPlayingCurrentSong();
                }
            }
        });
        iconprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex > 0) {
                    currentSongIndex--;
                    startPlayingCurrentSong();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Cập nhật SongModel hiện tại
        SongModel currentSong = MyExoplayer.getCurrentSong();
        if (currentSong != null) {
            binding.songTitleTextView.setText(currentSong.getTitle2());
            binding.songSubtitleTextView.setText(currentSong.getSubtitle2());
            Glide.with(binding.songCoverImgView2)
                    .asBitmap()
                    .load(currentSong.getCoverUrl2())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.songCoverImgView2.setImageBitmap(resource);
                            updateBackgroundColor(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            iconnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentSongIndex < songIdList.size()-1 ) {
                        currentSongIndex++;
                        startPlayingCurrentSong();
                    }
                }
            });
            iconprevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentSongIndex > 0) {
                        currentSongIndex--;
                        startPlayingCurrentSong();
                    }
                }
            });
        }

        }
    }



