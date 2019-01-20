package com.small.lx_0116;


public interface DataCall extends IView {
    void onshowCart(ShoppingCartBean shoppingCartBean);

    void onshowFailure(String error);

    void showLoading();

    void hideLoading();
}
