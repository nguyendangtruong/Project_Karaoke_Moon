package com.project_karaoke.ui.upnhac;

import androidx.lifecycle.ViewModel;

public class UpNhacViewModel  {
    private String baihat;
    private String casi;
    private String id;
    private String img;

    public  UpNhacViewModel(){

    }

    public UpNhacViewModel(String baihat, String casi, String id, String img) {
        this.baihat = baihat;
        this.casi = casi;
        this.id = id;
        this.img = img;
    }

    public String getBaihat() {
        return baihat;
    }

    public void setBaihat(String baihat) {
        this.baihat = baihat;
    }

    public String getCasi() {
        return casi;
    }

    public void setCasi(String casi) {
        this.casi = casi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}