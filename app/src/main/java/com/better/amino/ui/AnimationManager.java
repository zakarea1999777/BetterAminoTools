package com.better.amino.ui;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dd.CircularProgressButton;

public class AnimationManager {
    public static void simulateSuccessProgress(final CircularProgressButton button) {
        if (button.getProgress() == 0) {
            AnimationManager.simulateSuccessProgressAnimation(button);
        }
        else {button.setProgress(0);}
        button.setProgress(0);
    }

    public static void simulateErrorProgress(final CircularProgressButton button) {
        if (button.getProgress() == 0) {
            AnimationManager.simulateErrorProgressAnimation(button);
        }
        else {button.setProgress(0);}
        button.setProgress(0);
    }

    private static void simulateSuccessProgressAnimation(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            button.setProgress(value);
        });
        widthAnimation.start();
    }

    private static void simulateErrorProgressAnimation(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            button.setProgress(value);
            if (value == 99) {
                button.setProgress(-1);
            }
        });
        widthAnimation.start();
    }
}
