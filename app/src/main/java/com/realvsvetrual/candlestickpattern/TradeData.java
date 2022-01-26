package com.realvsvetrual.candlestickpattern;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tradetbl")
public class TradeData {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "trade")
    public String trade;
    @ColumnInfo(name = "buyamount")
    public double buyamount;
    @ColumnInfo(name = "sellamount")
    public double sellamount;
    @ColumnInfo(name = "profit")
    public double profit;

    public TradeData(int id,
                         String trade,
                         double buyamount,
                         double sellamount,
                         double profit
                         ) {
        this.id = id;
        this.trade = trade;
        this.buyamount = buyamount;
        this.sellamount = sellamount;
        this.profit = profit;
    }
}
