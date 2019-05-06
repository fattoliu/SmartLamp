package com.smart.lamp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fatto.mpandroidchart.components.MarkerView;
import com.fatto.mpandroidchart.data.Entry;
import com.fatto.mpandroidchart.highlight.Highlight;
import com.fatto.mpandroidchart.utils.MPPointF;
import com.smart.lamp.R;
import com.smart.lamp.net.response.SensorDataRecord;

import java.util.List;


/**
 * TODO 自定义图表数据弹出视图
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 27/4/2019 1:14 PM
 */
@SuppressLint("ViewConstructor")
public class ChartMarkerView extends MarkerView {

    /**
     * 数据源
     */
    private List<SensorDataRecord.VR> datas;
    /**
     * 年份
     */
    private TextView year;
    /**
     * 日期
     */
    private TextView date;
    /**
     * 时间
     */
    private TextView time;
    /**
     * 值
     */
    private TextView value;

    public ChartMarkerView(Context context, int layoutResource,
                           List<SensorDataRecord.VR> datas) {
        super(context, layoutResource);
        this.datas = datas;
        year = findViewById(R.id.year);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        value = findViewById(R.id.value);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SensorDataRecord.VR vr = datas.get((int) e.getX());
        if (vr != null) {
            String[] fullDateTime = vr.RecordTime.split(" ")[0].split("-");
            year.setText(String.format("年份：%s年", fullDateTime[0]));
            date.setText(String.format("日期：%s月%s日", fullDateTime[1], fullDateTime[2]));
            time.setText(String.format("时间：%s", vr.RecordTime.split(" ")[1]));
            value.setText(String.format("距离：%s cm", vr.Value));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        // 默认显示在右上方
        ((View) year.getParent()).setBackgroundResource(R.drawable.chart_popu);
        return new MPPointF(0, -getHeight());
    }

    @Override
    public MPPointF getOffsetRight() {
        // 右边不够显示时，显示在左上方
        ((View) year.getParent()).setBackgroundResource(R.drawable.chart_popu_right);
        return new MPPointF(-getWidth(), -getHeight());
    }
}
