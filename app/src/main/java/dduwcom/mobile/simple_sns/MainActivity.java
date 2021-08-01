package dduwcom.mobile.simple_sns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignUpActivity();
        }
        findViewById(R.id.logoutBtn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logoutBtn:
                    FirebaseAuth.getInstance().signOut();
                    startSignUpActivity();
                    break;
            }
        }
    };
        private void startSignUpActivity(){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
}