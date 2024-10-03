package com.example.musicapp.model;

import java.io.Serializable;
import java.util.List;

public class CategoryModel2 implements Serializable {
    private String name2;
    private String coverUrl2;
    private String nametitle2;
    private List<String> songs2;

    public CategoryModel2() {
    }

    public CategoryModel2 (String name, String coverUrl, List<String> songs2, String nametitle2) {
        this.name2 = name;
        this.coverUrl2 = coverUrl;
        this.songs2 = songs2;
        this.nametitle2=nametitle2;
    }
    public String getNametitle2() {
        return nametitle2;
    }
    public String getName2() {
        return name2;
    }

    public String getCoverUrl2() {
        return coverUrl2;
    }

    public List<String> getSongs2() {
        return songs2;
    }
}
