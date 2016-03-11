package com.weiteng.tabview.sample.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.weiteng.tabview.sample.R;
import com.weiteng.tabview.sample.base.BaseFragment;


/**
 * Created by weiTeng on 2015/12/7.
 */
public class FirstFragment extends BaseFragment {

    private static final String TAG = "FirstFragment";


    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_first;
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
        Log.d(TAG, " FirstFragment : reloadData()");
    }
}
