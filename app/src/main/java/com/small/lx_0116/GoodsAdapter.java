package com.small.lx_0116;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<ShoppingCartBean.DataBean.ListBean,
        BaseViewHolder> {
    //接口回调
    private OnGoodsItemClckListener onGoodsItemClckListener;

    public interface OnGoodsItemClckListener {
        void onCallBack();
    }

    public void setOnGoodsItemClckListener(OnGoodsItemClckListener onGoodsItemClckListener) {
        this.onGoodsItemClckListener = onGoodsItemClckListener;
    }

    //*******************
    public GoodsAdapter(int layoutResId, @Nullable List<ShoppingCartBean.DataBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShoppingCartBean.DataBean.ListBean item) {
        //条目数据设置
        helper.setText(R.id.tv_goodsPrice, "￥：" + item.getPrice());
        helper.setText(R.id.tv_goodsTitle, item.getTitle());
        //图片
        ImageView iv_goodsIcon = helper.getView(R.id.iv_goodsIcon);
        String imagesString = item.getImages();
        //接口截取
        String[] imagesStr = imagesString.split("\\|");
        Glide.with(mContext).load(imagesStr[0]).into(iv_goodsIcon);
        //避免焦点抢占
        CheckBox cb_goods = helper.getView(R.id.cb_goods);
        cb_goods.setOnCheckedChangeListener(null);
        cb_goods.setChecked(item.getGoodsChecked());
        //接口方式传状态给外层
        cb_goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //更新bean对象
                item.setGoodsChecked(isChecked);
                //Todo 传递给外层
                onGoodsItemClckListener.onCallBack();
            }
        });
        //加减器
        AddSubView calculatorView = helper.getView(R.id.cv_calculatorView);
        //Todo 加减回调
        calculatorView.setOnCalCulatorLisenter(new AddSubView.OnCalCulatorLisenter() {
            @Override
            public void onDecrese(int number) {
                //对新增的字段进行改动
                item.setDefalutNumber(number);
                //Todo 将数量回传给外部
                onGoodsItemClckListener.onCallBack();
            }

            @Override
            public void onAdd(int number) {
                //对新增的字段进行改动
                item.setDefalutNumber(number);
                onGoodsItemClckListener.onCallBack();
            }
        });
    }
}
