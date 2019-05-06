package com.fatto.mpandroidchart.interfaces.dataprovider;

import com.fatto.mpandroidchart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
