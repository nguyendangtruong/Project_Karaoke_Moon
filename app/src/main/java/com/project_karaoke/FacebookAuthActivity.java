package com.project_karaoke;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project_karaoke.ui.home.HomeFragment;
import com.project_karaoke.ui.profile.ProfileFragment;
import com.squareup.picasso.Picasso;


import java.util.Arrays;


public class FacebookAuthActivity extends LoginActivity{

    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    String urlFB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addControls();
        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        //loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        urlFB = loginResult.getAccessToken().getToken();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void checkLogin(){
        //Toast.makeText(FacebookAuthActivity.this, "ssss"+FirebaseAuth.getInstance().getCurrentUser(), Toast.LENGTH_LONG).show();
    }



    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());



        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(FacebookAuthActivity.this, "Authentication failed." +task.getException(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }


                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, BottomNavigation.class);
        if(user!=null)
        {
            //truyền tên FB user qua Intent Bottomnavigation
            intent.putExtra("nameFB", user.getDisplayName());

            //truyền url img qua Intent Bottomnavigation
            if(user.getPhotoUrl() !=null){
                intent.putExtra("urlFB", user.getPhotoUrl() + "/picture?height=500&access_token=" +urlFB);

            }
        }
        else {
            intent.putExtra("urlFB", "Trường");
            intent.putExtra("urlFB", R.drawable.avatar_demo_vit);
        }
        startActivity(intent);


//        OpenFragment();
      //Lấy tên người dùng để hiển thị
            //txtTenUser.setText(user.getDisplayName());
            // lấy avatar facebook để hiển thị lại tren ImageView
//            if(user.getPhotoUrl() !=null){
//                String urlAvatar;
//                urlAvatar = user.getPhotoUrl().toString();
//                urlAvatar = urlAvatar + "?type=large";
//                Picasso.get().load(urlAvatar).into(imgAvatar);
//            }





    }

    private void addControls(){

    }

//    public void OpenFragment(){
//        ProfileFragment profileFragment = new ProfileFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        Bundle data = new Bundle();
//        data.putString("myData", "Hello");
//
//        profileFragment.setArguments(data);
//        fragmentTransaction.replace(R.id.nav_host_fragment_activity_bottom_navigation, profileFragment).commit();
//
//    }



}