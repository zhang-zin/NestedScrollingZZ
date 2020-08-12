package com.zj.nestedscrollingdemo.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zj.nestedscrollingdemo.R;
import com.zj.nestedscrollingdemo.bean.DetailBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.zj.nestedscrollingdemo.bean.DetailBean.COUPON;
import static com.zj.nestedscrollingdemo.bean.DetailBean.FIRST_TITLE;
import static com.zj.nestedscrollingdemo.bean.DetailBean.PUBLISH;
import static com.zj.nestedscrollingdemo.bean.DetailBean.TITLE;
import static com.zj.nestedscrollingdemo.bean.DetailBean.VIP;

/**
 * @author zhang
 */
public class DetailAdapter extends BaseMultiItemQuickAdapter<DetailBean, BaseViewHolder> {

    public DetailAdapter() {
        super(getList());
        addItemType(FIRST_TITLE, R.layout.item_text_first_title_layout);
        addItemType(TITLE, R.layout.item_text_title_layout);
        addItemType(COUPON, R.layout.item_text_coupon_layout);
        addItemType(PUBLISH, R.layout.item_text_publish_layout);
        addItemType(VIP, R.layout.item_vip_layout);
    }

    private static List<DetailBean> getList() {
        ArrayList<DetailBean> list = new ArrayList<>();
        list.add(new DetailBean(FIRST_TITLE, "优惠"));
        list.add(new DetailBean("特价", "特价商品15.5元起"));
        list.add(new DetailBean("会员", "超级会员领7元无门槛红包"));
        list.add(new DetailBean("折扣", "折扣商品55着起"));
        list.add(new DetailBean("限时", "限时秒杀甜品饮料商品"));
        list.add(new DetailBean(TITLE, "公告"));
        list.add(new DetailBean("春节不打烊，金喜送到家。新品金凤来福鸡排堡、金尊肉酱厚牛堡上线，配上扭扭薯条，等您品尝！"));
        list.add(new DetailBean(TITLE, "店铺会员卡"));
        list.add(new DetailBean(VIP));
        return list;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, DetailBean detailBean) {
        switch (detailBean.getType()) {
            case FIRST_TITLE:
                helper.setText(R.id.tv_item_text_first_title, detailBean.getTitle());
                break;
            case TITLE:
                helper.setText(R.id.tv_item_text_title, detailBean.getTitle());
                break;
            case COUPON:
                helper.setText(R.id.tv_item_text_coupon_title, detailBean.getCouponTitle());
                helper.setText(R.id.tv_item_text_coupon_content, detailBean.getCouponContent());
                break;
            case PUBLISH:
                helper.setText(R.id.tv_item_text_publish, detailBean.getPublishContent());
                break;
            default:
                break;
        }
    }
}
