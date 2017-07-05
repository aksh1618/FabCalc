package com.aksh.fabcalc.utils;

import static com.aksh.fabcalc.utils.LabelsLists.getLabelsForState;

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Aakarshit on 16-06-2017.
 */

public class Animations {

    private static final int ANIMATION_DURATION = 800;

    public static void animateActivityIn(ViewGroup viewGroup){
        AlphaAnimation animation = new AlphaAnimation(0.0f , 1.0f ) ;
        animation.setFillAfter(true);
        animation.setDuration(ANIMATION_DURATION / 2);
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
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    public static void animateButtonsIn(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(ANIMATION_DURATION + i * 2 *ANIMATION_DURATION / (3 * childCount))
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

    public static void animateKeysToState(ViewGroup viewGroup, States state) {
        final int childCount = viewGroup.getChildCount();
        List<String> labels = getLabelsForState(state);
        for (int i = 0; i < childCount; i++) {
            final View child = viewGroup.getChildAt(i);
            final String label = labels.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                child.animate()
                        .setStartDelay(i)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .alpha(0)
                        .scaleX(0f)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                ((MyFab)child).setDrawableText(label);
                                child.animate()
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .alpha(1)
                                        .scaleX(1)
                                        .scaleY(1);
                            }
                        });
            }
        }

    }
}
