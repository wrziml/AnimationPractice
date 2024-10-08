package com.tollcafe.animationpractice;

import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.animation.AnimatorSet;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.AnimatorListenerAdapter;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private boolean isLike = false;
    private ImageView thumbsUpImageView, planeImageView;
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // part.1
        planeImageView = findViewById(R.id.plane_iv);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        planeImageView.startAnimation(animation);

        ImageView refreshImageView = findViewById(R.id.refresh_iv);
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planeImageView.startAnimation(animation);
            }
        });

        //part.2
        ImageView planeImageView2 = findViewById(R.id.plane_iv2);

        // 创建一个 Handler
        Handler handler = new Handler();
        float[] angles = {0f, 90f, 180f, 270f};

        Runnable animatePlane = new Runnable() {
            @Override
            public void run() {
                // 随机旋转角度
                int index = new Random().nextInt(angles.length);
                float rotationAngle = angles[index];
                if(rotationAngle==270f){
                    rotationAngle =-90f;
                }

                // 随机平移距离
                final float translationDistance = 360f; // 平移的距离

                // 先旋转，然后朝着机头方向平移
                // 设置旋转动画
                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(planeImageView2, View.ROTATION, planeImageView2.getRotation(), planeImageView2.getRotation() + rotationAngle);

                if(rotationAngle != 0){
                    rotationAnimator.setDuration(500);
                }
                rotationAnimator.start();
                rotationAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(TAG,"rotation"+ planeImageView2.getRotation()+"newX: " + planeImageView2.getTranslationY() + " newY: " + planeImageView2.getTranslationX());
                        float rotation = planeImageView2.getRotation() % 360;

                        // 计算新的位置
                        float newY = rotation == 0 || rotation == 180 ? (planeImageView2.getTranslationY() + translationDistance * (rotation == 0 ? -1 : 1)) : planeImageView2.getTranslationY();
                        float newX = rotation == 90 || rotation == 270 ? (planeImageView2.getTranslationX() + translationDistance * (rotation == 90 ? 1 : -1)) : planeImageView2.getTranslationX();

                        Log.d(TAG, "rotation"+ rotation+"newX: " + newX + " newY: " + newY);

                        // 获取布局边界
                        int layoutWidth = ((View) planeImageView2.getParent()).getWidth()-100;
                        int layoutHeight = ((View) planeImageView2.getParent()).getHeight()-100;

                        // 检查新位置是否在布局内
                        newX = Math.max(0, Math.min(newX, layoutWidth - planeImageView2.getWidth()));
                        newY = Math.max(0, Math.min(newY, layoutHeight - planeImageView2.getHeight()));

                        // 平移动画
                        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(planeImageView2, View.TRANSLATION_X, planeImageView2.getTranslationX(), newX);
                        ObjectAnimator translationAnimatorY = ObjectAnimator.ofFloat(planeImageView2, View.TRANSLATION_Y, planeImageView2.getTranslationY(), newY);
                        translationAnimator.setDuration(2000);
                        translationAnimatorY.setDuration(2000);

                        translationAnimator.start();
                        translationAnimatorY.start();
                    }
                });
                handler.postDelayed(this, 1800); // 每2秒执行一次
            }
        };
        // 启动动画
        handler.post(animatePlane);

        //part.3
        thumbsUpImageView =

                findViewById(R.id.thumbs_up_iv);

        TextView thumbsUpTextView = findViewById(R.id.thumbs_up_tv);
        thumbsUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLike = !isLike;
                thumbsUpImageView.setSelected(isLike);
                if (isLike) {
                    thumbsUpTextView.setText("Liked");
                    likeAnimation();
                } else {
                    thumbsUpTextView.setText("Like");
                }
            }
        });
    }

    private void likeAnimation() {
        View viewById = findViewById(R.id.thumbs_up_iv);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(thumbsUpImageView, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(thumbsUpImageView, "scaleY", 1f, 1.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}