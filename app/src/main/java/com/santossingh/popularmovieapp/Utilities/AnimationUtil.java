package com.santossingh.popularmovieapp.Utilities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Stark on 18/04/2016.
 */
public class AnimationUtil {

    public static void animate(RecyclerView.ViewHolder holder, boolean down){
        AnimatorSet animationSet=new AnimatorSet();
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(holder.itemView,
                "translationY",
                down==true ? 200 : -200, 0);
        animatorY.setDuration(1000);
        animationSet.playTogether(animatorY);
        animationSet.start();
    }

    public static void animate2(RecyclerView.ViewHolder holder, boolean down){
        AnimatorSet animationSet=new AnimatorSet();
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(holder.itemView,
                "translationX",
                down==true ? 200 : -200, 0);
        animatorY.setDuration(1000);
        animationSet.playTogether(animatorY);
        animationSet.start();
    }
}
