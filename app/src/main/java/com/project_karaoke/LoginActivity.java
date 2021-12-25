package com.project_karaoke;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    ImageButton btnLoginFacebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();


        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);
            startActivity(intent);
        });
        // Write a message to the database
        addEvents();

        //kiểm tra cấp quyền tru cập bộ nhớ
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO }, 1);


        //Kiểm tra trạng thái đăng nhập và tự động chuyển sang màn hình chính khi đã từng login trước đó
        SaveTrangThaiDangNhap();





    }


    private void SaveTrangThaiDangNhap() {
        mAuth = FirebaseAuth.getInstance();
        //kiểm tra xem người dùng đã đăng nhập chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //nếu giá trị khác null thì người dùng đã Login
        //Tiến hành chuyển snag trang chủ và truyền tham số thông tin của người dùng
        if(currentUser!=null)
        {
            //lấy url Avatar đã lưu từ lần đăng nhập đầu
            SharedPreferences getDataSave = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            tokenuUrlFB = getDataSave.getString("tokenAvatar","");
            updateUI(currentUser);
        }



    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void addEvents() {
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent1 = new Intent(LoginActivity.this, FacebookAuthActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
//                startActivity(intent1);
                LoginFacebok();

            }
        });
    }

    private void addControls() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
    }


    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    String tokenuUrlFB;
    SharedPreferences sharedPreferences;
    //Loogin Fbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb

    private void LoginFacebok() {
        //mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                tokenuUrlFB = loginResult.getAccessToken().getToken();

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
                    Toast.makeText(LoginActivity.this, "Authentication failed." +task.getException(), Toast.LENGTH_SHORT).show();
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
                intent.putExtra("urlFB", user.getPhotoUrl().toString() + "?height=200&access_token=" + tokenuUrlFB);

                //Lưu url avatar người dùng vào cache
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString("tokenAvatar", tokenuUrlFB).apply(); //comit

            }
        }
        else {
            intent.putExtra("urlFB", "Guest");
            intent.putExtra("urlFB", R.drawable.avatar_demo_vit);
        }
        startActivity(intent);
        finish();





    }



}