package com.smart.lamp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.SensorInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;
import com.smart.lamp.net.util.RetrofitCallBack;
import com.smart.lamp.net.util.BusinessManager;
import com.smart.lamp.util.DataCache;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 6/3/2019 1:05 PM
 */
public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {
    // 设备标识
    private static final String DEVICE_ID = "26255";
    // 眼睛距离 api tag
    private static final String DESKTOP_TO_EYE_API_TAG = "desktop_to_eye";
    // 眼睛距离 api tag
    private static final String DESKTOP_TO_BODY_API_TAG = "desktop_to_body";
    // 蜂鸣器 api tag
    private static final String BUZZER_API_TAG = "alarm";
    // 亮度调节 api tag
    private static final String INTENSITY_CONTROL_API_TAG = "intensity_control";


    private BusinessManager businessManager;
    private TextView desktopToEyeText;
    private TextView desktopToBodyText;
    private TextView safeBodyDisEdit;
    private TextView safeEyeDisEdit;

    private Vibrator vibrator;
    //记录是否有首次按键
    private static boolean mBackKeyPressed = false;


    private GetDeviceInfoHandler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDeviceInfo();
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreateLifecycle(Bundle saveInstanceState) {
        super.onCreateLifecycle(saveInstanceState);
        businessManager = new BusinessManager(DataCache.getAccessToken(getApplicationContext()));
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    protected int setLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        desktopToEyeText = findViewById(R.id.tv_desk_to_eye);
        desktopToBodyText = findViewById(R.id.tv_desk_to_body);
        safeBodyDisEdit = findViewById(R.id.et_safe_body_dis);
        safeEyeDisEdit = findViewById(R.id.et_safe_eye_dis);
        TextView saveText = findViewById(R.id.tv_save);
        saveText.setOnClickListener(this);
        RadioGroup luminanceGroup = findViewById(R.id.rg_luminance);
        luminanceGroup.setOnCheckedChangeListener(this);

        float safeEyeDis = DataCache.getSafeEyeDistance(this);
        float safeBodyDis = DataCache.getSafeBodyDistance(this);
        DataCache.updateSafeEyeDistance(this, safeEyeDis == 0f ? 45 : safeEyeDis);
        DataCache.updateSafeBodyDistance(this, safeBodyDis == 0f ? 45 : safeBodyDis);
        safeEyeDisEdit.setText(String.format("%s", DataCache.getSafeEyeDistance(this)));
        safeBodyDisEdit.setText(String.format("%s", DataCache.getSafeBodyDistance(this)));
    }

    @Override
    protected void initData() {
        getDeviceInfo();
    }

    @Override
    protected void registerListener() {
        handler = new GetDeviceInfoHandler();
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    private static class GetDeviceInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private void getDeviceInfo() {
        businessManager.getDeviceInfo(DEVICE_ID,
                new RetrofitCallBack<BaseResponseEntity<DeviceInfo>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<DeviceInfo> response) {
                        if (response.getStatus() == 0) {
                            List<SensorInfo> sensors = response.getResultObj().Sensors;
                            for (int i = 0; i < sensors.size(); i++) {
                                SensorInfo info = sensors.get(i);
                                if (info.ApiTag.equals(DESKTOP_TO_EYE_API_TAG)) {
                                    updateEyeDisState(info);
                                } else if (info.ApiTag.equals(DESKTOP_TO_BODY_API_TAG)) {
                                    updateBodyDisState(info);
                                }
                            }
                        }
                    }
                });
    }

    private void updateEyeDisState(SensorInfo info) {
        String value = info.Value;
        desktopToEyeText.setTextColor(getResources().getColor(R.color.green));
        if (TextUtils.isEmpty(value)) {
            desktopToEyeText.setText(R.string.unknown);
        } else {
            desktopToEyeText.setText(info.Value);
            float val;
            try {
                val = Float.parseFloat(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                val = 0f;
            }
            if (val < DataCache.getSafeEyeDistance(HomeActivity.this)) {
                desktopToEyeText.setTextColor(getResources().getColor(R.color.red));
                vibrator(R.id.tv_desk_to_eye);
            }
        }
    }

    private void updateBodyDisState(SensorInfo info) {
        String value = info.Value;
        desktopToBodyText.setTextColor(getResources().getColor(R.color.green));
        if (TextUtils.isEmpty(value)) {
            desktopToBodyText.setText(R.string.unknown);
        } else {
            desktopToBodyText.setText(info.Value);
            float val;
            try {
                val = Float.parseFloat(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                val = 0f;
            }
            if (val < DataCache.getSafeBodyDistance(HomeActivity.this)) {
                desktopToBodyText.setTextColor(getResources().getColor(R.color.red));
                vibrator(R.id.tv_desk_to_body);
            }
        }
    }

    /**
     * 执行振动
     * @param type 类型：眼睛距离 / 身体距离
     */
    private void vibrator(int type) {
        if (vibrator == null) {
            return;
        }
        vibrator.cancel();
        // 振动300毫秒
        long[] patter = {0, 300, 100, 300};
        vibrator.vibrate(patter,-1);
        Toast.makeText(this,
                type == R.id.tv_desk_to_eye ?
                        getString(R.string.eye_too_close) : getString(R.string.body_too_close),
                Toast.LENGTH_SHORT).show();

        //振动的同时，向云平台发起报警指令；0：不报警；1：报警
        businessManager.control(DEVICE_ID, BUZZER_API_TAG, 1,
                new RetrofitCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                    }
                });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int intensity = checkedId == R.id.rb_low ? 1 : (checkedId == R.id.rb_middle ? 2 : 3);
        businessManager.control(DEVICE_ID, INTENSITY_CONTROL_API_TAG, intensity,
                new RetrofitCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Toast.makeText(HomeActivity.this,
                                R.string.toast_intensity_control_succeed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                String safeEyeDis = safeEyeDisEdit.getText().toString();
                String safeBodyDis = safeBodyDisEdit.getText().toString();
                if (TextUtils.isEmpty(safeEyeDis)) {
                    Toast.makeText(this, R.string.please_input_safe_eye_dis, Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(safeBodyDis)) {
                    Toast.makeText(this, R.string.please_input_safe_body_dis, Toast.LENGTH_SHORT).show();
                }
                DataCache.updateSafeEyeDistance(this,
                        Float.parseFloat(safeEyeDis));
                DataCache.updateSafeBodyDistance(this,
                        Float.parseFloat(safeBodyDis));
                Toast.makeText(this, R.string.toast_save_successful, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override

                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
            System.exit(0);
        }
}
}