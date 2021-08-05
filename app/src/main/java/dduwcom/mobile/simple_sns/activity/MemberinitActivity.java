package dduwcom.mobile.simple_sns.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import dduwcom.mobile.simple_sns.MemberInfo;
import dduwcom.mobile.simple_sns.R;
import dduwcom.mobile.simple_sns.activity.CameraActivity;

public class MemberinitActivity extends BasicActivity {

    private static final String TAG = "MemberinitActivity";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.checkBtn).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkBtn:
                    profileUpdate();
                    break;
                case R.id.profileImageView:
                    CardView cardView = findViewById(R.id.btnsCardView);
                    if (cardView.getVisibility() == View.VISIBLE)
                        cardView.setVisibility(View.GONE);
                    else
                        cardView.setVisibility(View.VISIBLE);
                    break;
                case R.id.picture:
                    myStartActivity(CameraActivity.class);
                    break;
                case R.id.gallery:

                    if (ContextCompat.checkSelfPermission(
                            MemberinitActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemberinitActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberinitActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        } else {
                            startToast("권한을 허용해주세요.");
                        }

                    }else{
                        myStartActivity(GalleryActivity.class);
                    }

                    break;
            }
        }
    };

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        myStartActivity(GalleryActivity.class);
                    } else {
                        startToast("권한을 허용해주세요.");
                    }
            }
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    profilePath = data.getStringExtra("profilePath");
                    Log.e("로그: ", "profilePath" + profilePath);

                    Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);
                }
                break;
        }
    }

    private void profileUpdate() {
        final String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();
        final String address = ((EditText) findViewById(R.id.editTextTextPostalAddress)).getText().toString();

        if (name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5 && address.length() > 0) {

            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "profileImage.jpg");

            if(profilePath == null){
                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address);
                uploader(memberInfo);
            }
            else{
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address, downloadUri.toString());
                                uploader(memberInfo);

                            } else {
                                startToast("회원정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그: ", "에러: " + e.toString());
                }
            }

        } else {
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void uploader(MemberInfo memberInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("회원정보 등록을 성공하였습니다.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보 등록에 실패하였습니다.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   //로그인 성공후 뒤로가기 버튼을 클릭하면 앱 종료
        startActivityForResult(intent, 0);
    }
}