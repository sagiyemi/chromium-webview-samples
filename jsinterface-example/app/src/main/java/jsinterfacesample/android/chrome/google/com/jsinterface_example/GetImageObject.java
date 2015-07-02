package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
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
    private StringBuilder mStringBuilder;
    private String mFinalString;

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

        Log.wtf(TAG, "sendImage " + message.length());

        Intent resultIntent = new Intent(mContext, ImageActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(ImageActivity.EXTRA_BITMAP, message);
        mContext.startActivity(resultIntent);

    }

    @JavascriptInterface
    public void sendImageArray(String message) {

        Log.wtf(TAG, "sendImageArray " + message);

    }

    @JavascriptInterface
    public String getStorageUrl() {
//        if (mFile == null) {
//            try {
//                mFile = File.createTempFile("myFile", ".jpg");
//                Log.wtf(TAG, "Created file " + mFile.getPath());
//                return mFile.getPath();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
        return null;
    }


//    @JavascriptInterface
//    public void createFile() {
//        mStringBuilder = new StringBuilder();
//        Log.wtf(TAG, "createFile ");
//    }
//
    @JavascriptInterface
    public void closeFile() {
        mFinalString = mStringBuilder.toString();
        Log.wtf(TAG, "closeFile " + mStringBuilder.length());
        File file = getAlbumStorageDir("myImage.jpg");
        FileOutputStream outputStream = null;

        try {

            outputStream = new FileOutputStream(file);
            byte[] decodedString = Base64.decode(mFinalString, Base64.DEFAULT);
            outputStream.write(decodedString);
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.wtf(TAG, "closeFile " + mFinalString);
    }

    @JavascriptInterface
    public void append(String chunk) {

        if (mStringBuilder == null) {
            mStringBuilder = new StringBuilder();
            String base64 = "base64,";
            String content = chunk.substring(chunk.indexOf(base64) + base64.length());
            mStringBuilder.append(content);
        } else {
            mStringBuilder.append(chunk);
        }

        Log.wtf(TAG, "append " + mStringBuilder.length());
    }

    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    public File getAlbumStorageDir(String filename) {

        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, filename);

        return file;
    }


}
