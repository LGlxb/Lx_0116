package com.small.lx_0116;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DataCall, View.OnClickListener {

    @BindView(R.id.rv_business)
    RecyclerView rvBusiness;
    @BindView(R.id.srl_container)
    SmartRefreshLayout srlContainer;
    @BindView(R.id.cb_all)
    CheckBox cbAll;
    @BindView(R.id.btn_price)
    Button btnPrice;
    @BindView(R.id.tv_count)
    TextView tvCount;
    private CartPresenter cartPresenter;
    private AlertDialog alertDialog;
    private Context mContext;
    private BusinessAdapter adapter;
    private List<ShoppingCartBean.DataBean> cartBean;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //实例化P层
        cartPresenter = new CartPresenter(this);
        cartPresenter.showCart();
        //刷新加载
        srlContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                cartPresenter.showCart();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        srlContainer.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void showLoading() {
        alertDialog = new AlertDialog.Builder(mContext).setMessage("刷新中").create();
        alertDialog.show();
    }

    @Override
    public void hideLoading() {
        alertDialog.hide();
    }


    @Override
    public void onshowCart(ShoppingCartBean shoppingCartBean) {
        cbAll.setOnCheckedChangeListener(null);
        cbAll.setOnClickListener(this);
        //数据源
        cartBean = shoppingCartBean.getData();
        //设置布局管理器
        manager = new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false);
        rvBusiness.setLayoutManager(manager);
        //设置适配器
        adapter = new BusinessAdapter(R.layout.recycler_business_item,
                cartBean);
        rvBusiness.setAdapter(adapter);
        //TODO:外层条目接口回调
        adapter.setOnBusinessItemClickLisenter(new BusinessAdapter.OnBusinessItemClickLisenter() {
            @Override
            public void onCallBack() {
                boolean result = true;
                for (int i = 0; i < cartBean.size(); i++) {
                    //外层选中状态
                    boolean businessChecked = cartBean.get(i).getBusinessChecked();
                    result = result & businessChecked;
                    for (int j = 0; j < cartBean.get(i).getList().size(); j++) {
                        //里层选中状态
                        boolean goodsChecked = cartBean.get(i).getList().get(j).getGoodsChecked();
                        result = result & goodsChecked;
                    }
                }
                cbAll.setChecked(result);
                //计算总价
                calculateTotalCount();
            }
        });
    }

    @Override
    public void onshowFailure(String error) {

    }

    @Override
    public Context CONTEXT() {
        return this;
    }

    private void calculateTotalCount() {
        //对总价进行计算
        double totalCount = 0;
        //外层条目
        for (int i = 0; i < cartBean.size(); i++) {
            //内层条目
            for (int j = 0; j < cartBean.get(i).getList().size(); j++) {
                //判断内层条目是否勾选
                if (cartBean.get(i).getList().get(j).getGoodsChecked() == true) {
                    //获取商品数据*商品价格
                    double price = cartBean.get(i).getList().get(j).getPrice();
                    int defalutNumber = cartBean.get(i).getList().get(j).getDefalutNumber();
                    double goodsPrice = price * defalutNumber;
                    totalCount = totalCount + goodsPrice;
                }
            }
        }
        tvCount.setText("总价是：" + String.valueOf(totalCount));
    }

    @Override
    public void onClick(View v) {
        //全选逻辑的处理
        for (int i = 0; i < cartBean.size(); i++) {
            //首先让外层的商家条目全部选中
            cartBean.get(i).setBusinessChecked(cbAll.isChecked());
            //然后让里层的商品条目全部选中
            for (int j = 0; j < cartBean.get(i).getList().size(); j++) {
                cartBean.get(i).getList().get(j).setGoodsChecked(cbAll.isChecked());
            }
        }
        //计算总价
        calculateTotalCount();
        adapter.notifyDataSetChanged();
    }
}
