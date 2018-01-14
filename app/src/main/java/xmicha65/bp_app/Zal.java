package xmicha65.bp_app;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import xmicha65.bp_app.View.*;

public class Zal extends Activity {

    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private Uri mVideoUri;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


    /* Photo album for this application */
//    private String getAlbumName() {
//        return getString(R.string.album_name);
//    }


//    private File getAlbumDir() {
//        File storageDir = null;
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//
//            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
//
//            if (storageDir != null) {
//                if (! storageDir.mkdirs()) {
//                    if (! storageDir.exists()){
//                        Log.d("CameraSample", "failed to create directory");
//                        return null;
//                    }
//                }
//            }
//
//        } else {
//            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
//        }
//
//        return storageDir;
//    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
//        File albumF = getAlbumDir();
//        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
//        return imageF;
//    }

//    private File setUpPhotoFile() throws IOException {
//
//        File f = createImageFile();
//        mCurrentPhotoPath = f.getAbsolutePath();
//
//        return f;
//    }

//    private void setPic() {
//
//		/* There isn't enough memory to open up more than a couple camera photos */
//		/* So pre-scale the target bitmap into which the file is decoded */
//
//		/* Get the size of the ImageView */
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//
//		/* Get the size of the image */
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//		/* Figure out which way needs to be reduced less */
//        int scaleFactor = 1;
//        if ((targetW > 0) || (targetH > 0)) {
//            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//        }
//
//		/* Set bitmap options to scale the image decode target */
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
//		/* Associate the Bitmap to the ImageView */
//        mImageView.setImageBitmap(bitmap);
//        mVideoUri = null;
//        mImageView.setVisibility(View.VISIBLE);
//    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    private void handleSmallCameraPhoto(Intent intent) {
        System.out.println("#### 4 handleSmallCameraPhoto");
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    Button.OnClickListener openCameraListener =
            v -> {
                System.out.println("#### 2 openCameraListener");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO_S);
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mImageBitmap = null;
        mVideoUri = null;

        Button picBtn = (Button) findViewById(R.id.btnOpenCamera);
        setBtnListenerOrDisable(
                picBtn,
                openCameraListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("#### 3 onActivityResult");
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            }
        }
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("#### 2 onSaveInstanceState");
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("#### onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }

    /**
     * Check if action is available
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

}