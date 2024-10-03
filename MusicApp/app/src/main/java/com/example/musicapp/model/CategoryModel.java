package com.example.musicapp.model;

import java.util.List;

public class CategoryModel {
    private String name;
    private String coverUrl;
    private List<String> songs;

    public CategoryModel() {
    }

    public CategoryModel(String name, String coverUrl, List<String> songs) {
        this.name = name;
        this.coverUrl = coverUrl;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }


    public String getCoverUrl() {
        return coverUrl;
    }

    public List<String> getSongs() {
        return songs;
    }
}
