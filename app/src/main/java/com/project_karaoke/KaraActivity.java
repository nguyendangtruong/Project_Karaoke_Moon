package com.project_karaoke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.project_karaoke.view.DefaultLrcBuilder;
import com.project_karaoke.view.ILrcBuilder;
import com.project_karaoke.view.ILrcView;
import com.project_karaoke.view.LrcRow;
import com.project_karaoke.view.LrcView;

import java.util.Calendar;

public class KaraActivity extends Activity  {

    private String idBaiHat="0001"; //khởi tạo biến và đc sử dụng để nhận dữ liệu putExtra
    private String tenBaiHat="TenBH";
    public final static String TAG = "KaraActivity";
    ILrcView mLrcView;
    private int mPalyTimerDuration = 1000;
    private Timer mTimer;
    private TimerTask mTask;
    //String lrc1 =(String) Environment.getExternalStorageDirectory().getPath() + "/MoonKara/"+"0001.lrc";

    public String getFromAssets(){
        try {
            File sdcard = Environment.getExternalStorageDirectory();

            File file = new File(sdcard,("/MoonKara/"+idBaiHat+".lrc"));

            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line="";
                String Result="";
                while((line = br.readLine()) != null){
                    if(line.trim().equals(""))
                        continue;
                    Result += line + "\r\n";
                }
                return Result;
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
//            InputStream instream = openFileInput();
////            InputStream is = new FileInputStream();
//            InputStreamReader inputReader = new InputStreamReader( );
//            BufferedReader bufReader = new BufferedReader(inputReader);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //giữ màn hình không tắt

//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_giao_dien_chay_bai_hat);
        mLrcView = new LrcView(this, null);
        mLrcView = (LrcView) findViewById(R.id.lrcView3);
        //file:///android_asset/test.lrc;

        //Nhận dữ liệu id  bài hát từ Intent Giao diện Hát activity
        Intent intent = getIntent();
        idBaiHat = intent.getStringExtra("idBHFromGiaoDien");
        tenBaiHat = intent.getStringExtra("tenBHFromGiaoDien");


        String lrc = getFromAssets();
        Log.d(TAG, "lrc:" + lrc);

        ILrcBuilder builder = new DefaultLrcBuilder();
        List<LrcRow> rows = builder.getLrcRows(lrc);

        mLrcView.setLrc(rows);
        beginLrcPlay();

        mLrcView.setListener(new ILrcView.LrcViewListener() {

            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (mPlayer != null) {
                    Log.d(TAG, "onLrcSeeked:" + row.time);
                    mPlayer.seekTo((int)row.time);
                }
            }
        });

        GhiAm();
        SetUpDislay();

    }

    ImageButton btnBack;
    Button btnTenBH;
    int clickBtnTenBh=1;
    VideoView videoview;
    private void SetUpDislay() {
        btnBack = findViewById(R.id.imageButtonBackStartHat);
        btnTenBH = findViewById(R.id.btnShowBackgroundHat);
        videoview = (VideoView) findViewById(R.id.videoViewBg);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnTenBH.setText(tenBaiHat);
        btnTenBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBtnTenBh%5==0)
                {
                    videoview.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_bg);
                    videoview.setVideoURI(uri);
                    videoview.start();
                    clickBtnTenBh++;

                }else {
                    videoview.stopPlayback();
                    videoview.setVisibility(View.GONE);
                    clickBtnTenBh++;
                }

            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
        }

        videoview.setVisibility(View.GONE);



    }
    @Override
    public void onBackPressed() {
        videoview.setVisibility(View.GONE);
        // your code.
        mRecorder.stop();
        mRecorder.release();;
        mRecorder = null;
        //tiến hành xóa file khi quay lại màn hình
        File file = new File(mFileName);
        file.delete();
        finish();

    }


    MediaPlayer mPlayer;
    public void beginLrcPlay(){

        mPlayer = new MediaPlayer();
        try {
            String mp3 = Environment.getExternalStorageDirectory().getPath() + ("/MoonKara/"+idBaiHat+".mp3");
            mPlayer.setDataSource(mp3);
//            mPlayer.setDataSource(getAssets().openFd("m.mp3").getFileDescriptor());
            mPlayer.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared");
                    mp.start();
                    if(mTimer == null){
                        mTimer = new Timer();
                        mTask = new LrcTask();
                        mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
                    }
                }
            });
            mPlayer.setOnCompletionListener(new OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopLrcPlay(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    class LrcTask extends TimerTask{

        long beginTime = -1;

        @Override
        public void run() {
            if(beginTime == -1) {
                beginTime = System.currentTimeMillis();
            }

            final long timePassed = mPlayer.getCurrentPosition();
            KaraActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    mLrcView.seekLrcToTime(timePassed);
                }
            });

        }
    };


    ImageButton btnTatMic, btnKetThucGhi, btnPause;
    // creating a variable for medi recorder object class.
    private MediaRecorder mRecorder;

    // creating a variable for mediaplayer class
    private MediaPlayer mediaPlayer;

    // string variable is created for storing a file name
    private static String mFileName = null;

    public void GhiAm(){
        final Boolean[] statusMic = {true};
        btnTatMic = findViewById(R.id.ImgBtnTatMic);
        btnKetThucGhi = findViewById(R.id.imgBtnKetThucKara);
        btnPause = findViewById(R.id.imgBtnStopContinueKara);

        btnTatMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusMic[0])
                {
                    btnTatMic.setImageResource(R.drawable.icon_mic_kara_off);
                    statusMic[0] = false;
                }else {
                    btnTatMic.setImageResource(R.drawable.icon_mic_kara);
                    statusMic[0] = true;

                }
            }
        });
        startRecording();

        btnKetThucGhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnKetThucGhi.setEnabled(false);
                KetThucRecording();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });


    }

    public void playAudio() {
        // for playing our recorded audio
        // we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer.setDataSource(mFileName);

            // below method will prepare our media player
            mPlayer.prepare();

            // below method will start our media player.
            mPlayer.start();

        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void KetThucRecording() {


        // below method will stop
        // the audio recording.
        mRecorder.stop();

        // below method will release
        // the media recorder class.
        mRecorder.release();
        mRecorder = null;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =  new Intent(KaraActivity.this, GiaoDienPlayingActivity.class);
                intent.putExtra("DuongDanDenDanGhi", mFileName);
                intent.putExtra("tenBH", tenBaiHat);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    public void pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;

    }


    private void startRecording() {

            // we are here initializing our filename variable
            // with the path of the recorded audio file.
        File rootPath = new File(Environment.getExternalStorageDirectory(), "MoonKara");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        File rootPath1 = new File(Environment.getExternalStorageDirectory()+"/MoonKara/", "BanGhi");
        if(!rootPath1.exists()) {
            rootPath1.mkdirs();
        }
        //lấy thời gian hiện tại đặt tên cho file
        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmm");
        String date = df.format(Calendar.getInstance().getTime());
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoonKara/BanGhi";
            mFileName += "/"+ idBaiHat + date + ".mp3";

            // below method is used to initialize
            // the media recorder clss
            mRecorder = new MediaRecorder();

            // below method is used to set the audio
            // source which we are using a mic.
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);

            // below method is used to set
            // the output format of the audio.
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // below method is used to set the
            // audio encoder for our recorded audio.
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // below method is used to set the
            // output file location for our recorded audio
            mRecorder.setOutputFile(mFileName);
            try {
                // below method will prepare
                // our audio recorder class
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            // start method will start
            // the audio recording.
            mRecorder.start();


    }



}
