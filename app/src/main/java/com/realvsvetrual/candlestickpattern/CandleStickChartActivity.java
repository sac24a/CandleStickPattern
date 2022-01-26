package com.realvsvetrual.candlestickpattern;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CandleStickChartActivity extends AppCompatActivity {
    private CandleStickChart chart;
    Handler handler = new Handler();
    Button buy,sell, drawnextCandle, profitloss;
    int i = 0;
    List scoreList;
    private static final int STORAGE_PERMISSION_CODE = 101;
    double buyamount = 0.00;
    double sellamount = 0.00;
    ArrayList<CandleEntry> values = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    String TAG = "Paper Trading";
    private FrameLayout adContainerView;
    private AdView mAdView;
    int buyCount = 0;
    Runnable r;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_candle_stick_chart);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAds();
            }
        });
        buy = findViewById(R.id.buyBtn);
        sell = findViewById(R.id.sellBtn);
        profitloss = findViewById(R.id.trade);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(r);
                finish();
            }
        });
        profitloss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandleStickChartActivity.this,ProfitLoss.class);
                startActivity(intent);
            }
        });
        drawnextCandle = findViewById(R.id.nextCandle);
        drawnextCandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawcandlesticks(scoreList);
            }
        });
        buy.setEnabled(true);
        sell.setEnabled(false);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buy.isEnabled()) {
                    String [] string = (String[]) scoreList.get(i);
                    buyamount = Float.parseFloat(string[7]);
                    Log.d("Buy Amount", "onClick: "+buyamount);
                    Toast.makeText(CandleStickChartActivity.this,String.valueOf("Buy amount: "+String.format("%.2f",buyamount)),Toast.LENGTH_SHORT).show();
                    buy.setEnabled(false);
                    sell.setEnabled(true);
                    buyCount = buyCount+1;
                }
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buy.isEnabled()) {

                    String [] string = (String[]) scoreList.get(i);
                    sellamount = Float.parseFloat(string[7]);
                    Log.d("Sell Amount", "onClick: "+sellamount);
                    Log.d("Profit/Loss", "onClick: "+(sellamount - buyamount));
                    Toast.makeText(CandleStickChartActivity.this,String.valueOf("Profit/Loss: "+String.format("%.2f",(sellamount-buyamount))),Toast.LENGTH_LONG).show();
                    sell.setEnabled(false);
                    buy.setEnabled(true);
                    saveData(buyamount,sellamount,(sellamount - buyamount),0);
                    if (buyCount == 10) {
                        mInterstitialAd.show(CandleStickChartActivity.this);
                        buyCount = 0;
                    }
                }
            }
        });
        chart = findViewById(R.id.chart1);
        chart.setBackgroundColor(Color.WHITE);
        chart.setPinchZoom(true);
        chart.setDragEnabled(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
//        rightAxis.setStartAtZero(false);

        // setting data
        chart.getLegend().setEnabled(false);
        main();
        loadBannerAds();

    }
    void loadBannerAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-2800990351363646/1508283941", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                loadAds();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });

                        Log.i(TAG, "onAdLoaded");
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }
    public void saveData(double buyamount,double sellamount, double profit,double percen) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TradeDatabase tradeDataBase = TradeDatabase.getInstance(CandleStickChartActivity.this);
                    tradeDataBase.tradeDao().insertNewVoter(new TradeData(0,"",buyamount,sellamount,profit));

                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void main()
    {
        Random r=new Random();
        int randomNumber=r.nextInt(23);
        openFile(randomNumber);
    }
    void openFile(int id){
        Integer[] arr={R.raw.adaniport, R.raw.asianpaint, R.raw.bpcl, R.raw.cipla, R.raw.divislab, R.raw.grasim, R.raw.hcltech, R.raw.heromoto, R.raw.hindalco, R.raw.hul, R.raw.infy, R.raw.jswsteel, R.raw.ongc, R.raw.persistent, R.raw.reliance, R.raw.sbin, R.raw.tataconsumer, R.raw.tatamotor, R.raw.tatasteel, R.raw.techm, R.raw.titan, R.raw.ultratech, R.raw.upl};
        InputStream inputStream = getResources().openRawResource(arr[id]);
        CSVFile csvFile = new CSVFile(inputStream);
        scoreList = csvFile.read();
        drawcandlesticks(scoreList);
        enableAutomatic();
    }

    public void enableAutomatic() {
        r = new Runnable() {
            public void run() {
                drawcandlesticks(scoreList);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }
    void drawcandlesticks(List scoreList) {
        if (i<scoreList.size()-1) {
            String [] open = (String[]) scoreList.get(i+1);
            values.add(new CandleEntry(i + 1,Float.parseFloat(open[3]),Float.parseFloat(open[4]),Float.parseFloat(open[2]),Float.parseFloat(open[7])));
            CandleDataSet set1= new CandleDataSet(values,"Data Set");
            set1.setDrawIcons(false);
            set1.setDrawIcons(false);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
            set1.setShadowColor(Color.DKGRAY);
            set1.setShadowWidth(0.7f);
            set1.setDecreasingColor(Color.RED);
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(Color.rgb(122, 242, 84));
            set1.setIncreasingPaintStyle(Paint.Style.FILL);
            set1.setNeutralColor(Color.BLUE);
            //set1.setHighlightLineWidth(1f);
            CandleData data = new CandleData(set1);
            chart.setData(data);
            chart.invalidate();
            i = i+1;
        }
        else {
            handler.removeCallbacks(r);
        }

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(r);
        super.onBackPressed();
    }
}
