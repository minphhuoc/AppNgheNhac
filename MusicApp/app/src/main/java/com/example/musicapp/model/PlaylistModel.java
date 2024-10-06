package com.example.musicapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaylistModel implements Serializable {
    private String playlistId;
    private String name;
    private List<String> songIds;
    private String coverUrl;
    private String createdBy;

    public PlaylistModel() {
        songIds = new ArrayList<>();
    }

    // Getters and setters
    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getSongIds() { return songIds; }
    public void setSongIds(List<String> songIds) { this.songIds = songIds; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}