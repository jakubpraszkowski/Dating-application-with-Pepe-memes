package com.example.meme_dating;

import java.util.Date;

public class Meme {
    public int m_id;
    public String url;
    public String cat_name;
    public String title;
    public Date uploadDate;
    public int u_id;
    public String u_name;

    public Meme(int m_id, String url, String cat_name, String title, Date uploadDate, int u_id, String u_name){
        this.m_id = m_id;
        this.url = url;
        this.cat_name = cat_name;
        this.title = title;
        this.uploadDate =uploadDate;
        this.u_id = u_id;
        this.u_name = u_name;
    }
    @Override
    public String toString() {
        return title;
    }
    public String m_idTostring(){ return String.valueOf(m_id); }
}
