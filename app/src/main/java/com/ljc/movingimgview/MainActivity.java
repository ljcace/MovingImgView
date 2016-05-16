package com.ljc.testproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //    MyImageView sv;
    MyMoveImgView myimg;
    TextView next;
    boolean isDog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        sv = (MyImageView) findViewById(R.id.sv);
//
//        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dog);
//        sv.setMOVE_SPEED(10);
//        sv.setREFRESH_TIME(30);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sv.setImage(bitmap);
//            }
//        }, 2000);
        next=(TextView)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyimg();
            }
        });
        myimg = (MyMoveImgView) findViewById(R.id.myimg);
    }

    private void setMyimg() {
        Bitmap b;
        if (isDog)
            b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cat);
        else
            b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dog);
        isDog = !isDog;
        int width = b.getWidth();
        int height = b.getHeight();
        Matrix m = new Matrix();
        float scale;
        if (width > height) {
            scale = 460.0f / height;
        } else {
            scale = 460.0f / width;
        }
        m.postScale(scale, scale);
        myimg.setImg(Bitmap.createBitmap(b, 0, 0, width, height, m, true));
    }
}
