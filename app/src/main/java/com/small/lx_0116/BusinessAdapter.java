package com.small.lx_0116;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BusinessAdapter extends BaseQuickAdapter<ShoppingCartBean.DataBean, BaseViewHolder> {
    /*接口回调*/
    OnBusinessItemClickLisenter onBusinessItemClickLisenter;

    public interface OnBusinessItemClickLisenter {
        public void onCallBack();
    }

    public void setOnBusinessItemClickLisenter(OnBusinessItemClickLisenter
                                                       onBusinessItemClickLisenter) {
        this.onBusinessItemClickLisenter = onBusinessItemClickLisenter;
    }

    //**********************
    public BusinessAdapter(int layoutResId, @Nullable List<ShoppingCartBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShoppingCartBean.DataBean item) {
        //设置商家名称
        helper.setText(R.id.tv_business_name, item.getSellerName());

        //设置商家下的子商品条目
        RecyclerView rv_goods = helper.getView(R.id.rv_goods);
        //避免焦点抢占
        final CheckBox cb_business = helper.getView(R.id.cb_business);
        cb_business.setOnCheckedChangeListener(null);
        //获取商家条目是否选中
        cb_business.setChecked(item.getBusinessChecked());
        //子商品条目数据源
        List<ShoppingCartBean.DataBean.ListBean> goodsList = item.getList();
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false);
        rv_goods.setLayoutManager(manager);
        //设置子条目适配器
        final GoodsAdapter goodsAdapter = new GoodsAdapter(R.layout.recycler_goods_item, goodsList);
        rv_goods.setAdapter(goodsAdapter);
        //内部条目复选框更新外部条目联动
        //Todo:内部条目调用
        goodsAdapter.setOnGoodsItemClckListener(new GoodsAdapter.OnGoodsItemClckListener() {
            @Override
            public void onCallBack() {
                //遍历获取所有子条目，只要有一个未勾选，商品类别也应该是未勾选
                 boolean result = true;
                for (int i = 0; i < item.getList().size(); i++) {
                    result = result & item.getList().get(i).getGoodsChecked();
                }
                cb_business.setChecked(result);
                //刷新适配器
                goodsAdapter.notifyDataSetChanged();
                //最终状态回传
                onBusinessItemClickLisenter.onCallBack();
            }
        });
        //外部条目复选框更新内部条目联动
        cb_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取商品类别勾选状态
                //外层商品类别条目获取的关键：cb_business.isChecked()
                for (int i = 0; i < item.getList().size(); i++) {
                    item.getList().get(i).setGoodsChecked(cb_business.isChecked());
                }
                item.setBusinessChecked(cb_business.isChecked());
                notifyDataSetChanged();
                //把最后的状态进行回传
                onBusinessItemClickLisenter.onCallBack();
            }
        });
    }
}
