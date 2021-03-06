package com.project_karaoke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.project_karaoke.databinding.ActivityBottomNavigationBinding;
import com.project_karaoke.ui.profile.ProfileFragment;

public class BottomNavigation extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    String nameLoginFB;
    String imgUrlFB;

    private ActivityBottomNavigationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_people, R.id.navigation_mykaroke, R.id.navigation_upnhac, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               //Toast.makeText(BottomNavigation.this,""+ destination.getId() ,Toast.LENGTH_LONG).show();
                if(destination.getLabel() == "@string/title_profile")
                {
                    //khi click v??o nav c?? id l?? navigation_profile s??? kh???i t???o v?? setup Profiles

                    senDataProfileToFragment();
                }
            }
        });




    }




    private void senDataProfileToFragment() {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_bottom_navigation, new ProfileFragment());
            fragmentTransaction.commit();


    }

    public String getNameLoginFB() {
        //nh???n gi?? tr??? t??? putExtra FacbookAuthActivity v?? return
        //sau ???? h??m n??y s??? ??c g???i ??? ProjectFragment ????? hi???n th??? ra textView
        return nameLoginFB = getIntent().getStringExtra("nameFB");
    }

    public String getImgUrlFB() {
        //nh???n gi?? tr??? t??? putExtra FacbookAuthActivity v?? return
        //sau ???? h??m n??y s??? ??c g???i ??? ProjectFragment ????? hi???n th??? ra textView
        return imgUrlFB= getIntent().getStringExtra("urlFB");
    }


    //    public void OpenFragment(){
//        Fragment profileFragment = new Fragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        Bundle data = new Bundle();
//        data.putString("myData", "Hello");
//
//        profileFragment.setArguments(data);
//        fragmentTransaction.replace(R.id.nav_host_fragment_activity_bottom_navigation, profileFragment).commit();
//
//    }


}