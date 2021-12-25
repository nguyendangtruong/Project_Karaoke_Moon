package com.project_karaoke;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button button;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://karaoke-app-9f87c.appspot.com");
    StorageReference imagesRef = storageRef.child("0001.jpg");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try{
//                    mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/karaoke-app-9f87c.appspot.com/o/SoucreKaraoke%2FFileMp3%2Fnhacdemo%20(online-audio-converter.com).mp3?alt=media&token=58a61c19-cb7b-4ddc-9af9-ba6eff246324");
//                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//
//                        }
//                    });
//                    mediaPlayer.prepare();
//                }catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }

                final File localFile = new File(rootPath,"imageName.txt");

                imagesRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(MainActivity.this, "sssssss", Toast.LENGTH_SHORT).show();
                                // Successfully downloaded data to local file
                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...
                    }
                });

            }
        });


        File path = new File(Environment.getExternalStorageDirectory(), "/MoonKara/");

        ArrayList<String> myList = new ArrayList<>();
        //hàm đọc tên file trong bộ nhớ
        File list[] = path.listFiles();
        for( int i=0; i< list.length; i++)  //lấy danh sách các file
        {
            for (String w : list[i].getName().split("\\.")) {  // tách đuôi để lấy được tên file
                if(!w.equals("lrc"))
                {
                    Toast.makeText(MainActivity.this, ""+w, Toast.LENGTH_SHORT).show();
                    myList.add( w );
                }
                if(!w.equals("mp3"))
                {
                    Toast.makeText(MainActivity.this, ""+w, Toast.LENGTH_SHORT).show();
                    myList.add( w );
                }
            }

        }




    }


}