package com.realvsvetrual.candlestickpattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.viewpager.widget.PagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {
    Analysis analysis;
    Context mcontext;
    ArrayList<String> url;
    public MyPagerAdapter(Context context, Analysis activity) {
        this.mcontext = context;
        this.analysis = activity;

    }
    @Override
    public int getCount() {
        return 5;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.pager_layout,container,false);
        ApiCall apiCall = new ApiCall(mcontext);
        ListView listView = viewGroup.findViewById(R.id.listView);
        ProgressBar progressBar = viewGroup.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        switch (position) {
            case 0:
                apiCall.getAnalysisData(new Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<ApiData> apiData = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++) {
                                apiData.add(new ApiData(jsonArray.getJSONObject(i).getInt("ID"),jsonArray.getJSONObject(i).getString("Title"),jsonArray.getJSONObject(i).getString("Url"),jsonArray.getJSONObject(i).getString("Date")));
                            }
                            NewsAdapterNew newsAdapter = new NewsAdapterNew(mcontext,apiData);
                            listView.setAdapter(newsAdapter);
                            progressBar.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    analysis.mcustomTabOpened = true;
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    int colorInt = Color.parseColor("#FF0000"); //red
                                    CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(colorInt)
                                            .build();
                                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_launcher_background));
                                    builder.setDefaultColorSchemeParams(defaultColors);
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(mcontext, Uri.parse(apiData.get(i).url));
                                }
                            });

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 1:
                apiCall.getIPOData(new Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<ApiData> apiData = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++) {
                                apiData.add(new ApiData(jsonArray.getJSONObject(i).getInt("ID"),jsonArray.getJSONObject(i).getString("Title"),jsonArray.getJSONObject(i).getString("Url"),jsonArray.getJSONObject(i).getString("Date")));
                            }
                            NewsAdapterNew newsAdapter = new NewsAdapterNew(mcontext,apiData);
                            listView.setAdapter(newsAdapter);
                            progressBar.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    analysis.mcustomTabOpened = true;
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    int colorInt = Color.parseColor("#FF0000"); //red
                                    CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(colorInt)
                                            .build();
                                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_launcher_background));
                                    builder.setDefaultColorSchemeParams(defaultColors);
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(mcontext, Uri.parse(apiData.get(i).url));
                                }
                            });

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 2:
                apiCall.getDividendData(new Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<ApiData> apiData = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++) {
                                apiData.add(new ApiData(jsonArray.getJSONObject(i).getInt("ID"),jsonArray.getJSONObject(i).getString("Title"),jsonArray.getJSONObject(i).getString("Url"),jsonArray.getJSONObject(i).getString("Date")));
                            }
                            NewsAdapterNew newsAdapter = new NewsAdapterNew(mcontext,apiData);
                            listView.setAdapter(newsAdapter);
                            progressBar.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    analysis.mcustomTabOpened = true;
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    int colorInt = Color.parseColor("#FF0000"); //red
                                    CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(colorInt)
                                            .build();
                                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_launcher_background));
                                    builder.setDefaultColorSchemeParams(defaultColors);
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(mcontext, Uri.parse(apiData.get(i).url));
                                }
                            });

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 3:
                apiCall.getBonusSplitData(new Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<ApiData> apiData = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++) {
                                apiData.add(new ApiData(jsonArray.getJSONObject(i).getInt("ID"),jsonArray.getJSONObject(i).getString("Title"),jsonArray.getJSONObject(i).getString("Url"),jsonArray.getJSONObject(i).getString("Date")));
                            }
                            NewsAdapterNew newsAdapter = new NewsAdapterNew(mcontext,apiData);
                            listView.setAdapter(newsAdapter);
                            progressBar.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    analysis.mcustomTabOpened = true;
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    int colorInt = Color.parseColor("#FF0000"); //red
                                    CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(colorInt)
                                            .build();
                                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_launcher_background));
                                    builder.setDefaultColorSchemeParams(defaultColors);
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(mcontext, Uri.parse(apiData.get(i).url));
                                }
                            });

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 4:
                apiCall.getNewsData(new Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<ApiData> apiData = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++) {
                                apiData.add(new ApiData(jsonArray.getJSONObject(i).getInt("ID"),jsonArray.getJSONObject(i).getString("Title"),jsonArray.getJSONObject(i).getString("Url"),jsonArray.getJSONObject(i).getString("Date")));
                            }
                            NewsAdapterNew newsAdapter = new NewsAdapterNew(mcontext,apiData);
                            listView.setAdapter(newsAdapter);
                            progressBar.setVisibility(View.GONE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    analysis.mcustomTabOpened = true;
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    int colorInt = Color.parseColor("#FF0000"); //red
                                    CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(colorInt)
                                            .build();
                                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_launcher_background));
                                    builder.setDefaultColorSchemeParams(defaultColors);
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(mcontext, Uri.parse(apiData.get(i).url));
                                }
                            });

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
        container.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Analysis";
            case 1:
                return "IPO";
            case 2:
                return "Dividend";
            case 3:
                return "Bonus/Split";
            case 4:
                return "News";
            default:
                return "";
        }
    }
}
