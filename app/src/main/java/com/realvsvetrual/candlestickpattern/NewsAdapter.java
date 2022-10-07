package com.realvsvetrual.candlestickpattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class NewsAdapterNew extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<ApiData> titleList;
    public NewsAdapterNew(Context applicationContext,List<ApiData>titleList) {
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
        view = inflter.inflate(R.layout.news_layout, null);
        TextView newsTitle = view.findViewById(R.id.newsTitle);
        TextView newsDate = view.findViewById(R.id.newsDate);

        newsTitle.setText(String.valueOf(titleList.get(i).title));
        newsDate.setText(String.valueOf(titleList.get(i).date));
        return view;
    }
}
