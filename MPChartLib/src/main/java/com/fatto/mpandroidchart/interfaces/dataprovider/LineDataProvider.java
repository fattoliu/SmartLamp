package com.fatto.mpandroidchart.interfaces.dataprovider;

import com.fatto.mpandroidchart.components.YAxis;
import com.fatto.mpandroidchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
