package com.small.lx_0116;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddSubView extends LinearLayout implements View.OnClickListener {
    private Button btn_add;
    private Button btn_decrease;
    private TextView tv_number;


    public AddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //填充的条目布局
        View rootView = LayoutInflater.from(context).inflate(R.layout.calculator_layout, this);
        btn_add = rootView.findViewById(R.id.btn_add);
        btn_decrease = rootView.findViewById(R.id.btn_decrease);
        tv_number = rootView.findViewById(R.id.tv_number);
        btn_add.setOnClickListener(this);
        btn_decrease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String numberString = tv_number.getText().toString();
        int number = Integer.parseInt(numberString);
        switch (v.getId()) {
            case R.id.btn_decrease:
                number = number - 1;
                if (number < 0) {
                    number = 0;
                    //最小数量为0
                    tv_number.setText(String.valueOf(number));
                }
                tv_number.setText(String.valueOf(number));
                //接口回调回传数字
                onCalCulatorLisenter.onDecrese(number);
                break;
            case R.id.btn_add:
                number = number + 1;
                tv_number.setText(String.valueOf(number));
                //接口回调回传数字
                onCalCulatorLisenter.onAdd(number);
                break;

        }
    }

    //接口回调
    private OnCalCulatorLisenter onCalCulatorLisenter;

    public interface OnCalCulatorLisenter {
        //减少
        public void onDecrese(int number);

        //增加
        public void onAdd(int number);
    }

    public void setOnCalCulatorLisenter(OnCalCulatorLisenter onCalCulatorLisenter) {
        this.onCalCulatorLisenter = onCalCulatorLisenter;
    }

}
