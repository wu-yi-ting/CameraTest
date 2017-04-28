package com.app.bella.cameratest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/*
 * Created by bella on 2017/4/26.
 */

public class MainActivity extends AppCompatActivity {

    final static String TAG = "ooo_MainActivity";
    private Button mCaptureButton;
    private CameraPreview mPreview;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCaptureButton = (Button) findViewById(R.id.btn_capture);
        mFrameLayout = (FrameLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(this);
        mFrameLayout.addView(mPreview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Take Photo");
                mPreview.takePicture();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }
}
