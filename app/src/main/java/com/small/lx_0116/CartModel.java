package com.small.lx_0116;

import com.google.gson.Gson;

public class CartModel {
    public void ShopCart(final CartModelCallBack cartModelCallBack) {
        HttpUtils httpsUtils = HttpUtils.getHttpUtils();
        httpsUtils.doGet(Constant.SHOPPINGCART_URL, new HttpUtils.IOKHttpUtilsCallBack() {
            @Override
            public void onFailure(String error) {
                cartModelCallBack.getFaid(error);
            }

            @Override
            public void onResponse(String json) {
                ShoppingCartBean shoppingCartBean = new Gson().fromJson(json, ShoppingCartBean
                        .class);
                if (shoppingCartBean.getCode().equals("0")) {
                    cartModelCallBack.getSuccess(shoppingCartBean);
                } else {
                    cartModelCallBack.getFaid("未加载");
                }
            }
        });

    }

    //接口回调
    public interface CartModelCallBack {
        void getSuccess(ShoppingCartBean shoppingCartBean);

        void getFaid(String error);
    }
}
