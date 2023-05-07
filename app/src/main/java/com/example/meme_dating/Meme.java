package com.example.meme_dating;

import java.time.LocalDateTime;
import java.util.Date;

public class Meme {
    public int m_id;
    public String url;
    public String cat_name;
    public String title;
    public LocalDateTime uploadDate;
    public int u_id;
    public String u_name;
    public int likes;
    public int dislikes;
    public int reaction;

    public Meme(int m_id, String url, String cat_name, String title, LocalDateTime uploadDate, int u_id, String u_name, int likes, int dislikes, int reaction){
        this.m_id = m_id;
        this.url = url;
        this.cat_name = cat_name;
        this.title = title;
        this.uploadDate =uploadDate;
        this.u_id = u_id;
        this.u_name = u_name;
        this.likes = likes;
        this.dislikes = dislikes;
        this.reaction = reaction;
    }
}
