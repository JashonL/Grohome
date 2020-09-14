package com.growatt.grohome.module.service;

import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.service.presenter.CommonProblemPresenter;
import com.growatt.grohome.module.service.view.ICommonProblemView;

import butterknife.BindView;

public class CommonProblemActivity extends BaseActivity<CommonProblemPresenter> implements ICommonProblemView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


    @Override
    protected CommonProblemPresenter createPresenter() {
        return new CommonProblemPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_problem;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m317_common_problem);


    }

    @Override
    protected void initData() {
        try {
            presenter.readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showConfigText(String content) {
        tvContent.setText(content);
    }


}
