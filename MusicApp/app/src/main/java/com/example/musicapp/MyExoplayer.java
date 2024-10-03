package com.example.musicapp;

import android.content.Context;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.musicapp.model.SongModel;

public class MyExoplayer {


    private static ExoPlayer exoPlayer;
    private static SongModel currentSong;

    public static SongModel getCurrentSong() {
        return currentSong;
    }
    public static void setCurrentSong() {
        exoPlayer.pause();
        currentSong = null;
    }
    public static ExoPlayer getInstance(Context context) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
        }
        return exoPlayer;
    }
    public static void startPlaying(Context context, SongModel song) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
        }

        if (!song.equals(currentSong)) {
            // It's a new song, so start playing
            currentSong = song;

            if (currentSong.getUrl2() != null) {
                MediaItem mediaItem = MediaItem.fromUri(currentSong.getUrl2());
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                exoPlayer.play();

            }
        }
    }
    public static long getCurrentPosition() {
        if (exoPlayer != null) {
            return exoPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

}
