package dduwcom.mobile.simple_sns;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;

public class Util {
    public Util(){
        /**/
    }

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
    public static boolean isStorageUri(String uri){
        return Patterns.WEB_URL.matcher(uri).matches() && uri.contains("https://firebasestorage.googleapis.com/v0/b/simple-snsproject.appspot.com/o/post");
    }

    public static String storageUriToName(String uri){
        return uri.split("\\?")[0].split("%2F")[uri.split("\\?")[0].split("%2F").length-1];
    }
}
