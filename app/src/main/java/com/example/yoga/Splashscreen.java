package com.example.yoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {

    private TextView appname;
    Animation up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        appname=(TextView) findViewById(R.id.appname);


        up= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up);
        appname.setAnimation(up);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
            }
        },3500);
    }
}