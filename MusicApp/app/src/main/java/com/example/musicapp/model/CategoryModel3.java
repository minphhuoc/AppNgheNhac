package com.example.musicapp.model;


import java.io.Serializable;
import java.util.List;

public class CategoryModel3 implements Serializable {
    private String coverUrl3;
    private List<String> songs2;
    private String nametitle2;
    private String name2;
    private String coverUrl2;

    public CategoryModel3() {
    }

    public CategoryModel3(String coverUrl3, List<String> songs,String name2, String nametitle2) {
        this.name2 = name2;
        this.coverUrl3=coverUrl3;
        this.songs2 = songs2;
        this.nametitle2=nametitle2;
    }

    public List<String> getSongs2() { // Add this method
        return songs2;
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
    public String getCoverUrl3() {
        return coverUrl3;
    }


}
