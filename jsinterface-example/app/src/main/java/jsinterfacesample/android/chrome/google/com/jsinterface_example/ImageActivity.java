package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by sagiyemini on 7/1/15.
 */
public class ImageActivity extends Activity {

    public static final String EXTRA_BITMAP = "EXTRA_BITMAP";
    private static final String TAG = "ImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String bitmapStr;

        if (getIntent() != null && getIntent().hasExtra(EXTRA_BITMAP)) {
            bitmapStr = getIntent().getStringExtra(EXTRA_BITMAP);
        } else {
            Log.e(TAG, "Missing string extra");
            return;
        }

        String base64 = "base64,";
        String content = bitmapStr.substring(bitmapStr.indexOf(base64) + base64.length());

        byte[] decodedString = Base64.decode(content, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        imageView.setImageBitmap(decodedByte);

    }

}
