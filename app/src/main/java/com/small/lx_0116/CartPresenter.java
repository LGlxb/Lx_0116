package com.small.lx_0116;

public class CartPresenter extends BasePresenter<DataCall> {

    private CartModel cartModel;

    public CartPresenter(DataCall view) {
        super(view);
    }

    @Override
    protected void initModel() {
        cartModel = new CartModel();
    }

    public void showCart() {
        cartModel.ShopCart(new CartModel.CartModelCallBack() {
            @Override
            public void getSuccess(ShoppingCartBean shoppingCartBean) {
                view.onshowCart(shoppingCartBean);
            }

            @Override
            public void getFaid(String error) {
                view.onshowFailure(error);
            }
        });
    }
}
