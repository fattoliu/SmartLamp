package com.fatto.mpandroidchart.interfaces.dataprovider;

import com.fatto.mpandroidchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
