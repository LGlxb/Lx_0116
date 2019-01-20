package com.small.lx_0116;


import java.lang.ref.SoftReference;

public abstract class BasePresenter<V extends IView> {
    protected V view;
    private final SoftReference<V> vSoftReference;

    public BasePresenter(V view) {
        //软引用包裹
        vSoftReference = new SoftReference<>(view);
        this.view = view;
        initModel();
    }

    protected abstract void initModel();

    void onDestroy() {
        vSoftReference.clear();
        view = null;  //减少内存溢出时调用
    }
}
