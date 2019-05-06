package com.smart.lamp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fatto.mpandroidchart.charts.LineChart;
import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.SensorDataRecord;
import com.smart.lamp.net.response.SensorInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;
import com.smart.lamp.net.util.RetrofitCallBack;
import com.smart.lamp.net.util.BusinessManager;
import com.smart.lamp.util.ChartUtil;
import com.smart.lamp.util.DataCache;
import com.smart.lamp.view.ChartMarkerView;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

/**
 * TODO Home page
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 6/3/2019 1:05 PM
 */
public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener {
    /**
     * 设备标识
     */
    private static final String DEVICE_ID = "26255";
    /**
     * 眼睛距离 api tag
     */
    public static final String DESKTOP_TO_EYE_API_TAG = "desktop_to_eye";
    /**
     * 眼睛距离 api tag
     */
    public static final String DESKTOP_TO_BODY_API_TAG = "desktop_to_body";
    /**
     * 蜂鸣器 api tag
     */
    private static final String BUZZER_API_TAG = "alarm";
    /**
     * 亮度调节 api tag
     */
    private static final String INTENSITY_CONTROL_API_TAG = "intensity_control";
    /**
     * 交易管理类实例
     */
    private BusinessManager businessManager;
    /**
     * 桌面与眼睛距离文本控件
     */
    private TextView desktopToEyeText;
    /**
     * 桌面与眼睛距离文本控件
     */
    private TextView desktopToBodyText;
    /**
     * 桌面与眼睛安全距离输入框
     */
    private EditText safeEyeDisEdit;
    /**
     * 桌面与身体安全距离输入框
     */
    private EditText safeBodyDisEdit;
    /**
     * 振动器实例
     */
    private Vibrator vibrator;
    /**
     * 记录是否有首次按键
     */
    private static boolean mBackKeyPressed = false;
    /**
     * 获取传感器数据处理实例
     */
    private GetDeviceInfoHandler handler;
    /**
     * 轮询获取传感器数据子线程 Runnable
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDeviceInfo();
            handler.postDelayed(this, 3000);
        }
    };
    /**
     * 折线图表控件
     */
    private LineChart chart;

    @Override
    protected void onCreateLifecycle(Bundle saveInstanceState) {
        super.onCreateLifecycle(saveInstanceState);
        businessManager = new BusinessManager(DataCache.getAccessToken());
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    /**
     * 设置界面布局
     *
     * @return 界面布局 id
     */
    @Override
    protected int setLayoutRes() {
        return R.layout.activity_home;
    }

    /**
     * 初始化
     */
    @Override
    protected void init() {
        // 找到控件
        chart = findViewById(R.id.chart_line);
        desktopToEyeText = findViewById(R.id.tv_desk_to_eye);
        desktopToBodyText = findViewById(R.id.tv_desk_to_body);
        safeBodyDisEdit = findViewById(R.id.et_safe_body_dis);
        safeEyeDisEdit = findViewById(R.id.et_safe_eye_dis);
        TextView saveText = findViewById(R.id.tv_save);
        saveText.setOnClickListener(this);
        RadioGroup luminanceGroup = findViewById(R.id.rg_luminance);
        luminanceGroup.setOnCheckedChangeListener(this);
        // 默认选中展示“桌面与眼睛”的数据报表
        RadioGroup chartsGroup = findViewById(R.id.rg_charts);
        chartsGroup.setOnCheckedChangeListener(this);
        chartsGroup.check(R.id.rb_desk_to_eye);
        // 获取保存的安全距离并设置到输入框中
        float safeEyeDis = DataCache.getSafeEyeDistance();
        float safeBodyDis = DataCache.getSafeBodyDistance();
        DataCache.updateSafeEyeDistance(safeEyeDis == 0f ? 45 : safeEyeDis);
        DataCache.updateSafeBodyDistance(safeBodyDis == 0f ? 45 : safeBodyDis);
        safeEyeDisEdit.setText(String.format("%s", DataCache.getSafeEyeDistance()));
        safeBodyDisEdit.setText(String.format("%s", DataCache.getSafeBodyDistance()));
        // 获取设备信息
        getDeviceInfo();
        // 设置图表无数据提示文字
        chart.setNoDataText("报表数据加载中,请耐心等候...");
        // 创建并执行实时查询任务
        handler = new GetDeviceInfoHandler();
        handler.post(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 释放振动器
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        // 释放 handler
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    /**
     * 获取设备信息
     */
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

    /**
     * 刷新桌面与眼睛的距离
     *
     * @param info 传感器数据
     */
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
            // 小于安全距离就报警，距离标红
            if (val < DataCache.getSafeEyeDistance()) {
                desktopToEyeText.setTextColor(getResources().getColor(R.color.red));
                vibrator(R.id.tv_desk_to_eye);
            }
        }
    }

    /**
     * 刷新桌面与身体的距离
     *
     * @param info 传感器数据
     */
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
            // 小于安全距离就报警，距离标红
            if (val < DataCache.getSafeBodyDistance()) {
                desktopToBodyText.setTextColor(getResources().getColor(R.color.red));
                vibrator(R.id.tv_desk_to_body);
            }
        }
    }

    /**
     * 执行振动
     *
     * @param type 类型：眼睛距离 / 身体距离
     */
    private void vibrator(int type) {
        if (vibrator == null) {
            return;
        }
        vibrator.cancel();
        // 振动300毫秒
        long[] patter = {0, 300, 100, 300};
        vibrator.vibrate(patter, -1);
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
        // 亮度选项
        if (group.getId() == R.id.rg_luminance) {
            int intensity = checkedId == R.id.rb_low ? 1 : (checkedId == R.id.rb_middle ? 2 : 3);
            businessManager.control(DEVICE_ID, INTENSITY_CONTROL_API_TAG, intensity,
                    new RetrofitCallBack<BaseResponseEntity>(getApplicationContext()) {
                        @Override
                        protected void onResponse(BaseResponseEntity response) {
                            Toast.makeText(HomeActivity.this,
                                    R.string.toast_intensity_control_succeed, Toast.LENGTH_SHORT).show();
                        }
                    });
            // 图表选项
        } else if (group.getId() == R.id.rg_charts) {
            switchChart(checkedId == R.id.rb_desk_to_eye ? DESKTOP_TO_EYE_API_TAG :
                    DESKTOP_TO_BODY_API_TAG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:  // 保存
                String safeEyeDis = safeEyeDisEdit.getText().toString();
                String safeBodyDis = safeBodyDisEdit.getText().toString();
                if (TextUtils.isEmpty(safeEyeDis)) {
                    Toast.makeText(this, R.string.please_input_safe_eye_dis, Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(safeBodyDis)) {
                    Toast.makeText(this, R.string.please_input_safe_body_dis, Toast.LENGTH_SHORT).show();
                }
                DataCache.updateSafeEyeDistance(Float.parseFloat(safeEyeDis));
                DataCache.updateSafeBodyDistance(Float.parseFloat(safeBodyDis));
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

    /**
     * 切换图表
     *
     * @param apiTag 传感器 TAG
     */
    private void switchChart(String apiTag) {
        // 显示加载进度框
        showLoading();
        businessManager.getSensorData(DEVICE_ID,    // 设备ID
                apiTag, // 传感标识名
                "2",    // 查询方式（1：XX分钟内 2：XX小时内 3：XX天内 4：XX周内 5：XX月内 6：按startDate与endDate指定日期查询）
                "20",   // 与Method一起配对使用（当Method=1~5时），表示以现在起"多长时间范围内"的数据，例：(Method=2,TimeAgo=30)
                // 表示现在起30小时内的历史数据
                "", // 起始时间（可选，格式YYYY-MM-DD HH:mm:ss）
                "", // 结束时间（可选，格式YYYY-MM-DD HH:mm:ss）
                "DESC", // 时间排序方式，DESC:倒序，ASC升序
                "20",   // 指定每次要请求的数据条数，默认20，最多3000
                "1",    // 指定页码
                new RetrofitCallBack<BaseResponseEntity<SensorDataRecord>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorDataRecord> response) {
                        dismissLoading();
                        if (response != null && response.getResultObj() != null) {
                            // 拿到云平台返回的传感器历史数据集合
                            List<SensorDataRecord.DataPoint> dataPoints =
                                    response.getResultObj().DataPoints;
                            if (dataPoints != null && dataPoints.get(0) != null) {
                                SensorDataRecord.DataPoint point = dataPoints.get(0);
                                List<SensorDataRecord.VR> resultDataPoints = point.PointDTO;
                                if (resultDataPoints.size() > 0) {
                                    Collections.reverse(resultDataPoints);
                                    // 根据阈值，设置 Y 轴最大值以及递增粒度
                                    float granularity = 1;
                                    // 初始化图表（图表本身样式、X 轴、Y 轴数据）
                                    ChartUtil.initChart(chart, resultDataPoints, granularity);
                                    // 创建 marker view（即点击折线图上的数据点时，展示的详情小弹窗）
                                    final ChartMarkerView markerView =
                                            new ChartMarkerView(HomeActivity.this,
                                                    R.layout.view_marker, resultDataPoints);
                                    markerView.setChartView(chart);
                                    chart.setMarker(markerView);
                                    // 设置图表数据
                                    chart.setData(ChartUtil.generateLineData(resultDataPoints));
                                }
                            } else {
                                chart.setNoDataText("暂无报表数据！");
                            }
                            // 数据更新后，需要重绘图表
                            chart.invalidate();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponseEntity<SensorDataRecord>> call,
                                          Throwable t) {
                        super.onFailure(call, t);
                        dismissLoading();
                    }
                }
        );
    }

    private static class GetDeviceInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}