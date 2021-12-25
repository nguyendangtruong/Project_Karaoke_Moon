package com.project_karaoke.ui.people;

import androidx.lifecycle.ViewModel;

public class PeopleViewModel {
    // TODO: Implement the ViewModel
    private String img;
    private String tenNguoiDang;
    private String thoiGianDang;
    private String caption;
    private String baiHat;

    public  PeopleViewModel(){

    }

    public PeopleViewModel(String img, String tenNguoiDang, String thoiGianDang, String caption, String baiHat) {
        this.img = img;
        this.tenNguoiDang = tenNguoiDang;
        this.thoiGianDang = thoiGianDang;
        this.caption = caption;
        this.baiHat = baiHat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTenNguoiDang() {
        return tenNguoiDang;
    }

    public void setTenNguoiDang(String tenNguoiDang) {
        this.tenNguoiDang = tenNguoiDang;
    }

    public String getThoiGianDang() {
        return thoiGianDang;
    }

    public void setThoiGianDang(String thoiGianDang) {
        this.thoiGianDang = thoiGianDang;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getBaiHat() {
        return baiHat;
    }

    public void setBaiHat(String baiHat) {
        this.baiHat = baiHat;
    }
}