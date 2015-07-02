package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sagiyemini on 7/1/15.
 */
public class GetImageObject {

    private static final String TAG = "GetImageObject";
    private Context mContext;
    private File mFile;
    private FileOutputStream mOutputStream;

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

        Intent resultIntent = new Intent(mContext, ImageActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(ImageActivity.EXTRA_BITMAP, message);
        mContext.startActivity(resultIntent);

    }

    @JavascriptInterface
    public void closeFile() {
        try {
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void append(String chunk) {
        byte[] decodedString = Base64.decode(chunk, Base64.DEFAULT);
        try {
            mOutputStream.write(decodedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void createFile() {
        if (mFile == null) {
            mFile = getStorageDir("file.zip");
            try {
                mOutputStream = new FileOutputStream(mFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    public File getStorageDir(String filename) {

        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, filename);

        return file;
    }


}
