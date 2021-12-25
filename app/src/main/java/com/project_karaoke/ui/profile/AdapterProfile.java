package com.project_karaoke.ui.profile;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project_karaoke.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterProfile extends ArrayAdapter<ProfileViewModel> {
    Context context;
    int resource;
    @NonNull List<ProfileViewModel> objects;
    public AdapterProfile(@NonNull Context context, int resource, @NonNull List<ProfileViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    //inal String[] pathfileid = new String[1];

    Boolean statusLike=false;

    @NonNull
    @Override
    public View getView(int position, @Nullable View row, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = layoutInflater.inflate(this.resource, null);
        ImageView _img =(ImageView) row.findViewById(R.id.imgAvatarNguoiDang);
        TextView _ten = (TextView) row.findViewById(R.id.txtTenNguoiDungBD);
        TextView _thoigian = (TextView) row.findViewById(R.id.txtThoiGianDang);
        TextView _caption = (TextView) row.findViewById(R.id.txtCaption);
        TextView _bh = (TextView) row.findViewById(R.id.txtTenBHkhiDang);
        ImageButton btnplay = (ImageButton) row.findViewById(R.id.ibtnPlayBaiDang);
        SeekBar seekBar = (SeekBar) row.findViewById(R.id.seekBarMediaPeple);
        TextView txtCurrentTime = (TextView) row.findViewById(R.id.txtTimeSongCurrent);
        TextView txtMaxTime = (TextView) row.findViewById(R.id.txtTimeSongTotal);
        ImageButton btnLike = (ImageButton) row.findViewById(R.id.iBtnLike);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!statusLike){
                    btnLike.setImageResource(R.drawable.icon_tt_love_on);
                    statusLike=true;
                }else {
                    btnLike.setImageResource(R.drawable.icon_tt_love);
                    statusLike=false;
                }
            }
        });


        ProfileViewModel profileViewModel =this.objects.get(position);
        Picasso.get().load(profileViewModel.getImg()).into(_img); // hiển thị ảnh bằng Picasso
        _ten.setText(profileViewModel.getTenNguoiDang());
        _thoigian.setText(profileViewModel.getThoiGianDang());
        _caption.setText(profileViewModel.getCaption());
        _bh.setText(profileViewModel.getBaiHat());



        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File path = new File(Environment.getExternalStorageDirectory(), "/MoonKara/BanGhi");
                //kiểm tra xem file đã được tạo trong bộ nhớ chưa
                if (!path.exists()) {
                    path.mkdirs();
                }

                ArrayList<String> myList = new ArrayList<>();
                //hàm đọc tên file trong bộ nhớ
                File list[] = path.listFiles();
                for (int i = 0; i < list.length; i++)  //lấy danh sách các file
                {
                    if (i == position) {
                        myList.add(list[i].getName());
                    }

                }
                MediaPlayer mediaPlayer;
                mediaPlayer = new MediaPlayer();
                String path1 = myList.get(0);
                Toast.makeText(context, ""+path1, Toast.LENGTH_SHORT).show();
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnplay.setImageResource(R.drawable.ic_play);
                }else {
                    mediaPlayer.start();
                    btnplay.setImageResource(R.drawable.ic_pause);
                }


            }
        });




        return row;
    }


}
