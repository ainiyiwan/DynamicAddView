package com.zy.xxl.dynamicaddview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zy.xxl.dynamicaddview.helper.AnimHelper;
import com.zy.xxl.dynamicaddview.util.PreUtils;
import com.zy.xxl.dynamicaddview.widget.Html5Webview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zy.xxl.dynamicaddview.util.PreCons.FIRST_OPEN_SHARE_ACT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.add)
    Button add;
    @BindView(R.id.icon)
    ImageView icon;//会动的图标
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.click_me)
    Button clickMe;

    private Html5Webview webView;
    private ImageView imageView;
    private float sacle = 0.2F;

    private boolean isFirst = false;//是否是第一次进入
    private boolean isAdd = false;//是否添加过
    private int time = 0;//关闭按钮点击次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        if (PreUtils.getPrefBoolean(this, FIRST_OPEN_SHARE_ACT, true)) {
            isFirst = PreUtils.getPrefBoolean(this, FIRST_OPEN_SHARE_ACT, true);
            dynamicAddView(webView, imageView);
            icon.setVisibility(View.GONE);
            PreUtils.setPrefBoolean(this, FIRST_OPEN_SHARE_ACT, false);
        } else {
            initIconAnim();//如果不是第一次 说明icon处于显示状态
        }

    }

    //开始Icon的动画
    private void initIconAnim() {
        final ObjectAnimator nopeAnimator = AnimHelper.nope(icon);
//                nopeAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        nopeAnimator.setStartDelay(5000);
        nopeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.e("tag", "====start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                nopeAnimator.start();
                Log.e("tag", "====end");
            }
        });
        nopeAnimator.start();
    }

    private void initView() {
        webView = new Html5Webview(this);
        webView.setId(R.id.WEB_VIEW);

        int id = webView.getId();

        webView.loadUrl("https://www.baidu.com/");

        imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_close);
        imageView.setId(R.id.IMAGE_VIEW);

    }

    @OnClick(R.id.add)
    public void onAddClicked() {
        if (!isAdd) {
            dynamicAddView(webView, imageView);
        }
    }

    //动态添加WebView
    private void dynamicAddView(final WebView webView, final ImageView imageView) {
        isAdd = true;

//        root.setVisibility(View.VISIBLE);
        //添加WebView
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        // btn1 位于父 View 的顶部，在父 View 中水平居中
        root.addView(webView, lp1);
//        rl.addView(btn1, lp1 );

        //添加ImageView
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lp2.setMargins(0, dp2px(25), dp2px(12), 0);
        root.addView(imageView, lp2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirst) {
                    if (time == 0) {
                        initView();
                        time++;
                        isAdd = false;
                        imageView.setVisibility(View.GONE);
                        ValueAnimator transAnimator = AnimHelper.getTransAnimator(webView, sacle, MainActivity.this);
                        ValueAnimator scaleAnimator = AnimHelper.getScaleAnimator(webView, sacle);

                        AnimatorSet set = AnimHelper.getAnimationSet(transAnimator, scaleAnimator);
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                root.removeAllViews();
                                icon.setVisibility(View.VISIBLE);
                                initIconAnim();
                            }
                        });
                    } else {
                        isAdd = false;
                        root.removeAllViews();
                    }
                } else {
                    isAdd = false;
                    root.removeAllViews();
                }
            }
        });
    }

    @OnClick(R.id.icon)
    public void onIconClicked() {
        if (!isAdd) {
            dynamicAddView(webView, imageView);
        }
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(final float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public int px2dp(final float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @OnClick(R.id.click_me)
    public void onViewClicked() {
        Toast.makeText(this, "啊啊", Toast.LENGTH_SHORT).show();
    }
}
