package com.example.faker.wuziqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FristActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
        Handler handler = new Handler();
        handler.postDelayed(new Loading(this), 2000);

    }


    private class Loading implements Runnable {
        Activity activity;
        public Loading(Activity activity)
        {
            this.activity=activity;
        }

        public void run() {
            Intent intent = new Intent(activity,MainActivity.class);
            startActivity(intent);
            activity.finish();
        }
    }
}
