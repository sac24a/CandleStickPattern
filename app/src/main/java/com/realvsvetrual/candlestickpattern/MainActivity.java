    package com.realvsvetrual.candlestickpattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.datatransport.BuildConfig;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button intro;
    Button exerciseButton;
    Button tradingButton;
    Button engulfButton;
    Button insideButton;
    Button patternButton;
    Button reload;

    ArrayList<String> urls;
    ProgressBar progressBar ;

//    AlarmManager manager;
//    PendingIntent pendingIntent;

    private RelativeLayout adContainer;
    private InterstitialAd mInterstitialAd;
//    String adsString = "";
    String ButtonType = "";
    private AdLoader adLoader ;
    private AdLoader adLoader1 ;
    private boolean adLoaded=false;

    TemplateView template1;
    TemplateView template2;
    String message = "Please check for update.";
    private void showNativeAd()
    {
        if ( adLoaded )
        {

            template1.setVisibility( View.VISIBLE) ;
            template2.setVisibility( View.VISIBLE) ;
            // Showing a simple Toast message to user when an Native ad is shown to the user

        }
        else
        {
            AdRequest adRequest = new AdRequest.Builder().build() ;

            // load Native Ad with the Request
            adLoader.loadAd(adRequest) ;
        }
    }

    public void showNativeAds(){

        template1 = findViewById(R.id.nativeTemplateView1);
        template2 = findViewById(R.id.nativeTemplateView2);
        String nativead1 = "ca-app-pub-2800990351363646/6252263569";
        String nativead2 = "ca-app-pub-2800990351363646/7373773547";
//        String testads = "ca-app-pub-3940256099942544/2247696110";

        adLoader = new AdLoader.Builder(this, nativead1)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    private ColorDrawable background;
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();
                        template2.setStyles(styles);
                        template2.setNativeAd(nativeAd);
                        adLoaded = true;
                        showNativeAd();
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                        Toast.makeText(MainActivity.this,"ads failed",Toast.LENGTH_LONG).show();
                        super.onAdFailedToLoad(loadAdError);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();


        adLoader1 = new AdLoader.Builder(this, nativead2)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    private ColorDrawable background;
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                        template1.setStyles(styles);
                        template1.setNativeAd(nativeAd);
                        adLoaded = true;
                        showNativeAd();
                    }

                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("TAG", "onAdFailedToLoad: "+loadAdError);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();


        AdRequest adRequest = new AdRequest.Builder().build() ;

        // load Native Ad with the Request
        adLoader.loadAd(adRequest) ;
        adLoader1.loadAd(adRequest) ;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_main);
        AdColony.configure(this, "app47fea0a828e840d88e", "vzf29313b26d21486ea5");
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                /** Store and use this ad object to show your ad when appropriate */
                ad.show();
            }
        };
        AdColony.requestInterstitial("vzf29313b26d21486ea5", listener);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        showNativeAds();
//        setupInteretialAds();
//        Intent alarmIntent = new Intent(MainActivity.this, MyReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
//        startAlarm();



        intro = findViewById(R.id.introduction);
        patternButton = findViewById(R.id.candlepattern);
        tradingButton = findViewById(R.id.trading);
        engulfButton = findViewById(R.id.engulfing);
        insideButton = findViewById(R.id.inside);
        exerciseButton = findViewById(R.id.exercise);
        reload= findViewById(R.id.reload);
        progressBar = findViewById(R.id.progressBar);
        urls = new ArrayList<>();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct();
                if (isInternetAvailable()) {

                }
                else {
                    Toast.makeText(MainActivity.this,"Internet is required to access content.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        if (isInternetAvailable()) {

        }
        else {
            Toast.makeText(MainActivity.this,"Internet is required to access content.",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }



        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "intro";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }




            }
        });
        patternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "pattern";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }



            }
        });
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "exercise";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }


            }
        });

        tradingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "trading";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }


            }
        });
        engulfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "engulf";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }


            }
        });
        insideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonType = "inside";
                if (isAdLoaded) {
                    mInterstitialAd.show(MainActivity.this);
                }
                else {
                    moveToNext();
                }


            }
        });

        Button share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CandleStick Chart Guide");
                    String shareMessage= "\nHey I am makeing profit by learing from this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        Button moreapps = findViewById(R.id.moreApps);
        moreapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ButtonType = "more";
//                progressBar.setVisibility(View.VISIBLE);
//                setupInteretialAds();
                Intent intent = new Intent(MainActivity.this,MoreApps.class);
                startActivity(intent);
            }
        });

        checkVersion();
    }

    public void checkAds(){

        try {

            progressBar.setVisibility(View.VISIBLE);

            String url = "http://candlestickschart.com/api/Candlestick/getReq.php?service=ads";
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
                                progressBar.setVisibility(View.GONE);
                                if (version.equals("1")){
                                    setupInteretialAds();
                                    showNativeAds();
                                }
                            }
                            catch (JSONException e) {
                                Toast.makeText(MainActivity.this,"Some error",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                                Log.e("volley_error", error.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) ;
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(postRequest);



        }
        catch (NullPointerException e) {
            Toast.makeText(MainActivity.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

    }

    public void checkVersion(){
        final String currentVersion = "5";
        try {

            progressBar.setVisibility(View.VISIBLE);

            String url = "http://candlestickschart.com/api/Candlestick/getReq.php?service=version";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responselogin", response);
                            try {

                                JSONArray jsonObject = new JSONArray(response);
                                String version = jsonObject.getJSONObject(0).getString("version");
                                progressBar.setVisibility(View.GONE);
                                if (version.equals(currentVersion)){
                                    getProduct();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Candlesticks Chart Guide");
                                    builder.setMessage("New update is available")
                                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                }
                                            });

                                    // Create the AlertDialog object and return it
                                    builder.create();
                                    builder.show();
                                }
                            }
                            catch (JSONException e) {
                                Toast.makeText(MainActivity.this,"Some error",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                                Log.e("volley_error", error.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) ;
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(postRequest);



        }
        catch (NullPointerException e) {
            Toast.makeText(MainActivity.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    boolean isAdLoaded = false;
    public void setupInteretialAds(){


//        mInterstitialAd.setAdUnitId("ca-app-pub-2800990351363646/8341390809");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //Test Ads


        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-2800990351363646/8341390809",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.mInterstitialAd = interstitialAd;
                        isAdLoaded = true;
                        progressBar.setVisibility(View.GONE);
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        progressBar.setVisibility(View.GONE);
                                        moveToNext();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        progressBar.setVisibility(View.GONE);
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
//                                        moveToNext();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        progressBar.setVisibility(View.GONE);

                    }
                });


    }
    public void moveToNext(){
        if (ButtonType.equals("intro")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",13);
                intent.putExtra("title","Intoduction");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("pattern")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",46);
                intent.putExtra("title","Candlestick Pattern");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("exercise")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",78);
                intent.putExtra("title","Candlestick Pattern Exercise");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("trading")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",108);
                intent.putExtra("title","Trading Exercise");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("engulf")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",136);
                intent.putExtra("title","Engulfing Bar");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("inside")){
            if (urls.size()!=0) {
                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                intent.putExtra("array",urls);
                intent.putExtra("index",168);
                intent.putExtra("title","The Inside Bar");
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
        else if (ButtonType.equals("share")){
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CandleStick Chart Guide");
                String shareMessage= "\nHey I am makeing profit by learing from this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        else if (ButtonType.equals("more")){
            try {
                Intent intent = new Intent(MainActivity.this,MoreApps.class);
                startActivity(intent);
            } catch(Exception e) {
                //e.toString();
            }
        }
    }

//    public void startAlarm() {
//
//        Calendar calendar=Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY,11);
//        calendar.set(Calendar.MINUTE,34);
//        calendar.set(Calendar.SECOND,00);
//        Calendar now=Calendar.getInstance();
//        long _alarm = 0;
//        if (calendar.getTimeInMillis()<=now.getTimeInMillis())
//        {
//            _alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
//        }
//        else
//        {
//            _alarm = calendar.getTimeInMillis();
//        }
//
//
//        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
////        long interval = 60*1000;
////        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, _alarm, 5*60*1000, pendingIntent);
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, _alarm, AlarmManager.INTERVAL_HOUR, pendingIntent);
//
//
//
////        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isAdLoaded = false;
        checkAds();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void getProduct ()
    {
        try {

            progressBar.setVisibility(View.VISIBLE);

            String url = "http://candlestickschart.com/api/Candlestick/getReq.php?service=get";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responselogin", response);
                            try {

                                JSONArray jsonObject = new JSONArray(response);

                                    for (int i=0;i<jsonObject.length();i++) {
                                        urls.add(jsonObject.getJSONObject(i).getString("url"));
                                    }

                                progressBar.setVisibility(View.GONE);



                            }
                            catch (JSONException e) {
                                Toast.makeText(MainActivity.this,"Some error",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                                Log.e("volley_error", error.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) ;
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(postRequest);



        }
        catch (NullPointerException e) {
            Toast.makeText(MainActivity.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }



    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
