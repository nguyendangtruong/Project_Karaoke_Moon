package com.project_karaoke.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project_karaoke.GiaoDienHatActivity;
import com.project_karaoke.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeAdapter extends ArrayAdapter<HomeViewModel> implements Filterable {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://karaoke-app-9f87c.appspot.com");


    Context context;
    int resource;
    @NonNull List<HomeViewModel> objects;
    public HomeAdapter(@NonNull Context context, int resource, @NonNull List<HomeViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    //hàm tìm kiếm bằng filter https://www.geeksforgeeks.org/searchview-in-android-with-recyclerview/
    public void filterList(ArrayList<HomeViewModel> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        objects = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View row, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(this.resource, null);
        ImageView _img =(ImageView) row.findViewById(R.id.imgBHKaraoke);
        TextView _tenbh = (TextView) row.findViewById(R.id.txtTenBaiHat);
        TextView _tencasi = (TextView) row.findViewById(R.id.txtTenCaSi);
        String _id; //id của bài hát

        Button _btntaive= (Button) row.findViewById(R.id.btnTaiVe);
        //hiển thị include khi nhấn nút tải về
        View anim = (View) row.findViewById(R.id.loading_taive_anim);


        HomeViewModel homeViewModel =this.objects.get(position);
        //_img.setImageResource(homeViewModel.getImg());
        _id =( homeViewModel.getId());
        _tenbh.setText(homeViewModel.getBaihat());
        _tencasi.setText(homeViewModel.getCasi());
        if(homeViewModel.getImg()==null){
            _img.setImageResource(R.drawable.image_default_bh);
        }else
        {
            Picasso.get().load(homeViewModel.getImg()).into(_img);
        }

        KiemTraFileBHtrongBoNho( _id, _btntaive);



        //View finalRow = row;  //copy View để sử dụng load animation
        _btntaive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BHDaDuocTaiVe( _id))
                {
                    Intent intent = new Intent(context, GiaoDienHatActivity.class);
                    intent.putExtra("idBH",_id);
                    intent.putExtra("tenBH", homeViewModel.getBaihat());
                    context.startActivity(intent);
                }
                else {
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //khi click sẽ ẩn btnTaive và show include progressBarTaiVe

                    _btntaive.setVisibility(View.GONE);
                    anim.setVisibility(View.VISIBLE);

                    //tạo file External stroge để lưu các tệp
                    File rootPath = new File(Environment.getExternalStorageDirectory(), "MoonKara");
                    if(!rootPath.exists()) {
                        rootPath.mkdirs();
                    }

                    String pathLocalFileLRC = (String) _id + ".lrc";
                    final File localFileLRC = new File(rootPath, pathLocalFileLRC);     //tạo định dạng tên khi lưu trong local Storage

                    String pathFirebaseLRC=(String) "/SoucreKaraoke/FileLRC/"+ _id+".lrc";     //lấy đại chỉ đường dẫn file trên Firebase
                    StorageReference imagesRef = storageRef.child(pathFirebaseLRC);

                    //hàm tải file LRC từ Firebase và luu trong bộ nhớ
                    imagesRef.getFile(localFileLRC).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(context, "Save Failed LRC", Toast.LENGTH_SHORT).show();

                        }
                    });


                    String pathLocalMP3 = (String) _id + ".mp3";
                    final File localFilemp3 = new File(rootPath, pathLocalMP3);      //tạo định dạng tên khi lưu trong local Storage

                    String pathFirebaseMP3 = (String) "/SoucreKaraoke/FileMp3/"+ _id+".mp3";
                    StorageReference mp3Ref = storageRef.child(pathFirebaseMP3);    //lấy đại chỉ đường dẫn file trên Firebase

                    //hàm tải file Mp3 từ Firebase và luu trong bộ nhớ
                    mp3Ref.getFile(localFilemp3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            KiemTraFileBHtrongBoNho(_id, _btntaive);
                            anim.setVisibility(View.GONE);
                            _btntaive.setVisibility(View.VISIBLE);
                            //chuyển qua màn hình bắt đầu hát
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent intent = new Intent(context, GiaoDienHatActivity.class);
//                                    intent.putExtra("idBH",_id);
//                                    context.startActivity(intent);
//                                }
//                            }, 3000);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            anim.setVisibility(View.GONE);
                            _btntaive.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Download Failed MP3", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return row;
    }




    public void KiemTraFileBHtrongBoNho(String _id, Button _btntaive){ //kiểm tra các file trong bộ nhớ và so sánh với id để xem bài hát đã đc tải về máy hay chưa
        //truy xuất đến file /MoonKara trong bộ nhớ
        File path = new File(Environment.getExternalStorageDirectory(), "/MoonKara/");
        //kiểm tra xem file đã được tạo trong bộ nhớ chưa
        if(!path.exists()) {
            path.mkdirs();
        }
        ArrayList<String> myList = new ArrayList<>();
        //hàm đọc tên file trong bộ nhớ
        File list[] = path.listFiles();
        for( int i=0; i< list.length; i++)  //lấy danh sách các file
        {
            for (String w : list[i].getName().split("\\.")) {  // tách tên file với đuôi mở rộng
                //kiểm tra xem tên file có trùng với id bài hát không, nếu trùng thì người dùng bài hát đã tải về rồi
                if(w.equals(_id))
                {
                    _btntaive.setText("Hát");
                    _btntaive.setBackgroundResource(R.drawable.button_listview_hat);
                    myList.add(w);

                }

            }

        }
        //xóa phần tử trùng lặp trong arrayList
        Set<String> set = new HashSet<>(myList);
        myList.clear();
        myList.addAll(set);

    }
    public boolean BHDaDuocTaiVe(String _id){
        Boolean taive = false;
        //truy xuất đến file /MoonKara trong bộ nhớ
        File path = new File(Environment.getExternalStorageDirectory(), "/MoonKara/");
        //kiểm tra xem file đã được tạo trong bộ nhớ chưa
        if(!path.exists()) {
            path.mkdirs();
        }
        ArrayList<String> myList = new ArrayList<>();
        //hàm đọc tên file trong bộ nhớ
        File list[] = path.listFiles();
        for( int i=0; i< list.length; i++)  //lấy danh sách các file
        {
            for (String w : list[i].getName().split("\\.")) {  // tách tên file với đuôi mở rộng
                //kiểm tra xem tên file có trùng với id bài hát không, nếu trùng thì người dùng bài hát đã tải về rồi
                if(w.equals(_id))
                {
                    taive =true;

                }

            }

        }


        return taive;
    }



}
