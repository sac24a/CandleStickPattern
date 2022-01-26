package com.realvsvetrual.candlestickpattern;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TradeDao {
    @Query("Select * from tradetbl")
    List<TradeData> getTradeData();

    @Insert
    void insertNewVoter(TradeData tradeData);
    @Delete
    void deletePollFirst(TradeData tradeData);
}
