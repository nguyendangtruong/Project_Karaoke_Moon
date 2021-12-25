package com.project_karaoke;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project_karaoke.view.DefaultLrcBuilder;
import com.project_karaoke.view.ILrcBuilder;
import com.project_karaoke.view.LrcRow;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class GiaoDienHatActivity extends AppCompatActivity {

    private SlidrInterface slidr;
    ImageButton btnHat, btnBackHome;
    TextView txttenBH, txtLoiBH;
    String receiveIDBaiHat;
    String receiveTenBaiHat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_hat);

        addControls();
        //nhận dữ liệu từ sự kiện chuyển màn hình
        Intent intentNhanID = getIntent();
        receiveIDBaiHat = intentNhanID.getStringExtra("idBH");
        receiveTenBaiHat = intentNhanID.getStringExtra("tenBH");

        txttenBH.setText(receiveTenBaiHat); //nhận lời bh từ putExtra và hiển thị ra màn hình

        txtLoiBH.setMovementMethod(new ScrollingMovementMethod());
        txtLoiBH.setText(getFromAssets());

        btnHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGuiID = new Intent(GiaoDienHatActivity.this, KaraActivity.class);
                intentGuiID.putExtra("idBHFromGiaoDien", receiveIDBaiHat);
                intentGuiID.putExtra("tenBHFromGiaoDien", receiveTenBaiHat);
                intentGuiID.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentGuiID);
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        //Swipe Close Activity with Slidr Library https://www.youtube.com/watch?v=Ci5146VOyg4
        slidr = Slidr.attach(this);
    }

    public void lockSlide(View v) {
        slidr.lock();
    }

    public void unlockSlide(View v) {
        slidr.unlock();
    }

    private void addControls() {
        btnHat = findViewById(R.id.imageButtonStartHat);
        btnBackHome = findViewById(R.id.imageButtonBackHome);
        txttenBH = findViewById(R.id.txtTenBHGiaodienHat);
        txtLoiBH = findViewById(R.id.txtShowAllLyricBH);
    }

    public String getFromAssets(){
        try {
            File sdcard = Environment.getExternalStorageDirectory();

            File file = new File(sdcard,("/MoonKara/"+receiveIDBaiHat+".lrc"));

            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line="";
                String Result="";
                while((line = br.readLine()) != null){
                    if(line.trim().equals(""))
                        continue;
                    String linecut =line.substring(10);//loại bỏ 10 ký tự đầu tiên, //chỉ lấy lời bài hát phía sau
                    Result += linecut + "\r\n";
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
}
