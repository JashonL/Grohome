package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.BulbSceneAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.customview.colorpicker.BlurMaskCircularView;
import com.growatt.grohome.customview.colorpicker.CircleBackgroundView;
import com.growatt.grohome.customview.colorpicker.ColorPicker;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.presenter.BulbPresenter;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BulbActivity extends BaseActivity<BulbPresenter> implements IBulbView, SeekBar.OnSeekBarChangeListener, ColorPicker.OnColorChangedListener, ColorPicker.OnColorSelectedListener , BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_white_light)
    ImageView ivWhiteLight;
    @BindView(R.id.iv_colour_light)
    ImageView ivColourLight;
    @BindView(R.id.iv_scenec_light)
    ImageView ivScenecLight;
    @BindView(R.id.v_bulb_backgroud_white)
    CircleBackgroundView vBulbBackgroudWhite;
    @BindView(R.id.seek_brightness_white)
    AppCompatSeekBar seekBrightnessWhite;
    @BindView(R.id.seek_temp_white)
    AppCompatSeekBar seekTempWhite;
    @BindView(R.id.seek_brightness_colour)
    AppCompatSeekBar seekBrightnessColour;
    @BindView(R.id.seek_temp_colour)
    AppCompatSeekBar seekTempColour;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.layout_white)
    View whiteClude;
    @BindView(R.id.layout_colour)
    View colourClude;
    @BindView(R.id.layout_scene)
    View sceneClude;
    @BindView(R.id.color_picker)
    ColorPicker colorPicker;
    @BindView(R.id.white_mask_view)
    BlurMaskCircularView whiteMaskView;
    @BindView(R.id.rlv_scene)
    RecyclerView rlvScene;
    @BindView(R.id.v_bulb_background_scene)
    View sceneBackGround;

    private BulbSceneAdapter mBulbSceneAdapter;

    @Override
    protected BulbPresenter createPresenter() {
        return new BulbPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bulb;
    }

    @Override
    protected void initViews() {
        //设置头部
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.nocolor));
        tvTitle.setTextColor(ContextCompat.getColor(this,R.color.white));
        showViewsByTab(DeviceBulb.BULB_MODE_WHITE);
        ivSwitch.setImageResource(R.drawable.icon_on);
        //场景列表
        rlvScene.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mBulbSceneAdapter=new BulbSceneAdapter(R.layout.item_bulb_scene,new ArrayList<>());
        rlvScene.setAdapter(mBulbSceneAdapter);
        rlvScene.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 30, ContextCompat.getColor(this, R.color.nocolor)));
    }

    @Override
    protected void initData() {
        List<BulbSceneBean> bulbSceneBeans = presenter.initScene();
        mBulbSceneAdapter.replaceData(bulbSceneBeans);
        sceneBackGround.setBackgroundResource(R.drawable.sence_night);//默认选中第一个
        presenter.initDevice();

    }

    @Override
    public void setDeviceTitle(String devName) {
        tvTitle.setText(devName);
    }

    @Override
    public void setOnoff(String onoff) {
        if ("true".equals(onoff)) {//状态开启
            ivSwitch.setImageResource(R.drawable.icon_on);
        } else {//关闭
            ivSwitch.setImageResource(R.drawable.icon_off);
        }
    }

    @Override
    public void setBright(String bright) {
        if (!TextUtils.isEmpty(bright)) {
            try {
                int brightValue = Integer.parseInt(bright) - 10;
                Log.i(TuyaApiUtils.TUYA_TAG,"白光亮度："+brightValue);
                seekBrightnessWhite.setProgress(brightValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setColour(int color) {
        colorPicker.setColor(color);
    }

    @Override
    public void set(String controdata) {

    }

    @Override
    public void setCuntDown(String countdown) {

    }

    @Override
    public void setScene(String scene) {
        //解析返回的场景设置ui
        if (!TextUtils.isEmpty(scene)&&scene.length()>2){
            String number = scene.substring(0, 2);
            int id = CommentUtils.hexStringToInter(number);
            mBulbSceneAdapter.setNowSelectPosition(id);
            Integer integer = DeviceBulb.getSceneDefultPicRes().get(id);
            sceneBackGround.setBackgroundResource(integer);
        }
    }

    @Override
    public void setMode(String mode) {
        showViewsByTab(mode);
    }

    @Override
    public void setTemp(String temp) {
        if (!TextUtils.isEmpty(temp)) {
            try {
                int brightValue = Integer.parseInt(temp);
                Log.i(TuyaApiUtils.TUYA_TAG,"白光冷暖值："+brightValue);
                int mProgree = seekTempWhite.getMax() - brightValue;
                seekTempWhite.setProgress(mProgree);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setControData(String controdata) {

    }

    @Override
    public void setSatProgress(int progress) {
        seekTempColour.setProgress(progress);

    }

    @Override
    public void setVatProgress(int progress) {
        seekBrightnessColour.setProgress(progress);
    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {
        String msg = getString(R.string.m151_send_dps_failed) + ":" + code + "--->" + error;
        MyToastUtils.toast(msg);
    }

    @Override
    public void setCenterColor(int color) {
        colorPicker.setNewCenterColor(color);
    }

    @Override
    public void setWhiteBgColor(int color) {
        vBulbBackgroudWhite.setColor(color);
    }

    @Override
    public void setWhiteMaskView(int color) {
        whiteMaskView.setColor(color);

    }

    @Override
    public void setColourMaskView(int color) {
    }

    public void showViewsByTab(String mode) {
        if (DeviceBulb.BULB_MODE_WHITE.equals(mode)) {//白光模式
            ivWhiteLight.setSelected(true);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.VISIBLE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.GONE);
        } else if (DeviceBulb.BULB_MODE_COLOUR.equals(mode)) {//彩光模式
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(true);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.VISIBLE);
            sceneClude.setVisibility(View.GONE);
        } else {
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(true);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.iv_white_light, R.id.iv_colour_light, R.id.iv_scenec_light, R.id.iv_switch,R.id.iv_edit,R.id.tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_white_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_WHITE);
                break;
            case R.id.iv_colour_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_COLOUR);
                break;
            case R.id.iv_scenec_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_SCENE);
                break;
            case R.id.iv_switch:
                presenter.bulbSwitch();
                break;
            case R.id.iv_edit:
            case R.id.tv_edit:
                presenter.toEditScene();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        seekTempWhite.setOnSeekBarChangeListener(this);
        seekBrightnessWhite.setOnSeekBarChangeListener(this);
        seekBrightnessColour.setOnSeekBarChangeListener(this);
        seekTempColour.setOnSeekBarChangeListener(this);
        colorPicker.setOnColorChangedListener(this);//不松手
        colorPicker.setOnColorSelectedListener(this);//松手
        mBulbSceneAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //亮度调节
        if (seekBar == seekBrightnessWhite) {
            presenter.bulbBrightness(progress + 10);
        }
        //温度调节
        if (seekBar == seekTempWhite) {
            int mProgree = seekTempWhite.getMax() - progress;
            presenter.bulbTemper(mProgree);
        }
        //饱和度调节
        if (seekBar == seekBrightnessColour) {
            presenter.bulbColourVal(progress);
        }

        if (seekBar == seekTempColour) {
            presenter.bulbColourSat(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onColorChanged(int color) {
        presenter.bulbColour(color);
    }

    @Override
    public void onColorSelected(int color) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BulbSceneBean bulbSceneBean = mBulbSceneAdapter.getData().get(position);
        presenter.bulbScene(bulbSceneBean.getSceneValue());
    }
}