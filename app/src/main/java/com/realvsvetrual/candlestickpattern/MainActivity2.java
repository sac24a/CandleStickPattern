package com.realvsvetrual.candlestickpattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    ArrayList<String> titleList;
    ArrayList<String> detailsList;
    ArrayList<String> urlList;
    ProgressBar progressBar ;
    NewsAdapter newsAdapter;
    String data= ""; String title=""; String url="";
    boolean isAdLoaded = false;
    String adType = "";
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private int positionSelected = 0;
    Button reload;
    String currentVersion = "16";
    Button switchOld;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main2);
        MobileAds.initialize (this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete( InitializationStatus initializationStatus ) {
            }
        });
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);
        titleList = new ArrayList<>();
        detailsList = new ArrayList<>();
        urlList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        reload= findViewById(R.id.reload);
        progressBar = findViewById(R.id.progressBar);
        switchOld = findViewById(R.id.switchToNewApp);
        switchOld.setPaintFlags(switchOld.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        switchOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = detailsList.get(position);
                title = titleList.get(position);
                url = urlList.get(position);
                positionSelected = position;
                if (isAdLoaded) {
                    showAds();
                }
                else  {
                    moveToNext(detailsList.get(position),titleList.get(position),urlList.get(position));
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
                    String shareMessage= "\nHey I am making profit by learning from this application\n\n";
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
                drawerLayout.openDrawer(Gravity.LEFT);

            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,CandleStickChartActivity.class);
                startActivity(intent);
            }
        });
        checkVersion();
        UpdateApp();
    }
    public void UpdateApp(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                requestUpdate(result);
                android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this,R.style.AlertDialog_AppCompat);
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
                alertDialogBuilder.setTitle("Update Candlestick Pattern");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setIcon(R.drawable.googleplay);
                alertDialogBuilder.setMessage("Candlestick Pattern recommends that you update to the latest version for a seamless & enhanced performance of the app.");
                alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No Thanks",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialogBuilder.show();

            } else {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_oldapp:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent3 = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_papertrading:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent2 = new Intent(MainActivity2.this,CandleStickChartActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_ipo:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity2.this,Analysis.class);
                intent.putExtra("position",1);
                startActivity(intent);
                break;
            case R.id.nav_analysis:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent4 = new Intent(MainActivity2.this,Analysis.class);
                intent4.putExtra("position",0);
                startActivity(intent4);
                break;
            case R.id.nav_dividend:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentdiv = new Intent(MainActivity2.this,Analysis.class);
                intentdiv.putExtra("position",2);
                startActivity(intentdiv);
                break;
            case R.id.nav_bonus:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentbon = new Intent(MainActivity2.this,Analysis.class);
                intentbon.putExtra("position",3);
                startActivity(intentbon);
                break;
            case R.id.nav_share:
                drawerLayout.closeDrawer(Gravity.LEFT);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CandleStick Chart Guide");
                    String shareMessage= "\nHey I am making profit by learning from this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.nav_moreapp:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent1 = new Intent(MainActivity2.this,MoreApps.class);
                startActivity(intent1);
                break;
            default:
                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
        }
        return true;
    }
    public void checkVersion(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            String url = "https://candlestickschart.com/api/Candlestick/getReq.php?service=version";
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
                                    progressBar.setVisibility(View.VISIBLE);
                                    getProduct("appversion");
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
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
                                Toast.makeText(MainActivity2.this,"Some error",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) ;
            Mysingleton.getInstance(getApplicationContext()).addToRequestque(postRequest);
        }
        catch (NullPointerException e) {
            Toast.makeText(MainActivity2.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        isAdLoaded = false;
        checkAds();
    }
    public void moveToNext(String data, String title, String url) {
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

        public NewsAdapter(Context applicationContext,ArrayList<String>titleList,ArrayList<String>dataList,ArrayList<String>urlList) {
            this.context = applicationContext;
            this.titleList= titleList;
            this.dataList= dataList;
            this.urls= urlList;
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
            String url = "https://candlestickschart.com/api/webservice.php?service="+name;
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
                                    data = jsonArray.getJSONObject(i).getString("Content");
                                    titleList.add(title);
                                    detailsList.add(data);
                                    urlList.add(jsonArray.getJSONObject(i).getString("Url"));
                                }
                                progressBar.setVisibility(View.GONE);
                                newsAdapter = new NewsAdapter(MainActivity2.this,titleList,detailsList,urlList);
                                listView.setAdapter(newsAdapter);
                            }
                            catch (JSONException e) {
                                Toast.makeText(MainActivity2.this,"",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity2.this,"",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
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
                                adType = version;
                                if (version.equals("1")){
                                    MobileAds.initialize(MainActivity2.this, new OnInitializationCompleteListener() {
                                        @Override
                                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                                        }
                                    });
                                    setupInteretialAds();
                                }
                                else if (version.equals("2")) {
                                    MobileAds.initialize(MainActivity2.this, new OnInitializationCompleteListener() {
                                        @Override
                                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                                        }
                                    });
                                    readyVideoAds();
                                }
                                else if(version.equals("3")) {
                                    MobileAds.initialize(MainActivity2.this, new OnInitializationCompleteListener() {
                                        @Override
                                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                                        }
                                    });
                                    setupInteretialAds();
                                }
                                else if(version.equals("4")) {
                                    MobileAds.initialize(MainActivity2.this, new OnInitializationCompleteListener() {
                                        @Override
                                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                                        }
                                    });
                                    readyVideoAds();
                                }
                            }
                            catch (JSONException e) {
                                Toast.makeText(MainActivity2.this,"Some error",Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
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
            Toast.makeText(MainActivity2.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.GONE);
        }

    }
    private void setupInteretialAds(){
//        mInterstitialAd.setAdUnitId("ca-app-pub-2800990351363646/8341390809");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  Test Ads
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
                        MainActivity2.this.mInterstitialAd = interstitialAd;
                        isAdLoaded = true;
                        progressBar.setVisibility(View.GONE);
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity2.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        progressBar.setVisibility(View.GONE);
                                        moveToNext(detailsList.get(positionSelected),titleList.get(positionSelected),urlList.get(positionSelected));
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        progressBar.setVisibility(View.GONE);
                                        MainActivity2.this.mInterstitialAd = null;
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
    public void readyVideoAds(){
        if (mRewardedAd == null) {
//            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    "ca-app-pub-2800990351363646/3885471827",
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d("TAG", loadAdError.getMessage());
                            mRewardedAd = null;
                            isAdLoaded = false;
//                            MainActivity.this.isLoading = false;
//                            Toast.makeText(MainActivity2.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            isAdLoaded = true;
                            Log.d("TAG", "onAdLoaded");
//                            MainActivity.this.isLoading = false;
//                            Toast.makeText(MainActivity2.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void showAds (){
        if (adType.equals("1")) {
            mInterstitialAd.show(MainActivity2.this);
        }
        else if (adType.equals("2")) {
            showRewardedVideo();
        }
        else if(adType.equals("3")) {
            mInterstitialAd.show(MainActivity2.this);
        }
        else if(adType.equals("4")) {
            showRewardedVideo();
        }
    }
    private void showRewardedVideo() {

        if (mRewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
//        showVideoButton.setVisibility(View.INVISIBLE);

        mRewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
//                        Log.d("TAG", "onAdShowedFullScreenContent");
//                        Toast.makeText(MainActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
//                        Log.d("TAG", "onAdFailedToShowFullScreenContent");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mRewardedAd = null;
//                        Toast.makeText(
//                                MainActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                        moveToNext(detailsList.get(positionSelected),titleList.get(positionSelected),urlList.get(positionSelected));
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mRewardedAd = null;
                        Log.d("TAG", "onAdDismissedFullScreenContent");
//                        Toast.makeText(MainActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                        // Preload the next rewarded ad.
                        readyVideoAds();
                        moveToNext(detailsList.get(positionSelected),titleList.get(positionSelected),urlList.get(positionSelected));

                    }
                });
        Activity activityContext = MainActivity2.this;
        mRewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward."+rewardItem.getAmount());
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                    }
                });
    }
}