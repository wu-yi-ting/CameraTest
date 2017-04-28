package com.app.bella.cameratest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by bella on 2017/4/26.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "ooo_CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera = null;
    private Context mContext;

    public CameraPreview(Context context) {
        super(context);
        this.mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mCamera == null) {
            mCamera = checkCamera(mContext);
        }

        try {
            if (mCamera != null) {
                int rotation = getDisplayOrientation();
                mCamera.setDisplayOrientation(rotation);
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            int rotation = getDisplayOrientation();
            Log.v(TAG, "rotation1＝" + String.valueOf(rotation));
            mCamera.setDisplayOrientation(rotation);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.v(TAG, "surfaceDestroyed");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void takePicture() {
        if (mCamera != null) {
            int rotation = getDisplayOrientation();
            Log.v(TAG, "rotation2＝" + String.valueOf(rotation));
            mCamera.setDisplayOrientation(rotation);
            mCamera.takePicture(null, null, mPicture);
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.v(TAG, "onPictureTaken");
            try {
                FileOutputStream fop = new FileOutputStream(imagePath());
                fop.write(data);
                fop.close();
                mCamera.startPreview();//restart

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Camera checkCamera(Context context) {
     /*Check device has a camera*/
        Camera c = null;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.v("camera", "This device has camera!");
            c = Camera.open();

        } else {
            Log.v("camera", "This device has no camera!");
        }
        return c;
    }

    private String imagePath() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BellaCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("BellaCameraApp", "failed to create directory");
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
        Log.v(TAG, "Path=" + mediaFile.getAbsolutePath());
        return mediaFile.getAbsolutePath();
    }


    public int getDisplayOrientation() {

        android.hardware.Camera.CameraInfo camInfo = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = (camInfo.orientation - degrees + 360) % 360;
        return result;
    }


}
