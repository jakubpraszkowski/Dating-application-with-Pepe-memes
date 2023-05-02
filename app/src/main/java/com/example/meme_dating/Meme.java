package com.example.meme_dating;

import java.util.Date;

public class Meme {
    public int m_id;
    public String url;
    public int cat_id;
    public String title;
    public Date uploadDate;
    public int u_id;

    public Meme(int m_id, String url, int cat_id, String title, Date uploadDate, int u_id){
        this.m_id = m_id;
        this.url = url;
        this.cat_id = cat_id;
        this.title = title;
        this.uploadDate =uploadDate;
        this.u_id = u_id;
    }
    @Override
    public String toString() {
        return title;
    }
}
