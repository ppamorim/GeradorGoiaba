package com.goiaba.goiabas.geradorgoiaba.utils;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class AnimationUtils {

    private static final int ANIMATION_DURATION = 4000;
    private static final int ANIMATION_INTERPOLATION = 6;

    public static void animateView(View view) {
        animate(view)
                .setInterpolator(new DecelerateInterpolator(ANIMATION_INTERPOLATION))
                .translationX(0)
                .alpha(100)
                .setDuration(ANIMATION_DURATION)
                .start();
    }

}
