package com.weiteng.tabview.sample.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.weiteng.tabview.SegmentView;
import com.weiteng.tabview.sample.R;
import com.weiteng.tabview.sample.base.BaseFragment;


/**
 * Created by weiTeng on 2015/12/7.
 */
public class FirstFragment extends BaseFragment {

    private static final String TAG = "FirstFragment";
    private SegmentView mSegmentView;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_first;
    }

    @Override
    public void initView(View view) {
        mSegmentView = (SegmentView) view.findViewById(R.id.segment_view);
    }

    @Override
    public void initData(Bundle saveInstaceState) {
        mSegmentView.setCurrentIndex(0);
        final String[] texts = mSegmentView.getTexts();
        mSegmentView.setOnSegmentItemClickListener(new SegmentView.OnSegmentItemClickListener() {

            @Override
            public void onSegmentItemClick(int index) {
                Toast.makeText(mActivity, texts[index], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void reloadData() {
        super.reloadData();
        Log.d(TAG, " FirstFragment : reloadData()");
    }
}
