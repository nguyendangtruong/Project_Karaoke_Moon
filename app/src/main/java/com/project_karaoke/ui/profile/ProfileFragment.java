package com.project_karaoke.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project_karaoke.BottomNavigation;
import com.project_karaoke.LoginActivity;
import com.project_karaoke.R;
import com.project_karaoke.ui.profile.ProfileViewModel;
import com.project_karaoke.ui.profile.ProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    TabHost tabHost;

    private ListView lvBanGhi;
    private ArrayList<ProfileViewModel> arrayList;
    private AdapterProfile adapter;

    BottomNavigation mbottomNavigation;

    private TextView txtTenUser;
    private ImageView imgAvatarUser;
    private ImageButton btnLogOut;

    String urlImgAvatar ="", tenNguoiDung="";

    View v;
    private ProfileViewModel mViewModel;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        addControls();

//        Bundle data= getArguments();
//        if(data!=null){
//            reciveData = data.getString("myData");
//        }

        SetProfileUser();

        LoadCacBanGhi();
        return v;
    }

    ArrayList<String> listID = new ArrayList<>();
    ArrayList<String> listThoiGianGhi = new ArrayList<>();
    private void LoadCacBanGhi() {
        File path = new File(Environment.getExternalStorageDirectory(), "/MoonKara/BanGhi");
        //ki???m tra xem file ???? ???????c t???o trong b??? nh??? ch??a
        if(!path.exists()) {
            path.mkdirs();
        }

        //h??m ?????c t??n file trong b??? nh???
        File list[] = path.listFiles();
        for( int i=0; i< list.length; i++)  //l???y danh s??ch c??c t??n file c?? trong th?? m???c
        {
            //l???y 14 k?? t??? ?????u c???a file b???n ghi, g???m id(4 k?? t???) v?? th???i gian ghi(10 k?? t???)
            String id = list[i].getName().substring(0,4);
            listID.add(id);
            String timeGhi = list[i].getName().substring(4,16);
            listThoiGianGhi.add(XuLyHienThiThoigianGhi(timeGhi));// V?? khi t???o file ta ???? l???y ng??y gi??? ghim ??m ?????t t??n cho file n??n gi??? ch??ng ta x??? l?? t??n file ????? l???y ???????c th???i gian
        }
        AddModel();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<String> tenBH = new ArrayList<>();//t???o bi???n luu t??n B??i h??t t??? truy v???n firebase
    private void AddModel() {
        for (int i=0; i<listID.size();i++)
        {
            int convertID = Integer.parseInt(listID.get(i));
            String idBh = String.valueOf(convertID);
            DatabaseReference myRef = database.getReference("karaoke").child(idBh);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap: snapshot.getChildren())
                    {
                        if (snap.getKey().equals("baihat"))
                        {
                            tenBH.add(String.valueOf(snap.getValue()));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        tenBH.add("0");
        arrayList = new ArrayList<>();
        for (int i=0; i<listID.size();i++)
        {
            arrayList.add(new ProfileViewModel(urlImgAvatar,tenNguoiDung,listThoiGianGhi.get(i),"B??i h??t c???a t??i",tenBH.get(i)));
        }

        adapter = new AdapterProfile(requireActivity(), R.layout.view_layout_dang_bai_hat, arrayList);
        lvBanGhi.setAdapter(adapter);
    }

    private String XuLyHienThiThoigianGhi(String timeGhi) {

            String dinhDangHienThi;
            dinhDangHienThi = timeGhi.substring(0,2);
            dinhDangHienThi += "-";
            dinhDangHienThi += timeGhi.substring(2,4);
            dinhDangHienThi += "-";
            dinhDangHienThi += timeGhi.substring(4,8);
            dinhDangHienThi += "   ";
            dinhDangHienThi += timeGhi.substring(8,10);
            dinhDangHienThi += ":";
            dinhDangHienThi += timeGhi.substring(10,12);
            return dinhDangHienThi;

    }


    private void SetProfileUser() {
            mbottomNavigation = (BottomNavigation) getActivity();
            if (mbottomNavigation.getNameLoginFB()!=null){
                txtTenUser.setText(mbottomNavigation.getNameLoginFB());
                tenNguoiDung = mbottomNavigation.getNameLoginFB();//l??u gi?? tr??? t??n ????? d??ng cho hi???n th??? t??n l??n danh s??ch b???n ghi
                btnLogOut.setVisibility(View.VISIBLE);
            }else {
                txtTenUser.setText("Guest");
                tenNguoiDung = "Guest";
                btnLogOut.setVisibility(View.INVISIBLE);
            }

            if(mbottomNavigation.getImgUrlFB()!=null){
                Picasso.get().load(mbottomNavigation.getImgUrlFB()).into(imgAvatarUser);
                urlImgAvatar = mbottomNavigation.getImgUrlFB(); //l??u gi?? tr??? url avatar d??ng cho hi???n th??? avatar l??n danh s??ch b???n ghi
            }else {
                Picasso.get().load(R.drawable.avatar_demo_vit).into(imgAvatarUser);
                urlImgAvatar = String.valueOf(R.drawable.avatar_demo_vit);
            }

    }

    int statusLike = 1;
    private void addControls() {
        txtTenUser = v.findViewById(R.id.txtTennguoidung);
        imgAvatarUser = v.findViewById(R.id.imgAvatarNguoiDung);
        tabHost = v.findViewById(R.id.tabHostFragmentProfile);
        btnLogOut = v.findViewById(R.id.ibtnLogOut);
        lvBanGhi = v.findViewById(R.id.listViewBaiDangProfile);


        tabHost.setup();
        //set tab host
        createTab();
        setTextandColorTabHost();

        //logout
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                BottomNavigation b = new BottomNavigation();
                b.finish();
            }
        });

    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTab() {
        TabHost.TabSpec tabSpec1;
        tabSpec1 = tabHost.newTabSpec("tab1_profile");
        tabSpec1.setContent(R.id.tab1_profile);
        tabSpec1.setIndicator("B??i h??t");
        tabHost.addTab(tabSpec1);
        ((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(0).getLayoutParams()).weight = 3;//set weight ????? chia t??? l??? chi???m d???ng Spec

        //t???o tab 2
        TabHost.TabSpec tabSpec2;
        tabSpec2 = tabHost.newTabSpec("tab2_profile");
        tabSpec2.setContent(R.id.tab2_profile);
        tabSpec2.setIndicator("Nh???c c???a t??i");
        tabHost.addTab(tabSpec2);
        ((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(1).getLayoutParams()).weight = 3;//set weight ????? chia t??? l??? chi???m d???ng Spec


        TabHost.TabSpec tabSpec3;
        tabSpec3 = tabHost.newTabSpec("tab3_profile");
        tabSpec3.setContent(R.id.tab3_profile);
        tabSpec3.setIndicator("",getResources().getDrawable(R.drawable.ic_settings_24));
        tabHost.addTab(tabSpec3);
        //tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 100; //set chi???u r???ng c???a spec 3(spec ch???a icon)
        ((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(2).getLayoutParams()).weight = 1;//set weight ????? chia t??? l??? chi???m d???ng Spec

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setTextandColorTabHost() {

        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackground(getResources().getDrawable(R.drawable.boder_background_spec_tab_unselected));
        }


        Typeface tf = Typeface.create("@font/roboto_bold",Typeface.BOLD);

        final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        //set tabhost khi b???t ?????u load
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#F2F2F2"));
        final View tabViewfirst = tw.getChildTabViewAt(0); //0 l?? v??? tr?? tabhost
        final TextView tv1 = (TextView)tabViewfirst.findViewById(android.R.id.title);
        tv1.setTypeface(tf);
        tv1.setTextSize(14);

        //set tabhost khi click
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    //set m??u background
                    tabHost.getTabWidget().setBackground(getResources().getDrawable(R.drawable.boder_background_spec_tab_unselected)); // unselected
                    // set font-size
                    final View tabView = tw.getChildTabViewAt(i);
                    final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
                    tv.setTextSize(14);
                }

                tabHost.getTabWidget().setBackground(getResources().getDrawable(R.drawable.boder_background_spec_tab_selected)); // selected
                final View tabView = tw.getChildTabViewAt(tabHost.getCurrentTab());
                final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
                tv.setTextSize(14);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}