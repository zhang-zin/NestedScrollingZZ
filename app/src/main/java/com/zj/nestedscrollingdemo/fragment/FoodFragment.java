package com.zj.nestedscrollingdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zj.nestedscrollingdemo.R;
import com.zj.nestedscrollingdemo.adapter.FoodAdapter;

import java.util.ArrayList;

/**
 * @author zhang
 */
public class FoodFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FoodAdapter mAdater;
    private ArrayList<Integer> mDatas = new ArrayList<>();
    private View mFooterView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_layout, container, false);
        mFooterView = inflater.inflate(R.layout.item_food_footer_layout, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initEvent() {
        mAdater.addFooterView(mFooterView);
        mRecyclerView.setAdapter(mAdater);
//        mAdater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ((MyElemeActivity) getActivity()).showFoodLayout();
//            }
//        });
    }

    private void initData() {
        for (int i = 0; i < 32; i++) {
            mDatas.add(i);
        }
        mAdater = new FoodAdapter(mDatas);
    }
}
