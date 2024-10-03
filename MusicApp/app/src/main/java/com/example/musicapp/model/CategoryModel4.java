package com.example.musicapp.model;


import java.io.Serializable;
import java.util.List;

public class CategoryModel4 implements Serializable {
    private String coverUrl4;
    private List<String> songs2;
    private String title4;
    private String name4;
    private String coverUrl2;

    public CategoryModel4() {
    }

    public CategoryModel4(String coverUrl4, List<String> songs2,String name4, String title4) {
        this.name4 = name4;
        this.coverUrl4=coverUrl4;
        this.songs2 = songs2;
        this.title4=title4;
    }

    public List<String> getSongs2() { // Add this method
        return songs2;
    }

    public String getCoverUrl4() {
        return coverUrl4;
    }

    public String getTitle4() {
        return title4;
    }

    public String getName4() {
        return name4;
    }

    public String getCoverUrl2() {
        return coverUrl2;
    }
}
