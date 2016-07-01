package com.atguigu.beijingnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.activity.GuideActivity;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.utils.CacheUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rl_rootview;
//    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        s.toString();
        rl_rootview = (RelativeLayout) findViewById(R.id.rl_rootview);

        //三个动画:旋转动画，缩放动画，渐变动画

        RotateAnimation ra  = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);//持续播放时间
        ra.setFillAfter(true);//停留在播放后的状态


        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        AlphaAnimation aa  = new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);


        //开始播放动画
        rl_rootview.startAnimation(set);

        //监听动画播放完成
        set.setAnimationListener(new MyAnimationListener());


    }

    class MyAnimationListener implements Animation.AnimationListener {

        /**
         * 当动画开始播放的时候回调这个方法
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 当动画播放完成的时候回调这个方法
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {

            //1.得到信息，是否已经进入过主页面
            Boolean isStartMain = CacheUtils.getBoolean(SplashActivity.this,GuideActivity.START_MAIN);
            if(isStartMain){
                //主页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }else{
//                Toast.makeText(SplashActivity.this, "动画播放结束要进入主页面了哦", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this,GuideActivity.class));
            }

            finish();

//
        }

        /**
         * 当动画重复播放的时候回调这个方法
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
