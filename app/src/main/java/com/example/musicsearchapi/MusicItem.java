package com.example.musicsearchapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * music list item
 */
public class MusicItem implements Serializable {
    /**
     * music title
     */
    public String title;
    /**
     * music album
     */
    public Album album;
    /**
     * duration
     */
    public int duration;
    /**
     * contributors
     */
    public List<Contributor> contributors;

    public static class Album implements Serializable{
        public String cover;
        public String title;
    }

    public static class Contributor implements Serializable{
        public String name;
        @SerializedName("picture_big")
        public String picture;
    }
}
