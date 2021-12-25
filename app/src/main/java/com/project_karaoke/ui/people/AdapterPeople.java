package com.project_karaoke.ui.people;

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

public class AdapterPeople extends ArrayAdapter<PeopleViewModel> {
    Context context;
    int resource;
    @NonNull List<PeopleViewModel> objects;
    public AdapterPeople(@NonNull Context context, int resource, @NonNull List<PeopleViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


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


        PeopleViewModel peopleViewModel =this.objects.get(position);
        Picasso.get().load(peopleViewModel.getImg()).into(_img); // hiển thị ảnh bằng Picasso
        _ten.setText(peopleViewModel.getTenNguoiDang());
        _thoigian.setText(peopleViewModel.getThoiGianDang());
        _caption.setText(peopleViewModel.getCaption());
        _bh.setText(peopleViewModel.getBaiHat());

        
        return row;
    }




}
