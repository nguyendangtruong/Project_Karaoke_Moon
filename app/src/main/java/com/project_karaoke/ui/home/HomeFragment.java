package com.project_karaoke.ui.home;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project_karaoke.R;
import com.project_karaoke.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView listView;
    RecyclerView recyclerView;
    ArrayList<HomeViewModel> arrayList;
    HomeAdapter homeAdapter;
    Button btnTimKiem;

    FirebaseFirestore db;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    SearchView searchViewBH;
    //HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ////homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        //ánh xạ
        addControls();

        loadBaiHatTuFirebase();
        SearchBaiHat();



        return view;
    }

    //hàm tìm kiếm https://www.geeksforgeeks.org/searchview-in-android-with-recyclerview/
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<HomeViewModel> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (HomeViewModel item : arrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getBaihat().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            homeAdapter.filterList(filteredlist);
        }
    }

    private void SearchBaiHat() {
        //hàm tìm kiếm bài hát trên SearcView
        searchViewBH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


    }

    private void loadBaiHatTuFirebase() {


        //tạo ds trống
        arrayList = new ArrayList<>();

        //Tham chiếu đến dữ liệu đến csdl Firebase
        DatabaseReference myRef = database.getReference("karaoke");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren())
                {
                    HomeViewModel homeViewModel = snap.getValue(HomeViewModel.class);
                    arrayList.add(homeViewModel);
                }

                //Toast.makeText(getActivity(),""+homeViewModel, Toast.LENGTH_LONG).show();

                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        homeAdapter = new HomeAdapter(requireActivity(), R.layout.view_ds_bai_hat_home, arrayList);
        listView.setAdapter(homeAdapter);
    }

    private void addControls() {
        listView = view.findViewById(R.id.ListViewDSBaiHatHome);
        searchViewBH = (SearchView) view.findViewById(R.id.SearchBaiHat);
        btnTimKiem = view.findViewById(R.id.btntimkiemBH);

//        ProgressBar simpleProgressBar=(ProgressBar)view.findViewById(R.id.progressBarTaiVe); // initiate the progress bar
        //simpleProgressBar.setBackgroundColor(R.color.teal_200); // black background color for the progress bar
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}