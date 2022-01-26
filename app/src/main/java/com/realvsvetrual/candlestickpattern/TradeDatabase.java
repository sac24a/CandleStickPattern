package com.realvsvetrual.candlestickpattern;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TradeData.class}, exportSchema = false, version = 1)
public abstract class TradeDatabase extends RoomDatabase {
    private static final String DB_Name = "tradedb";
    private static TradeDatabase instance;
    public static synchronized TradeDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), TradeDatabase.class,DB_Name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public abstract TradeDao tradeDao();
}
