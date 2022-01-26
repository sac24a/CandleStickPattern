package com.realvsvetrual.candlestickpattern;

public class TradeDataType {
    String trade;
    double buyamount, sellamount,profit;
    public TradeDataType(String trade,
            double buyamount,double sellamount,double profit){
        this.trade = trade;
        this.buyamount = buyamount;
        this.sellamount = sellamount;
        this.profit = profit;
    }
}
