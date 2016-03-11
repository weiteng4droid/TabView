package com.weiteng.tabview.sample.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.weiteng.tabview.sample.R;
import com.weiteng.tabview.sample.base.BaseFragment;

/**
 * Created by weiTeng on 2015/12/7.
 */
public class SecondFragment extends BaseFragment {

    private static final String TAG = "SecondFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_second;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData(Bundle saveInstaceState) {

    }

    @Override
    public void reloadData() {
        super.reloadData();
        Log.d(TAG, " SecondFragment : reloadData()");
    }
}
