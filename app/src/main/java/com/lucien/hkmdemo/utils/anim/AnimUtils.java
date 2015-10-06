package com.lucien.hkmdemo.utils.anim;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lucien.hkmdemo.R;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class AnimUtils {

    public static Animation slideUpAnimation(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);
        return animation;
    }

    public static Animation fadeInAnimation(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        AccelerateInterpolator interpolator = new AccelerateInterpolator();
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);
        return animation;
    }

    public static Animation showLoginAnimation(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.show_login);
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        animation.setInterpolator(interpolator);
        return animation;
    }
}
