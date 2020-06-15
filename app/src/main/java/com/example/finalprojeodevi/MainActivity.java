package com.example.finalprojeodevi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView Text, Text2, Text3;
    private static int GecisSuresi = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Text = findViewById(R.id.splashText);
        Text2 = findViewById(R.id.splashText2);
        Text3 = findViewById(R.id.splashText3);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent gecis = new Intent(MainActivity.this, LoginActivity.class);
                //Intent gecis = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(gecis);
                finish();
            }
        }, GecisSuresi);
    }
}
