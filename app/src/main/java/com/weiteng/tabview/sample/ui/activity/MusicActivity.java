package com.weiteng.tabview.sample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.weiteng.menu.MenuTabView;
import com.weiteng.tabview.sample.R;

/**
 * 测试 MenuTabView 控件
 *
 * @author weiTeng
 * @version v1.0.0
 * @since 2017/9/9
 */
public class MusicActivity extends AppCompatActivity {

    private MenuTabView mMenuTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        mMenuTabView = (MenuTabView) findViewById(R.id.tabview);
    }
}
