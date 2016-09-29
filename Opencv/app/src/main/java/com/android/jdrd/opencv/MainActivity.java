package com.android.jdrd.opencv;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.mipmap.picture)).getBitmap();
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int[] resultPixes = OpenCVHelper.gray(pix, w, h);
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        result.setPixels(resultPixes, 0, w, 0, 0, w, h);
        img = (ImageView) findViewById(R.id.img);
        img.setImageBitmap(result);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = OpenCVHelper.test();
                Log.e("MainAcitivity", "return" + i);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = OpenCVHelper.send();
                Log.e("MainAcitivity", "return" + i);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = OpenCVHelper.init();
                Log.e("MainAcitivity", "return" + i);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mat mat = new Mat();
                int i = OpenCVHelper.getdata(mat.getNativeObjAddr());
                Log.e("MainAcitivity", "return" + i);
                if (!mat.empty()) {
                    Log.e("MainAcitivity", "return" + mat.size());
                    Bitmap result = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
                    Utils.matToBitmap(mat, result);
                    img.setImageBitmap(result);
                }
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = OpenCVHelper.stop();
                Log.e("MainAcitivity", "return" + i);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                //OpenCV���سɹ������뼶��������
                case LoaderCallbackInterface.SUCCESS: {
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
}
