package com.eyresapps.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.eyresapps.crimetracker.R;
import com.eyresapps.data.FilterItem;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import static com.eyresapps.utils.ScreenUtils.convertDpToPixel;
import static com.eyresapps.utils.ScreenUtils.getScreenHeight;
import static com.eyresapps.utils.ScreenUtils.getScreenWidth;

/**
 * Created by thomaseyre on 27/03/2018.
 */

public class AnimateFilter {

    public void showBackground(){

    }

    public void hideBackground(){

    }

    public void expandFilter(CardView cv, ImageView filterImage, Context context, Float filterHeight, ArrayList<FilterItem> filterList, LinearLayout dateRow, LinearLayout btnRow,
                             Spinner monthSpinner, Spinner yearSpinner, Button filterSearchBtn){
        if(cv.getHeight() == convertDpToPixel(36,context)) {
            filterImage.animate()
                    .alpha(0)
                    .setDuration(0)
                    .setStartDelay(0)
                    .start();
            new AnimateFilter().expandHeight(cv, context, filterHeight);
            new AnimateFilter().expandWidth(cv, context);
            final Handler handler = new Handler();
            final LinearLayout dr = dateRow;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dr.setVisibility(View.VISIBLE);
                }
            }, 675);
            new AnimateFilter().showAll(filterList,dateRow,btnRow);
            new AnimateFilter().hideBackground();
            new AnimateFilter().enableButtons(monthSpinner,yearSpinner, filterSearchBtn);
        }
    }

    public void shrinkFilter(CardView cv, ImageView filterImage, Context context,Float filterHeight, ArrayList<FilterItem> filterList, LinearLayout dateRow, LinearLayout btnRow,
                             Spinner monthSpinner,Spinner yearSpinner, Button filterSearchBtn){
        if(cv.getHeight() > convertDpToPixel(36,context)) {
            new AnimateFilter().shrinkHeight(cv, context, filterHeight);
            new AnimateFilter().shrinkWidth(cv, context);
            new AnimateFilter().hideAll(filterList,dateRow,btnRow);
            dateRow.setVisibility(View.GONE);
            new AnimateFilter().showBackground();
            new AnimateFilter().disableButtons(monthSpinner,yearSpinner, filterSearchBtn);
            filterImage.animate()
                    .alpha(1)
                    .setDuration(0)
                    .setStartDelay(750)
                    .start();
        }
    }

    public void showAll(ArrayList<FilterItem> animateFilterList, LinearLayout dateRow, LinearLayout btnRow){
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
        btnRow.animate()
                .alpha(1)
                .setDuration(250)
                .setStartDelay(500)
                .start();
    }

    public void hideAll(ArrayList<FilterItem> animateFilterList, LinearLayout dateRow, LinearLayout btnRow){
        for(int i = 0; i < animateFilterList.size(); i++) {
            animateFilterList.get(i).getCardView().animate()
                    .alpha(0)
                    .setDuration(250)
                    .setStartDelay(0)
                    .start();
        }
        dateRow.setVisibility(View.INVISIBLE);
        dateRow.animate()
                .alpha(0)
                .setDuration(250)
                .setStartDelay(0)
                .start();
        btnRow.animate()
                .alpha(0)
                .setDuration(250)
                .setStartDelay(0)
                .start();
    }

    public void disableButtons(Spinner two,Spinner three, Button filterSearchBtn){
        two.setClickable(false);
        two.setEnabled(false);
        three.setClickable(false);
        three.setEnabled(false);
        filterSearchBtn.setClickable(false);
        filterSearchBtn.setEnabled(false);
    }

    public void enableButtons(Spinner two,Spinner three, Button filterSearchBtn){
        two.setClickable(true);
        two.setEnabled(true);
        three.setClickable(true);
        three.setEnabled(true);
        filterSearchBtn.setClickable(true);
        filterSearchBtn.setEnabled(true);
    }

    public void expandHeight(final CardView cv, Context context, float height)
    {
        final Context innerContext = context;
        System.out.println("expand height = " + height);
        ValueAnimator anim = ValueAnimator.ofInt((int) convertDpToPixel(36, context),
                getScreenHeight(context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.height = val;
                cv.setCardBackgroundColor(innerContext.getResources().getColor(R.color.white));
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
                getScreenWidth(context));
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
        final Context innerContext = context;
        System.out.println("shrink height = " + height);
        ValueAnimator anim = ValueAnimator.ofInt(getScreenHeight(context),
                (int) convertDpToPixel(36, context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.height = val;
                cv.setCardBackgroundColor(innerContext.getResources().getColor(R.color.whiteFaded));
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(1);
            }
        });
        anim.setDuration(500);
        anim.setStartDelay(250);
        anim.start();
    }

    public void shrinkWidth(final CardView cv, Context context)
    {
        ValueAnimator anim = ValueAnimator.ofInt(getScreenWidth(context),
                (int) convertDpToPixel(36, context));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cv.getLayoutParams();
                layoutParams.width = val;
                cv.setLayoutParams(layoutParams);
                cv.setCardElevation(1);
            }
        });
        anim.setDuration(500);
        anim.setStartDelay(250);
        anim.start();
    }

    public void hideAdView(AdView adView, Context context){
        adView.animate()
                .y(-convertDpToPixel(100,context))
                .setDuration(250)
                .setStartDelay(0)
                .start();
    }

    public void showAdView(AdView adView){
        adView.animate()
                .y(0)
                .setDuration(250)
                .setStartDelay(0)
                .start();
    }
}
