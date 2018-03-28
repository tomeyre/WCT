package eyresapps.com.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import eyresapps.com.data.FilterItem;

import static eyresapps.com.utils.ScreenUtils.convertDpToPixel;

/**
 * Created by thomaseyre on 27/03/2018.
 */

public class AnimateFilter {

    public void showBackground(){

    }

    public void hideBackground(){

    }

    public void showAll(ArrayList<FilterItem> animateFilterList, LinearLayout dateRow){
        for(int i = 0; i < animateFilterList.size(); i++) {
            if(animateFilterList.get(i).getShow()) {
                animateFilterList.get(i).getCardView().animate()
                        .alpha(1)
                        .setDuration(250)
                        .setStartDelay(500)
                        .start();
            }else{
                animateFilterList.get(i).getCardView().animate()
                        .alpha(0.5f)
                        .setDuration(250)
                        .setStartDelay(500)
                        .start();
            }
        }
        dateRow.animate()
                .alpha(1)
                .setDuration(250)
                .setStartDelay(500)
                .start();
    }

    public void hideAll(ArrayList<FilterItem> animateFilterList, LinearLayout dateRow){
        for(int i = 0; i < animateFilterList.size(); i++) {
            animateFilterList.get(i).getCardView().animate()
                    .alpha(0)
                    .setDuration(250)
                    .setStartDelay(0)
                    .start();
        }
        dateRow.animate()
                .alpha(0)
                .setDuration(250)
                .setStartDelay(0)
                .start();
    }

    public void expandHeight(final CardView cv, Context context, float height)
    {
        System.out.println("expand height = " + height);
        ValueAnimator anim = ValueAnimator.ofInt((int) convertDpToPixel(36, context),
                (int) height);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.height = val;
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(8);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    public void expandWidth(final CardView cv, Context context)
    {
        ValueAnimator anim = ValueAnimator.ofInt((int) convertDpToPixel(36, context),
                ScreenUtils.getScreenWidth(context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.width = val;
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(8);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    public void shrinkHeight(final CardView cv, Context context, float height)
    {
        System.out.println("shrink height = " + height);
        ValueAnimator anim = ValueAnimator.ofInt((int) height,
                (int) convertDpToPixel(36, context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.height = val;
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(8);
            }
        });
        anim.setDuration(500);
        anim.setStartDelay(250);
        anim.start();
    }

    public void shrinkWidth(final CardView cv, Context context)
    {
        ValueAnimator anim = ValueAnimator.ofInt(ScreenUtils.getScreenWidth(context),
                (int) convertDpToPixel(36, context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.width = val;
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(8);
            }
        });
        anim.setDuration(500);
        anim.setStartDelay(250);
        anim.start();
    }
}
