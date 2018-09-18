package com.saltwater.baseproject.module.update.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saltwater.baseproject.R;
import com.saltwater.baseproject.base.BaseActivity;
import com.saltwater.baseproject.base.BasePresenter;
import com.saltwater.baseproject.module.update.contract.UpdateContract;
import com.saltwater.baseproject.module.update.presenter.UpdatePresenter;
import com.saltwater2233.baselibrary.utils.ImageTakeUtil;
import com.saltwater2233.baselibrary.utils.PermissionUtil;

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
        return new UpdatePresenter(this, this);
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void setUpdateInfo(String info) {
        hideLoading();
        mTvLocation.setText(info);
    }

    @OnClick({R.id.btn_location, R.id.tv_location})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_location:
                showLoading();
                mPresenter.loadUpdate();
                break;
            case R.id.tv_location:
                PermissionUtil.getPermissionAll(this, new PermissionUtil.PermissionsResultListener() {
                    @Override
                    public void onSuccessful() {
                        ImageTakeUtil.selectImage(UpdateActivity.this, 0);
                    }

                    @Override
                    public void onFailure() {

                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

                break;
        }
    }

}
