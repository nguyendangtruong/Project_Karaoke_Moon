package com.project_karaoke.ui.people;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project_karaoke.R;
import com.project_karaoke.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {

    private ListView listView1;
    private ArrayList<PeopleViewModel> arrayList;
    private AdapterPeople adapterPeople;

    String img1 = "https://scr.vn/wp-content/uploads/2020/08/%E1%BA%A2nh-g%C3%A1i-d%E1%BB%85-th%C6%B0%C6%A1ng-l%C3%A0m-h%C3%ACnh-%C4%91%E1%BA%A1i-di%E1%BB%87n-xinh-x%E1%BA%AFn.jpg";
    String img2 = "https://st.gamevui.com/images/image/2020/09/16/lmht-among-us-hd01.jpg";
    String img3 = "https://i.pinimg.com/236x/6e/af/1a/6eaf1a844ae4b6fa6eeb6ff17f468cc0.jpg";


    private PeopleViewModel mViewModel;
    TabHost tabHost;
    private FragmentHomeBinding binding;
    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);
        tabHost = v.findViewById(R.id.tabHostPeopleActivity);
        tabHost.setup();
        //set tab host
        createTab();
        //set text size và màu cho tabhost khi click
        setTextandColorTabHost();

        listView1 = v.findViewById(R.id.lvView);
        arrayList = new ArrayList<>();

        arrayList.add(new PeopleViewModel(img1,"Nguyễn Anh","11-10-2021","Hãy đên nghe tôi hát","Hãy trao cho anh"));
        arrayList.add(new PeopleViewModel(img2,"Yassuo","05-6-2020","Hãy đên nghe tôi hát","Bỏ đi tất cả ta làm lại ván mới"));
        arrayList.add(new PeopleViewModel(img3,"Nguyễn Vi","5-6-2019","Hãy đên nghe tôi hát","Yêu Là Cưới - Phát Hồ"));

        adapterPeople = new AdapterPeople(requireActivity(), R.layout.view_layout_dang_bai_hat, arrayList);
        listView1.setAdapter(adapterPeople);


        listView1.setEnabled(false);
        return v;


    }

    private void setTextandColorTabHost() {
        Typeface tf = Typeface.create("@font/roboto_bold",Typeface.BOLD); //tạo font chữ để gán cho tabSpec

        final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        //set tabhost khi bắt đầu load
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#FFDFDFDF"));
        final View tabViewfirst = tw.getChildTabViewAt(0); //0 là vị trí tabhost
        final TextView tv1 = (TextView)tabViewfirst.findViewById(android.R.id.title);
        tv1.setTypeface(tf);//set font chữ cho text tabSpec
        tv1.setTextSize(14);//set cỡ chữ cho text tabSpec

        //set tabhost khi click
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    //set màu background
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")); // unselected
                    // set font-size
                    final View tabView = tw.getChildTabViewAt(i);
                    final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
                    tv.setTextSize(13);
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFDFDFDF")); // selected
                final View tabView = tw.getChildTabViewAt(tabHost.getCurrentTab());
                final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
                tv.setTextSize(14);
            }
        });
    }

    private void createTab() {
        TabHost.TabSpec tabSpec1;
        tabSpec1 = tabHost.newTabSpec("tab1");
        tabSpec1.setContent(R.id.tab1_people);
        tabSpec1.setIndicator("Phổ biến");
        tabHost.addTab(tabSpec1);
        //tạo tab 2
        TabHost.TabSpec tabSpec2;
        tabSpec2 = tabHost.newTabSpec("tab2");
        tabSpec2.setContent(R.id.tab2_people);
        tabSpec2.setIndicator("Mới nhất");
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3;
        tabSpec3 = tabHost.newTabSpec("tab3");
        tabSpec3.setContent(R.id.tab3_people);
        tabSpec3.setIndicator("Bạn bè");
        tabHost.addTab(tabSpec3);
    }

    private void addControls() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(PeopleViewModel.class);
        // TODO: Use the ViewModel
    }

}