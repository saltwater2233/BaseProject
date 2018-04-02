package com.saltwater.baseprojectmvp.module.location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saltwater.baseprojectmvp.R;
import com.saltwater.baseprojectmvp.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdateActivity extends BaseActivity implements UpdateContract.View {
    @BindView(R.id.btn_location)
    Button mBtnLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;

    UpdatePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new UpdatePresenter(this);
    }

    @Override
    public void subscribePresenter() {
        mPresenter.subscribe();
    }

    @Override
    public void unsubscribePresenter() {
        mPresenter.unsubscribe();
    }


    @Override
    public void showMsg(String name) {
        mTvLocation.setText(name);
    }

    @OnClick({R.id.btn_location, R.id.tv_location})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_location:
                mPresenter.loadUpdate();
                break;
            case R.id.tv_location:
                mTvLocation.setText("click");
                break;
        }
    }

}
