package com.asaewing.healthimprover.app2.Interface;


import android.animation.Animator;
import android.view.View;
import android.widget.RelativeLayout;

public interface RL_Action {
    Action superAction = new Action();

    void openMoveRL(boolean setFabUse, RelativeLayout relativeLayout, double moveY);

    void closeRL(boolean setFabUse,final RelativeLayout relativeLayout, long Time);

    class Action {
        public void openMoveRL(boolean setFabUse, RelativeLayout relativeLayout, double moveY){
            if (setFabUse){
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.animate().alpha(1f)
                        .translationY(-(float)(moveY))
                        .setDuration(360)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }
                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
            }
        }

        public void closeRL(boolean setFabUse,final RelativeLayout relativeLayout, long Time){
            if (setFabUse) {
                relativeLayout.animate().alpha(0f)
                        .translationY(0f).setDuration(Time)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                relativeLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
            }
        }
    }
}


