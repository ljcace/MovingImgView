package com.ljc.movingimgview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * use by SurfaceView
 *
 * Created by lijiacheng on 16/5/5.
 */
public class MyImageView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Context context;
    private SurfaceHolder holder;
    private Bitmap icon;
    private Paint paint;
    private boolean running = true;
    private int x = 0, y = 0, i = 1;
    private int width;
    private int height;
    private float all;

    private int REFRESH_TIME = 20;
    private int MOVE_SPEED = 5;
    private int MAX_WIDTH = 460;
    private int MAX_HEIGHT = 460;
    private int DEFALUT_RES;
    private boolean isHorizantal;

    public MyImageView(Context context) {
        this(context, null);

    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        this.context = context;
        this.DEFALUT_RES = R.mipmap.ic_cat;
        holder = this.getHolder();//获取holder
        holder.addCallback(this);
    }

    public void setImage(Bitmap bitmap) {
        icon = bitmap;
        x = 0;
        y = 0;
        i = 1;
        width = icon.getWidth();
        height = icon.getHeight();
        all = (width + height) * 1.0f;
        if (width == height) {
            running = false;
        } else {
            running = true;
            if (width > height) {
                isHorizantal = true;
            } else {
                isHorizantal = false;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        icon = BitmapFactory.decodeResource(context.getResources(), DEFALUT_RES);
        width = icon.getWidth();
        height = icon.getHeight();
        all = (width + height) * 1.0f;
        paint = new Paint();
        running = true;
        new Thread(this).start();
        if (width == height) {
            running = false;
        } else {
            running = true;
            if (width > height) {
                isHorizantal = true;
            } else {
                isHorizantal = false;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            if (width == height) {
                running = false;
            } else {
                running = true;
            }
            if (i == 1) {
                if (isHorizantal) {
                    x += MOVE_SPEED;
                    if (x + MAX_WIDTH > width) {
                        i = -1;
                        x = width - MAX_WIDTH;
                    }
                } else {
                    y += MOVE_SPEED;
                    if (y + MAX_HEIGHT > height) {
                        i = -1;
                        y = height - MAX_HEIGHT;
                    }
                }
//                x += (int) ((width / all) * MOVE_SPEED);
//                y += (int) ((height / all) * MOVE_SPEED);
//                if (x + MAX_WIDTH > width || y + MAX_HEIGHT > height) {
//                    i = -1;
//                    x = width - MAX_WIDTH;
//                    y = height - MAX_HEIGHT;
//                }
            } else if (i == -1) {
                if (isHorizantal) {
                    x -= MOVE_SPEED;
                    if (x < 0) {
                        i = 1;
                        x = 0;
                    }
                } else {
                    y -= MOVE_SPEED;
                    if (y < 0) {
                        i = 1;
                        y = 0;
                    }
                }
//                x -= (int) ((width / all) * MOVE_SPEED);
//                y -= (int) ((height / all) * MOVE_SPEED);
//                if (x < 0 || y < 0) {
//                    i = 1;
//                    x = 0;
//                    y = 0;
//                }
            }
            Log.e("i,x,y", "i=" + i + ",x=" + x + ",y=" + y);
            Canvas canvas = holder.lockCanvas();//获取画布
            canvas.drawColor(Color.BLACK);
            Bitmap b = Bitmap.createBitmap(icon, x, y, MAX_WIDTH, MAX_HEIGHT);
            canvas.drawBitmap(b, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
            b = null;
            try {
                Thread.sleep(REFRESH_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getREFRESH_TIME() {
        return REFRESH_TIME;
    }

    public void setREFRESH_TIME(int REFRESH_TIME) {
        this.REFRESH_TIME = REFRESH_TIME;
    }

    public int getMOVE_SPEED() {
        return MOVE_SPEED;
    }

    public void setMOVE_SPEED(int MOVE_SPEED) {
        this.MOVE_SPEED = MOVE_SPEED;
    }

    public int getMAX_WIDTH() {
        return MAX_WIDTH;
    }

    public void setMAX_WIDTH(int MAX_WIDTH) {
        this.MAX_WIDTH = MAX_WIDTH;
    }

    public int getMAX_HEIGHT() {
        return MAX_HEIGHT;
    }

    public void setMAX_HEIGHT(int MAX_HEIGHT) {
        this.MAX_HEIGHT = MAX_HEIGHT;
    }
}
