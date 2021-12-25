package com.project_karaoke;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class GiaoDienPlayingActivity extends AppCompatActivity {

    ImageButton btnplay;
    SeekBar seekBar;
    TextView txtMaxTime, txtCurrentTime;
    String path;

    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_playing);

        addControls();

        Bundle bundle = getIntent().getExtras();
        String duongDan = bundle.getString("DuongDanDenDanGhi").toString();
        //String duongDan = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoonKara/BanGhi/11_12_2021-10-32.mp3";
        path = duongDan;


        mediaPlayer= new MediaPlayer();

        playSong();

        handler = new Handler();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();

    }

    private void updateSeekbar() {
        int currPos = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currPos);
        setCurentsTimeconvertDuration(currPos); // hàm chuyển đổi và hiển thị thời gian đang chạy của bài hát
        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private  void  playSong(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception of type : " + e.toString());
            e.printStackTrace();
        }
        //mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
                //Toast.makeText(GiaoDienPlayingActivity.this, ""+mediaPlayer.getDuration(), Toast.LENGTH_LONG).show();
                mediaPlayer.start();
                setMaxTimeconvertDuration(mediaPlayer.getDuration());//hàm chuyển đổi và hiển thị ra thời lượng của file mp3
                btnplay.setImageResource(R.drawable.ic_pause);
                updateSeekbar();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnplay.setImageResource(R.drawable.ic_play);
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                double  ratio = percent/100.0;
//                int bufferingLevel =  (int) (mediaPlayer.getDuration()*ratio);
//                seekBar.setSecondaryProgress(bufferingLevel);
            }
        });



    }
    //tham khảo tại https://www.youtube.com/watch?v=Pp_l2DP5gOA and https://www.youtube.com/watch?v=cw4z2Mv0x7Q
    private void play(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnplay.setImageResource(R.drawable.ic_play);
        }else {
            mediaPlayer.start();
            btnplay.setImageResource(R.drawable.ic_pause);
        }
    }

    private void setMaxTimeconvertDuration(long time){
        // convert duration to minute:seconds
        //long dur = Long.parseLong(path);
        String seconds = String.valueOf((time % 60000) / 1000);

        String minutes = String.valueOf(time / 60000);
        //Toast.makeText(GiaoDienPlayingActivity.this, ""+ minutes + ":" + seconds, Toast.LENGTH_SHORT).show();
        if (seconds.length() == 1) {
            txtMaxTime.setText("0" + minutes + ":0" + seconds);
        }else {
            txtMaxTime.setText("0" + minutes + ":" + seconds);
        }

    }
    private void setCurentsTimeconvertDuration(int time){
        // convert duration to minute:seconds
        //long dur = Long.parseLong(path);
        String seconds = String.valueOf((time % 60000) / 1000);

        String minutes = String.valueOf(time / 60000);
        //Toast.makeText(GiaoDienPlayingActivity.this, ""+ minutes + ":" + seconds, Toast.LENGTH_SHORT).show();
        if (seconds.length() == 1) {
            txtCurrentTime.setText("0" + minutes + ":0" + seconds);
        }else {
            txtCurrentTime.setText("0" + minutes + ":" + seconds);
        }

    }

    private void addControls() {
        btnplay = findViewById(R.id.iBtnPlay);
        seekBar = findViewById(R.id.seekBarPlayMusic);
        txtCurrentTime = findViewById(R.id.txtCurrentTimePlay);
        txtMaxTime = findViewById(R.id.txtMaxTimePlay);

        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }
}