package com.realvsvetrual.candlestickpattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ProfitLoss extends AppCompatActivity {
    ListView listView;
    NewsAdapter newsAdapter;
    Button back;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);
        listView = findViewById(R.id.listView);
        total = findViewById(R.id.total);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setData();


    }

    public void setData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                TradeDatabase tradeDatabase = TradeDatabase.getInstance(ProfitLoss.this);
                List<TradeData> tradeDetails = tradeDatabase.tradeDao().getTradeData();

                Log.d("ListSize ==>", "run: "+tradeDetails.size());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        double totalAmount = 0;
                        for (int i = 0; i<tradeDetails.size();i++) {
                            totalAmount = totalAmount + tradeDetails.get(i).profit;
                        }
                        total.setText("₹ "+String.format("%.2f",totalAmount));
                        if (totalAmount>0) {
                            total.setTextColor(ContextCompat.getColor(ProfitLoss.this,R.color.green));
                        }
                        else  {
                            total.setTextColor(ContextCompat.getColor(ProfitLoss.this,R.color.red));
                        }
                        newsAdapter = new NewsAdapter(ProfitLoss.this,tradeDetails);
                        listView.setAdapter(newsAdapter);
                    }
                });

            }
        });
    }

    class NewsAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<TradeData> titleList;

        public NewsAdapter(Context applicationContext,List<TradeData>titleList) {
            this.context = applicationContext;
            this.titleList= titleList;
            inflter = (LayoutInflater.from(applicationContext));
        }
        @Override
        public int getCount() {
            return titleList.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }
        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = inflter.inflate(R.layout.profit_loss_layout, null);
            TextView tradename = view.findViewById(R.id.trade_name);
            TextView buysell = view.findViewById(R.id.buysell);
            TextView profit = view.findViewById(R.id.profit);
            TextView percentage = view.findViewById(R.id.percent);
            tradename.setText("Trade - "+titleList.get(i).id);

            buysell.setText("₹ "+String.format("%.2f",titleList.get(i).buyamount)+" - "+"₹ "+String.format("%.2f",titleList.get(i).sellamount));
            profit.setText("₹ "+String.format("%.2f",titleList.get(i).profit));
            if (titleList.get(i).profit>0) {
                profit.setTextColor(ContextCompat.getColor(ProfitLoss.this,R.color.green));
            }
            else  {
                profit.setTextColor(ContextCompat.getColor(ProfitLoss.this,R.color.red));
            }
            return view;
        }

    }
}