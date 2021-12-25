package com.project_karaoke.ui.mykaraoke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project_karaoke.R;

public class MyKaraokeFragment extends Fragment {

    private MyKaraokeViewModel mViewModel;

    View v;

    public static MyKaraokeFragment newInstance() {
        return new MyKaraokeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_my_karaoke, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyKaraokeViewModel.class);
        // TODO: Use the ViewModel
    }



}