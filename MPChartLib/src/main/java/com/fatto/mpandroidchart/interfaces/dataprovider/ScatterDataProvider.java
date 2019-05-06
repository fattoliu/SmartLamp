package com.fatto.mpandroidchart.interfaces.dataprovider;

import com.fatto.mpandroidchart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
