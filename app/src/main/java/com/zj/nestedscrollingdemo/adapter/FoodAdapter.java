package com.zj.nestedscrollingdemo.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zj.nestedscrollingdemo.R;

import java.util.List;

/**
 * @author 79810
 */
public class FoodAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public FoodAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_food_laypout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
    }
}
