package com.example.music;

import java.io.Serializable;
import java.util.List;

public class MusicItem implements Serializable {
    public String id;
    public String name;
    public List<String> singer;
    public String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSinger() {
        return singer;
    }

    public void setSinger(List<String> singer) {
        this.singer = singer;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "MusicItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", singer=" + singer +
                ", img='" + img + '\'' +
                '}';
    }
}
