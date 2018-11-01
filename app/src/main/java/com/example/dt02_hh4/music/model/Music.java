package com.example.dt02_hh4.music.model;

import java.io.Serializable;

public class Music implements Serializable {
    private String name;
    private String image;
    private String singer;
    private String path;

    public Music(String name, String image, String singer, String path) {
        this.name = name;
        this.image = image;
        this.singer = singer;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSinger() {
        return singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
