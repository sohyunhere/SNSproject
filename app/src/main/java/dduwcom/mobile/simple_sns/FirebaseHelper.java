package dduwcom.mobile.simple_sns;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import dduwcom.mobile.simple_sns.activity.MainActivity;
import dduwcom.mobile.simple_sns.activity.PostActivity;
import dduwcom.mobile.simple_sns.listener.OnPostListener;

import static dduwcom.mobile.simple_sns.Util.isStorageUri;
import static dduwcom.mobile.simple_sns.Util.showToast;
import static dduwcom.mobile.simple_sns.Util.storageUriToName;

public class FirebaseHelper {
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;

    public FirebaseHelper(Activity activity){
        this.activity = activity;
    }
    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }
    public void storageDelete(final PostInfo postInfo){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final String id = postInfo.getId();
        ArrayList<String> contentsList = postInfo.getContents();

        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUri(contents)) {
                successCount++;

                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUriToName(contents));

                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, postInfo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, "???????????? ???????????????.");
                    }
                });
            }
        }
        storeDelete(id, postInfo);
    }

    public void storeDelete(String id, final PostInfo postInfo) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0) {
            firebaseFirestore.collection("posts").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "???????????? ?????????????????????.");
                            onPostListener.onDelete(postInfo);
                           // postUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            showToast(activity, "????????? ????????? ?????????????????????.");
                        }
                    });
        }
    }
}
