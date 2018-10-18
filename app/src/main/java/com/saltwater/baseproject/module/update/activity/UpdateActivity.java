package com.saltwater.baseproject.module.update.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saltwater.baseproject.R;
import com.saltwater.baseproject.base.BaseActivity;
import com.saltwater.baseproject.module.update.contract.UpdateContract;
import com.saltwater.baseproject.module.update.presenter.UpdatePresenter;
import com.saltwater2233.baselibrary.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdateActivity extends BaseActivity<UpdateContract.View, UpdatePresenter> implements UpdateContract.View {
    @BindView(R.id.btn_location)
    Button mBtnLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected UpdatePresenter createPresenter() {
        return new UpdatePresenter(this);
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void setUpdateInfo(String info) {
        hideLoadingDialog();
        ToastUtil.showShort(mContext, info);
        mTvLocation.setText(info);

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(mContext, msg);
    }

    @OnClick({R.id.btn_location, R.id.tv_location})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_location:
                showLoadingDialog();
                mPresenter.loadUpdate();
                break;
            case R.id.tv_location:
                finish();
                startActivity(new Intent(this, UpdateActivity.class));
                break;
        }
    }


}
