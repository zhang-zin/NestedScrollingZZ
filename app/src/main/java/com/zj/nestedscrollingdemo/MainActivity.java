package com.zj.nestedscrollingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.zj.nestedscrollingdemo.adapter.DetailAdapter;
import com.zj.nestedscrollingdemo.databinding.ActivityMainBinding;
import com.zj.nestedscrollingdemo.fragment.FoodFragment;
import com.zj.nestedscrollingdemo.fragment.TabFragment;

import java.util.ArrayList;

/**
 * @author zhang
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private DetailAdapter detailAdapter;
    private MyFragmentAdapter mFragmentAdapter;

    private final String[] mTitles = {
            "点餐", "评价", "商家"
    };
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        initData();
        initView();
    }

    private void initData() {
        detailAdapter = new DetailAdapter();
        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mFragments.add(new FoodFragment());
        mFragments.add(TabFragment.newInstance("我是评价页面"));
        mFragments.add(TabFragment.newInstance("我是商家页面"));
    }

    private void initView() {
        binding.rvCollasped.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCollasped.setAdapter(detailAdapter);
        binding.vp.setAdapter(mFragmentAdapter);
        binding.stl.setViewPager(binding.vp, mTitles);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        MyFragmentAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}