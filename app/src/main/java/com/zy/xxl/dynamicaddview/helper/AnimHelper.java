package com.zy.xxl.dynamicaddview.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.zy.xxl.dynamicaddview.R;

/**
 * Author ： zhangyang
 * Date   ： 2017/11/20
 * Email  :  18610942105@163.com
 * Description  :
 */

public class AnimHelper {

    //抖动动画
    public static ObjectAnimator nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

    public static AnimatorSet getAnimationSet(ValueAnimator trans, ValueAnimator scale) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(trans).after(scale);
        set.start();

        return set;
    }

    //缩小
    public static ValueAnimator getScaleAnimator(final View view, float scale) {

        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zy", 1.0F, scale)//
                .setDuration(500);//
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
        return anim;
    }


    /**
     * 上移
     *
     * @param view
     */

    public static ValueAnimator getTransAnimator(final View view, float sacle, AppCompatActivity activity) {

        WindowManager wm1 = activity.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();//屏幕宽度
        int height1 = wm1.getDefaultDisplay().getHeight();//屏幕高度

        float width = view.getMeasuredWidth() ;//图片宽度 乘以缩放比例
        float height = view.getMeasuredHeight() ;//图片高度 乘以缩放比例
        float left =view.getLeft();
        float top = view.getTop();
//


        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(500);
        valueAnimator.setObjectValues(new PointF(0, 0));
        float dx = (width1 - sacle * width) / 2 - 30;
        float endX = left + dx;
        float dy = top + (height - sacle * height) / 2 ;
        float endY = 0 - dy ;
        final PointF mStartValue = new PointF(left, top);
        final PointF mEndValue = new PointF(endX, endY);
        Log.e("tag","开始点X == " + left);
        Log.e("tag","开始点Y == " + top);
        Log.e("tag","结束点X == " + endX);
        Log.e("tag","结束点Y == " + endY);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                startValue = mStartValue;
                endValue = mEndValue;
                //result = x0 + t * (v1 - v0)
                float x = startValue.x + (fraction * (endValue.x - startValue.x));
                float y = startValue.y + (fraction * (endValue.y - startValue.y));


                PointF point = new PointF();
                point.set(x, y);
                return point;
            }
        });


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);

            }

        });

        return valueAnimator;
    }

}
