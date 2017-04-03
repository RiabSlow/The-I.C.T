package com.sakkar.theice;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sakkar.theice.login.LoginActivity;
import com.sakkar.theice.mainfeed.MainFeed;

public class MainActivity extends AppCompatActivity {

    TextView text;
    int height;
    ObjectAnimator animator;
    public String loggedInState = "LOGGED_IN_STATE";
    public String state = "STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null)
            return;

        text = (TextView) findViewById(R.id.text);
        height = getResources().getDisplayMetrics().heightPixels;
        animator = new ObjectAnimator();
        text.setY((float) -300.0);

        animator.setPropertyName("Y");
        animator.setFloatValues(-300, (int) (height * .5), (int) (height * .35), (int) (height * .5));
        animator.setStartDelay(500);
        animator.setDuration(4000);
        animator.setTarget(text);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animation.end();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void changeActivity() {
        SharedPreferences sharedPref = getSharedPreferences(loggedInState, Context.MODE_PRIVATE);
        Intent intent;
        if (sharedPref != null && sharedPref.contains(state)) {
            if (sharedPref.getBoolean(state, false)) {
                intent = new Intent(this, MainFeed.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                finish();
            } else {
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
                finish();
            }
        }
        else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
