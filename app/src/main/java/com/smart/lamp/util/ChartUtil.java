package com.smart.lamp.util;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.fatto.mpandroidchart.charts.LineChart;
import com.fatto.mpandroidchart.components.AxisBase;
import com.fatto.mpandroidchart.components.Legend;
import com.fatto.mpandroidchart.components.XAxis;
import com.fatto.mpandroidchart.components.YAxis;
import com.fatto.mpandroidchart.data.Entry;
import com.fatto.mpandroidchart.data.LineData;
import com.fatto.mpandroidchart.data.LineDataSet;
import com.fatto.mpandroidchart.formatter.DefaultAxisValueFormatter;
import com.fatto.mpandroidchart.interfaces.datasets.ILineDataSet;
import com.smart.lamp.net.response.SensorDataRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 图表工具类
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 27/4/2019 5:28 PM
 */
public class ChartUtil {

    // 颜色值
    private static final int RED = Color.parseColor("#ff0000");
    private static final int BLUE = Color.parseColor("#357CE4");
    private static final int BLUE_LIGHT = Color.parseColor("#8BC0FF");
    private static final int GREY = Color.parseColor("#333333");

    /**
     * 初始化图表
     *
     * @param chart        图表控件
     * @param dates        数据源
     */
    public static void initChart(LineChart chart, List<SensorDataRecord.VR> dates,
                                 float granularity) {
        chart.setNoDataText("暂无报表");
        // 可拖动
        chart.setDragEnabled(true);
        //默认显示时，是按照多大的比率缩放显示, 1f表示不放大也不缩小
        chart.zoom(0f, 0f, 0, 0);
        chart.zoom(1.5f, 1f, 0, 0);
        // 缩小到此值时，不能再缩小
        chart.setScaleMinima(2f, 1f);
        // 绘制图表时的动画执行时间
        chart.animateX(500);
        // 设置描述信息
        chart.getDescription().setText("时间");
        // 描述信息垂直方向的偏移量
        chart.getDescription().setYOffset(8f);
        // 设置底部边距
        chart.setExtraBottomOffset(10f);

        // 禁止显示图例
        Legend l = chart.getLegend();
        l.setEnabled(false);

        // X 轴配置项
        XAxis xAxis = chart.getXAxis();
        // X 轴文字大小
        xAxis.setTextSize(10f);
        // x轴文字显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //文字颜色
        xAxis.setTextColor(GREY);
        // 是否在垂直方向上画网格
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(BLUE_LIGHT);
        // 设置网格线样式（宽度、虚线间隔）
        xAxis.setGridLineWidth(0.5f);
        float[] intervals = {1f, 2f};
        xAxis.setGridDashedLine(new DashPathEffect(intervals, 1.5f));
        // 是否绘制 X 轴线
        xAxis.setDrawAxisLine(true);
        // 设置x 轴线宽度
        xAxis.setAxisLineWidth(2f);
        // X 轴线颜色
        xAxis.setAxisLineColor(BLUE);
        // 设置 X轴最小值
        xAxis.setAxisMinimum(0f);
        // X轴 有多少个值（该count应与数据源的size相同）
        xAxis.setLabelCount(dates.size());
        // 设置X轴递增粒度
        xAxis.setGranularity(1f);
        // 格式化显示 x 轴的值
        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0) {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    // 取出日期时间串中的“时间”
                    return dates.get((int) value).RecordTime.split(" ")[1];
                } catch (Exception e) {
                    return "";
                }
            }
        });
        // 左侧 Y 轴配置项
        YAxis leftAxis = chart.getAxisLeft();
        // y 轴文字颜色
        leftAxis.setTextColor(GREY);
        // y 轴最大值
        leftAxis.setAxisMaximum(100f);
        // y 轴最小值
        leftAxis.setAxisMinimum(0f);
        // 是否绘制水平方向的网格
        leftAxis.setDrawGridLines(true);
        // 网格颜色
        leftAxis.setGridColor(BLUE_LIGHT);
        // 设置网格线样式（宽度、虚线间隔）
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setGridDashedLine(new DashPathEffect(intervals, 1.5f));
        // 是否绘制Y 轴的轴线
        leftAxis.setDrawAxisLine(true);
        // y 轴颜色
        leftAxis.setAxisLineColor(BLUE);
        // Y 轴线宽度
        leftAxis.setAxisLineWidth(2f);
        // 设置 Y 轴递增粒度
        leftAxis.setGranularity(granularity);

        // 禁用右侧 Y 轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    /**
     * 生成图表数据及其样式
     *
     * @return line date
     */
    public static LineData generateLineData(List<SensorDataRecord.VR> datas) {
        // 组装图表数据
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            entries.add(new Entry(i, datas.get(i).Value));
        }
        // 配置图表折线样式
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        // 折线宽度
        lineDataSet.setLineWidth(1f);
        // 点击获取焦点时，线的颜色
        lineDataSet.setHighLightColor(BLUE_LIGHT);
        // 设置折线颜色
        lineDataSet.setColor(RED);
        // 设置圆的颜色
        lineDataSet.setCircleColor(RED);
        // 点的半径，值越大，圆越大
        lineDataSet.setCircleRadius(3f);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(lineDataSet);
        return new LineData(sets);
    }
}
