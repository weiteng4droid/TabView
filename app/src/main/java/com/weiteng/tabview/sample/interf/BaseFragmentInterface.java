package com.weiteng.tabview.sample.interf;

import android.os.Bundle;
import android.view.View;

/**
 * Created by weiTeng on 2015/12/7.
 */
public interface BaseFragmentInterface {

    void initView(View view);

    void initData(Bundle saveInstanceState);

    void reloadData();

    void release();
}
