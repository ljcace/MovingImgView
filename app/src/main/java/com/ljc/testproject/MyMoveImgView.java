package com.ljc.testproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by lijiacheng on 16/5/11.
 */
public class MyMoveImgView extends RelativeLayout implements Runnable {
    private Context context;
    private ImageView img_thread;

    private int child_count = 0;
    private int x = 0;
    private int y = 0;
    private int i = 1;//移动方向，1正向，-1反向
    private int width = 0;
    private int height = 0;
    private Bitmap nowBitmap;
    private Bitmap nextBitmap;
    private boolean isHorizantal;
    private boolean running = false;
    private Thread thread;

    private static int MOVE_SPEED = 1;
    private static int REFRESH_TIME = 30;
    private static int MAX_WIDTH = 460;
    private static int MAX_HEIGHT = 460;


    public MyMoveImgView(Context context) {
        this(context, null);
    }

    public MyMoveImgView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyMoveImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(REFRESH_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 设置图片
     *
     * @param bitmap
     */
    public void setImg(Bitmap bitmap) {
        Loge("setImg");
        if (child_count == 0)
            nowBitmap = bitmap;
        else
            nextBitmap = bitmap;
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(460, 460);
        imageView.setLayoutParams(params);
        imageView.setBackgroundColor(0x00000000);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageBitmap(bitmap);
        changeImg(imageView);

    }

    /**
     * 切换图片
     *
     * @param img
     */
    private void changeImg(ImageView img) {
        Loge("changeImg,child_count=" + child_count);
        addImg(img);
        if (child_count == 1) {
            show(img);
        } else if (child_count > 1) {
            hide(img_thread);
            show(img);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    /**
     * 渐隐图片
     *
     * @param hideimg
     */
    private void hide(final ImageView hideimg) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0f);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hideimg.clearAnimation();
                hideimg.setVisibility(ViewGroup.GONE);
                removeImg(hideimg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hideimg.startAnimation(animation);
    }

    /**
     * 渐现图片
     *
     * @param showimg
     */
    private void show(final ImageView showimg) {
        showimg.setVisibility(View.VISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1f);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (nextBitmap != null)
                    nowBitmap = nextBitmap;
                img_thread = showimg;
                setThreadInfo();
                if (thread == null) {
                    thread = new Thread(MyMoveImgView.this);
                    thread.start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showimg.startAnimation(animation);
    }

    /**
     * 添加子视图
     *
     * @param img
     */
    private void addImg(ImageView img) {
        addView(img);
        child_count++;
    }

    /**
     * 移除子视图
     *
     * @param img
     */
    private void removeImg(ImageView img) {
        removeView(img);
        child_count--;
    }

    private void setThreadInfo() {
        width = nowBitmap.getWidth();
        height = nowBitmap.getHeight();

        x = 0;
        y = 0;
        i = 1;

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

    /**
     * 绘制视图
     */
    private void DrawImg() {
        if (running) {
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
            }
            Bitmap b = Bitmap.createBitmap(nowBitmap, x, y, MAX_WIDTH, MAX_HEIGHT);
            img_thread.setImageBitmap(b);
            b = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DrawImg();
        }
    };

    private void Loge(String text) {
        Log.e("MyMoveImgView", text);
    }
}
