package dduwcom.mobile.simple_sns.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import dduwcom.mobile.simple_sns.FirebaseHelper;
import dduwcom.mobile.simple_sns.PostInfo;
import dduwcom.mobile.simple_sns.R;
import dduwcom.mobile.simple_sns.listener.OnPostListener;
import dduwcom.mobile.simple_sns.view.ContentsItemView;
import dduwcom.mobile.simple_sns.view.ReadContentsView;

import static dduwcom.mobile.simple_sns.Util.INTENT_PATH;


public class PostActivity extends BasicActivity{
    private PostInfo postInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsView readContentsView;
    private LinearLayout contentsLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = (PostInfo)getIntent().getSerializableExtra("postInfo");

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);

        uiUpdate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo) data.getSerializableExtra("postInfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                firebaseHelper.storageDelete(postInfo);
                finish();
                return true;
            case R.id.modify:
                myStartActivity(WritePostActivity.class, postInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete() {
            Log.e("로그: ", "삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그: ", "수정정 성공");
        }
   };
    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }

    private void uiUpdate(){
        setToolbarTitle(postInfo.getTitle());
        readContentsView.setPostInfo(postInfo);
    }
}
