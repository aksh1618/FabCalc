package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.LabelsUtils.getLabelsForState;

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class AnimationUtils {

    private static final int CIRCULAR_REVEAL_DURATION = 400;
    private static final int STATE_SWITCH_DURATION = CIRCULAR_REVEAL_DURATION / 3;

    public static void animateActivityIn(ViewGroup viewGroup){
        AlphaAnimation animation = new AlphaAnimation(0.0f , 1.0f ) ;
        animation.setFillAfter(true);
        animation.setDuration(CIRCULAR_REVEAL_DURATION / 2);
        viewGroup.startAnimation(animation);
    }

    public static void revealFromCenter(View myView){

        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, myView.getWidth() - cx);
        int dy = Math.max(cy, myView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(CIRCULAR_REVEAL_DURATION);
        animator.start();
    }

    public static void revealFromXY(View myView, int x, int y){

        // get the final radius for the clipping circle
        int dx = Math.max(x, myView.getWidth() - x);
        int dy = Math.max(y, myView.getHeight() - y);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(myView, x, y, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(CIRCULAR_REVEAL_DURATION);
        animator.start();
    }

    public static void animateButtonsIn(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(
                            CIRCULAR_REVEAL_DURATION + i * 2 * CIRCULAR_REVEAL_DURATION / (3 * childCount))
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }
    }

    public static void animateButtonsOut(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(i)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(0)
                    .scaleX(0f)
                    .scaleY(0f);
        }
    }

    public static void animateKeysToState(ViewGroup viewGroup, State state) {
        final int childCount = viewGroup.getChildCount();
        List<String> labels = getLabelsForState(state);
        for (int i = 0; i < childCount; i++) {
            final View child = viewGroup.getChildAt(i);
            final String label = labels.get(i);
            // TODO: 16-07-2017 Find a better alternative?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                child.animate()
                        .setStartDelay(i)
                        .setDuration(STATE_SWITCH_DURATION)
                        .setInterpolator(new AccelerateInterpolator())
                        .alpha(0)
                        .scaleX(0f)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                ((MyFab) child).setDrawableText(label);
                                child.animate()
                                        .setDuration(STATE_SWITCH_DURATION)
                                        .setInterpolator(new DecelerateInterpolator())
                                        .alpha(1)
                                        .scaleX(1)
                                        .scaleY(1);
                            }
                        });
            } else {
                ((MyFab) child).setDrawableText(label);
            }
        }
    }

    static void animatePreviewToResult(View preview, View result) {
        result.animate()
                .setDuration(STATE_SWITCH_DURATION)
                .translationY(1f)
        ;
        preview.animate()
                .translationY(1f)
                .scaleX(2)
                .scaleY(2)
        ;
    }
}
