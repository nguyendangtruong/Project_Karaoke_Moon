package com.project_karaoke.ui.upnhac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project_karaoke.R;

import java.io.File;

public class UpNhacFragment extends Fragment {

    private static final int PICK_FILELRC_REQUEST =1;
    private static final int PICK_FILEMP3_REQUEST =2;

    View v;
    EditText etxtTenBaiHat, etxtTenCasi;
    TextView txtShowNameFileMP3, txtShowNameFileLRC;
    Button btnSubmit, btnChonFileNhac, btnChonFileLRC;
    int countBH;//đếm số lượng bài hát đang có trên Firebase realtime
    String id =null;// id bài hát
    DatabaseReference mDatabase, mDataBaihat;
    UpNhacViewModel upNhacViewModel;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://karaoke-app-9f87c.appspot.com");
    
    public static UpNhacFragment newInstance() {
        return new UpNhacFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_up_nhac, container, false);
        addControls();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLRCUri ==null|| mMP3Uri==null)
                {
                    Toast.makeText(getActivity(), "Vui lòng chọn đủ file trước khi upload", Toast.LENGTH_SHORT).show();
                }else {
                    WriteVaoRealtime();
                    WriteVaoFireStorage();
                }

            }
        });
        UpFileLenFireStorage();

        return  v;
    }

    private void WriteVaoFireStorage() {
        //ghi gile LRC vào firebase Storage
        StorageReference storageLRC = storageRef.child("/SoucreKaraoke/FileLRC").child(id+".lrc");
        storageLRC.putFile(mLRCUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
        //ghi file vào đường dẫn này
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        StorageReference storageMP3 = storageRef.child("/SoucreKaraoke/FileMp3").child(id+".mp3");
        storageMP3.putFile(mMP3Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Upload thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                if(progress==100.0){
                    progressDialog.cancel();
                }
            }
        });
        //Picasso.get().load(mImgUri).into(imageView);
    }

    private void UpFileLenFireStorage() {
        //nhấn vào chọn file sẽ chuyến sang màn hình bộ nhớ
        btnChonFileLRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooseLRC();
            }
        });
        btnChonFileNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooseNhac();
            }
        });
    }

    private void openFileChooseNhac() {
        Intent intent = new Intent();
        intent.setType("audio/mpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILEMP3_REQUEST);
    }

    private void openFileChooseLRC() {
        Intent intent = new Intent();
        intent.setType("application/octet-stream");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILELRC_REQUEST);
    }

    Uri mLRCUri;
    Uri mMP3Uri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILELRC_REQUEST && requestCode == 1 && data != null  && data.getData() != null){
            mLRCUri = data.getData();
            File file = new File(data.getData().getPath()); //lấy đường dẫn file
            String fileName = file.getName(); // từ dường dẫn lấy tên file để hiển ra textView để người dùng biết tên file họ đã chọn
            txtShowNameFileLRC.setText(fileName);
        }else {
            if (requestCode == PICK_FILEMP3_REQUEST && requestCode == 2 && data != null  && data.getData() != null){
                mMP3Uri = data.getData();
                File file = new File(data.getData().getPath()); //lấy đường dẫn file
                String fileName = file.getName(); // từ dường dẫn lấy tên file để hiển ra textView để người dùng biết tên file họ đã chọn
                txtShowNameFileMP3.setText(fileName);
            }
        }

    }

    private void DemSoBHDangCo() {
        mDatabase = FirebaseDatabase.getInstance().getReference("karaoke");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countBH = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void WriteVaoRealtime() {
        String UIDBaiHat;  //biến tạo child ghi thêm vào firebase
        UIDBaiHat = String.valueOf(countBH+1);
        mDataBaihat = mDatabase.child(UIDBaiHat);

        String tenBH = etxtTenBaiHat.getText().toString();
        String tenCasi = etxtTenCasi.getText().toString();
        if(countBH<9){
            id = "000" + String.valueOf(countBH+1);
        }
        if(countBH>=9 && countBH<99){
            id = "00" + String.valueOf(countBH+1);
        }
        if(countBH>=99 && countBH<999){
            id = "0" + String.valueOf(countBH+1);
        }
        if(countBH>=999 && countBH<9999){
            id = String.valueOf(countBH+1);
        }
        String img = "https://firebasestorage.googleapis.com/v0/b/karaoke-app-9f87c.appspot.com/o/SoucreKaraoke%2FFileImg%2Fanh_icon_app.png?alt=media&token=847ad662-b279-4443-a8a4-3ec70b0c6445";
        if(TextUtils.isEmpty(tenBH) || TextUtils.isEmpty(tenCasi) || TextUtils.isEmpty(id) || TextUtils.isEmpty(img))
        {
            Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            adDataFireBase(tenBH, tenCasi, id, img);
        }


    }

    private void adDataFireBase(String tenBH, String tenCasi, String id, String img) {
        upNhacViewModel = new UpNhacViewModel();
        upNhacViewModel.setBaihat(tenBH);
        upNhacViewModel.setCasi(tenCasi);
        upNhacViewModel.setId(id);
        upNhacViewModel.setImg(img);

        mDataBaihat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDataBaihat.setValue(upNhacViewModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        DemSoBHDangCo(); // đếm số bài hát đang có để Set ID
    }

    private void addControls() {
        etxtTenBaiHat = v.findViewById(R.id.etxtTenBaiHatUpNhac);
        etxtTenCasi = v.findViewById(R.id.etxtTenCaSiUpNhac);
        btnSubmit = v.findViewById(R.id.btnSubmitUpNhac);
        btnChonFileNhac = v.findViewById(R.id.btnChonFileNhac);
        btnChonFileLRC = v.findViewById(R.id.btnChonFileLRC);
        txtShowNameFileMP3 = v.findViewById(R.id.txtShowNameFileMP3);
        txtShowNameFileLRC = v.findViewById(R.id.txtShowNameFileLRC);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(UpNhacViewModel.class);
        // TODO: Use the ViewModel
    }

}