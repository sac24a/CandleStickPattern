package com.realvsvetrual.candlestickpattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Fullscreen extends AppCompatActivity  {

    ArrayList<String> urlArray;
    int index;
    ViewPager viewPager;
    FullScreenImageAdapter fullScreenImageAdapter;
    Button back;
    TextView textView;
    private RelativeLayout adContainer;
    private InterstitialAd mInterstitialAd;
    ProgressDialog progress;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_fullscreen);
        progress=new ProgressDialog(this);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);


        try {
            viewPager = findViewById(R.id.pager);
            back = findViewById(R.id.back);
            textView= findViewById(R.id.title_header);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            Intent intent = getIntent();
            urlArray = intent.getStringArrayListExtra("array");
            index = intent.getIntExtra("index",0);
            final ArrayList<String> newurl = new ArrayList();
            switch (index) {
                case 13:
                    for (int i = 0; i<13; i++) {
                        newurl.add(urlArray.get(i));
                    }
                    break;
                case 46:
                    for (int i = 13; i<46; i++) {
                        newurl.add(urlArray.get(i));
                    }
                    break;
                case 78:
                    for (int i = 46; i<78; i++) {
                        newurl.add(urlArray.get(i));
                    }
                    break;
                case 108:
                    for (int i = 78; i<108; i++) {
                        newurl.add(urlArray.get(i));
                    }
                case 136:
                    for (int i = 108; i<136; i++) {
                        newurl.add(urlArray.get(i));
                    }
                    break;
                case 168:
                    for (int i = 136; i<168; i++) {
                        newurl.add(urlArray.get(i));
                    }
                    break;
                default:
                    break;
            }
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = viewPager.getCurrentItem();
                    if (item !=newurl.size()-1) {
                        viewPager.setCurrentItem(item+1);
                    }

                }
            });
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item = viewPager.getCurrentItem();
                    if (item !=0) {
                        viewPager.setCurrentItem(item-1);
                    }
                }
            });
            textView.setText(getIntent().getStringExtra("title"));
            fullScreenImageAdapter = new FullScreenImageAdapter(this,newurl);
            viewPager.setAdapter(fullScreenImageAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == newurl.size()-1) {
                        checkAds();
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        catch (IndexOutOfBoundsException e) {
            Log.d("Error", "onCreate: "+e);
        }
        catch (IllegalArgumentException e) {
            Log.d("Error", "onCreate: "+e);

        }

    }
    public void checkAds(){

        try {
            String url = "https://candlestickschart.com/api/Candlestick/getReq.php?service=ads";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responselogin", response);
                            try {

                                JSONArray jsonObject = new JSONArray(response);
                                String version = jsonObject.getJSONObject(0).getString("ads");
//                                progressBar.setVisibility(View.GONE);
                                if (version.equals("1")){
                                    setupInteretialAds();
//                                    showNativeAds();
                                }

                            }
                            catch (JSONException e) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            ) ;
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(postRequest);



        }
        catch (NullPointerException e) {

        }

    }

    public void showProgress(){


        progress.setTitle("Please wait");
        progress.setMessage("loading ad");
        progress.setCancelable(false);
        progress.show();
    }
    public void hideProgress(){

        progress.dismiss();
    }

    public void setupInteretialAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        showProgress();

//        mInterstitialAd.setAdUnitId("ca-app-pub-2800990351363646/1508283941");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-2800990351363646/1508283941",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Fullscreen.this.mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(Fullscreen.this);
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Fullscreen.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.

                                        Fullscreen.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
//                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
