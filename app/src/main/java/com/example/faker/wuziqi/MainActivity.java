package com.example.faker.wuziqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private Screen screen;
    private View gameParentView = null;
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getScreen();// 得到屏幕的大小
        //setContentView(R.layout.activity_main);
        gameView=new GameView(this);
        setContentView(gameView);
        /*gameView = (GameView)findViewById(R.id.gameview);
        gameView.setContext(this);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        menu.add("重新开始");
        menu.add("切换先手");
        menu.add("退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("重新开始")) {
            gameView.reStart();
        }
        else if(item.getTitle().equals("切换先手"))
        {
            gameView.change();
        }
        else if (item.getTitle().equals("退出")) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 得到屏幕的大小
     */
    private void getScreen() {
        int height = this.getWindowManager().getDefaultDisplay().getHeight();
        int width = this.getWindowManager().getDefaultDisplay().getWidth();
        screen = new Screen(width, height);
    }
}
