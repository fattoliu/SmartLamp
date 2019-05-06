package com.fatto.mpandroidchart.interfaces.dataprovider;

import com.fatto.mpandroidchart.data.BarLineScatterCandleBubbleData;
import com.fatto.mpandroidchart.utils.Transformer;
import com.fatto.mpandroidchart.components.YAxis;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
