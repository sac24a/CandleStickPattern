package com.realvsvetrual.candlestickpattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Analysis extends AppCompatActivity {
    Button backButton;
    TextView titleView;
    ListView listView;
    ArrayList<String> titleList;
    ArrayList<String> detailsList;
    ArrayList<String> urlList;
    ProgressBar progressBar ;
    boolean isAdLoaded = false;
    TemplateView template;
    private boolean adLoaded=false;
    private AdLoader adLoader ;
    UnifiedNativeAd unifiedNativeAdEx;
    NewsAdapter newsAdapter;
    private InterstitialAd mInterstitialAd;
    String data= ""; String title=""; String url="";
    Boolean mcustomTabOpened = false;
    Boolean mAdsStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        MobileAds.initialize (this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete( InitializationStatus initializationStatus ) {
            }
        });

        backButton = findViewById(R.id.back);
        titleView = findViewById(R.id.title_header);
        progressBar = findViewById(R.id.progressBar);
        titleList = new ArrayList<>();
        detailsList = new ArrayList<>();
        urlList = new ArrayList<>();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Analysis.this, MainActivity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Analysis.this.finish();
            }
        });
        listView = findViewById(R.id.newlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = detailsList.get(position);
                title = titleList.get(position);
                url = urlList.get(position);
                moveToNext(detailsList.get(position),titleList.get(position),urlList.get(position));
            }
        });
        CheckAds checkAds = new CheckAds(Analysis.this);
        checkAds.checkAdsEnableStatus(new ApiCallback() {
            @Override
            public void onResponse(boolean success) {
                mAdsStatus = success;
                if (mAdsStatus) {
                    setupInteretialAds();
                }
            }
        });
        getProduct("analysis");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Analysis.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mcustomTabOpened) {
            mcustomTabOpened = false;
            if (isAdLoaded) {
                showAds();
            }
            if (mAdsStatus) {
                setupInteretialAds();
            }
        }
    }
    public void showAds (){
        mInterstitialAd.show(Analysis.this);
    }
    public void moveToNext(String data, String title, String url) {
        mcustomTabOpened = true;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        int colorInt = Color.parseColor("#FF0000"); //red
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorInt)
                .build();
        builder.setDefaultColorSchemeParams(defaultColors);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    class NewsAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<String> titleList;
        ArrayList<String> dataList;
        ArrayList<String> urls;
        TemplateView templateView;
        UnifiedNativeAd unifiedNativeAd;

        public NewsAdapter(Context applicationContext,ArrayList<String>titleList,ArrayList<String>dataList,ArrayList<String>urlList,TemplateView templateView,UnifiedNativeAd unifiedNativeAd) {
            this.context = applicationContext;
//        this.logos = logos;
            this.titleList= titleList;
            this.dataList= dataList;
            this.urls= urlList;
            this.templateView = templateView;
            this.unifiedNativeAd = unifiedNativeAd;
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
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = inflter.inflate(R.layout.newslistlayout, null); // inflate the layout
            TextView textView = view.findViewById(R.id.title);
            textView.setText(titleList.get(i));
            Button button = view.findViewById(R.id.share);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey, Checkout this article");
                        String shareMessage= "\nI found this article on \n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"+urlList.get(i);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        e.toString();
                    }
                }
            });


            return view;
        }

    }

    public void getProduct (String name)
    {
        try {

            progressBar.setVisibility(View.VISIBLE);

            String url = "http://candlestickschart.com/api/webservice.php?service="+name;
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responselogin ======>", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                String data = "";
                                String title = "";
                                for (int i=0;i<jsonArray.length();i++) {
                                    title = jsonArray.getJSONObject(i).getString("Title");

                                    titleList.add(title);
                                    detailsList.add(data);
                                    urlList.add(jsonArray.getJSONObject(i).getString("Url"));
                                }
                                progressBar.setVisibility(View.GONE);
                                newsAdapter = new NewsAdapter(Analysis.this,titleList,detailsList,urlList,template,unifiedNativeAdEx);
                                listView.setAdapter(newsAdapter);
                            }
                            catch (JSONException e) {
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
        }
    }
    private void setupInteretialAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-2800990351363646/1578757080",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Analysis.this.mInterstitialAd = interstitialAd;
                        isAdLoaded = true;
                        progressBar.setVisibility(View.GONE);
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Analysis.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        progressBar.setVisibility(View.GONE);
                                        Analysis.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
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
}