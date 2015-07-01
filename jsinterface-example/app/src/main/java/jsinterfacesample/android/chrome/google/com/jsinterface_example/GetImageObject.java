package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by sagiyemini on 7/1/15.
 */
public class GetImageObject {

    private static final String TAG = "GetImageObject";
    private Context mContext;

    public GetImageObject(Context context) {
        mContext = context;
    }

    /**
     * The '@JavascriptInterface is required to make the method accessible from the Javascript
     * layer
     *
     * The code in this method is based on the documentation here:
     *
     * http://developer.android.com/training/notify-user/build-notification.html
     *
     * @param message The message displayed in the notification
     */
    @JavascriptInterface
    public void sendImage(String message) {

        Log.wtf(TAG, "showNotification " + message);

        Intent resultIntent = new Intent(mContext, ImageActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(ImageActivity.EXTRA_BITMAP, message);
        mContext.startActivity(resultIntent);

    }

}
