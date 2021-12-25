package com.project_karaoke.ui.home;

public class HomeViewModel  {

//    private MutableLiveData<String> mText;
//
//    public HomeViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
    private String id;
    private String baihat;
    private String casi;
    private String img;


    public  HomeViewModel(){

    }

    public HomeViewModel(String id, String baihat, String casi, String img) {
        this.id = id;
        this.baihat = baihat;
        this.casi = casi;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return baihat + casi +img;
    }
}