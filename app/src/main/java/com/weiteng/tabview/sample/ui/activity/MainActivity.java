package com.weiteng.tabview.sample.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.weiteng.tabview.TabView;
import com.weiteng.tabview.sample.R;
import com.weiteng.tabview.sample.base.BaseFragment;
import com.weiteng.tabview.sample.ui.fragment.FirstFragment;
import com.weiteng.tabview.sample.ui.fragment.MineFrgment;
import com.weiteng.tabview.sample.ui.fragment.SecondFragment;
import com.weiteng.tabview.sample.ui.fragment.ThirdFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by weiTeng on 2015/12/7.
 */
public class MainActivity extends AppCompatActivity implements TabView.OnTabClickListener{

    @InjectView(R.id.tabview)
    TabView mTabView;

    private List<BaseFragment> mFragments;
    private FragmentManager mFragmentManager;
    private int mIndex = 0;
    private BaseFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mFragmentManager = getSupportFragmentManager();

        mTabView.setStateListDrawables(R.drawable.home_insruance_icon, R.drawable.home_msg_icon,
                R.drawable.home_service_icon, R.drawable.home_mine_icon);
        mTabView.setDrawableWidth(getResources().getDimensionPixelOffset(R.dimen.w_dp_28));
        mTabView.setCurrentIndex(mIndex);
        mTabView.setOnTabClickListener(this);
        mTabView.showNoticePoint(true);
        mTabView.showNoticePointAtPosition(0, true);
        mTabView.showNoticePointAtPosition(2, true);

        initAllFragment();

        mTabView.setOnTabClickListener(this);
    }

    private void initAllFragment() {
        if(mFragments == null){
            mFragments = new ArrayList<>();
        }

        mFragments.add(new FirstFragment());
        mFragments.add(new SecondFragment());
        mFragments.add(new ThirdFragment());
        mFragments.add(new MineFrgment());

        mCurrentFragment = mFragments.get(mIndex);
        mFragmentManager.beginTransaction().replace(R.id.contentPanel, mCurrentFragment).commit();
    }

    @Override
    public void onTabClick(int index) {
        mIndex = index;
        switchContent(mFragments.get(mIndex));

        Toast.makeText(this, mTabView.getCurrentItemText(), Toast.LENGTH_SHORT).show();
    }

    public void switchContent(BaseFragment to) {
        if (mCurrentFragment != to) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mCurrentFragment).add(R.id.contentPanel, to).commit();
            } else {

                transaction.hide(mCurrentFragment).show(to).commit();
            }
            mCurrentFragment = to;
        }
    }
}
