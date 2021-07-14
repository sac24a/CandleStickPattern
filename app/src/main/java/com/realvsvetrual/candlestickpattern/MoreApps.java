package com.realvsvetrual.candlestickpattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoreApps extends AppCompatActivity {

    ListView gridView;
    ProgressBar progressBar;
    ArrayList<MoreAppData> moreAppData;
    TemplateView template;
    private boolean adLoaded=false;
    private AdLoader adLoader ;
    NativeAd unifiedNativeAdEx;
    GridAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_more_apps);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        moreAppData = new ArrayList<>();
        gridView = findViewById(R.id.moreappsGrid);
        progressBar = findViewById(R.id.progressBar);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        String nativead1 = "ca-app-pub-2800990351363646/6252263569";
        adLoader = new AdLoader.Builder(this, nativead1)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    private ColorDrawable background;
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        unifiedNativeAdEx = nativeAd;
                        adLoaded = true;
                        showNativeAd();
                    }



                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();

        AdRequest adRequest = new AdRequest.Builder().build() ;

        // load Native Ad with the Request
        adLoader.loadAd(adRequest) ;

        getMoreApps();
    }
    private void showNativeAd()
    {
        if ( adLoaded )
        {
            if (moreAppData.size() != 0) {
                gridAdapter = new GridAdapter(MoreApps.this,moreAppData,unifiedNativeAdEx);
                gridView.setAdapter(gridAdapter);

            }
        }

    }

    public void getMoreApps(){
        try {

            progressBar.setVisibility(View.VISIBLE);

            String url = "http://candlestickschart.com/api/Candlestick/moreapp.php?service=get";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responselogin", response);
                            try {
                                moreAppData = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(response);
                                Log.d("TAG", "onResponse======>: "+jsonArray.length());
                                for (int i = 0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    moreAppData.add(new MoreAppData(jsonObject.getString("app_name"),jsonObject.getString("app_url"),jsonObject.getString("app_image")));
                                    if (i%5 == 0) {
                                        moreAppData.add(new MoreAppData("ad","ad","ad"));
                                    }
                                }
                                Log.d("TAG", "onResponse: "+moreAppData.size());
                                progressBar.setVisibility(View.GONE);
                                Log.d("Value", "onResponse: "+moreAppData.get(0).name);

                                gridAdapter = new GridAdapter(MoreApps.this,moreAppData,unifiedNativeAdEx);
                                gridView.setAdapter(gridAdapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        try {
                                            Uri uri = Uri.parse(moreAppData.get(position).url); // missing 'http://' will cause crashed
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        } catch(Exception e) {
                                            //e.toString();
                                        }
                                    }
                                });

                            }
                            catch (JSONException e) {
                                Toast.makeText(MoreApps.this,"Some error",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MoreApps.this,"Some fields are missing",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    class GridAdapter extends BaseAdapter {

        Context context;
        ArrayList<MoreAppData> moreAppData;
        LayoutInflater inflter;
        NativeAd unifiedNativeAd;

        GridAdapter(Context context, ArrayList<MoreAppData> moreAppData, NativeAd unifiedNativeAd) {
            this.context = context;
            this.moreAppData = moreAppData;
            inflter = (LayoutInflater.from(context));
            this.unifiedNativeAd = unifiedNativeAd;
        }

        @Override
        public int getCount() {
            return moreAppData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (moreAppData.get(position).name.equals("ad")) {
                convertView = inflter.inflate(R.layout.nativeadslayout, null); // inflate the layout
                TemplateView templateView1 = convertView.findViewById(R.id.nativeTemplateView);
                TemplateView templateView2 = convertView.findViewById(R.id.nativeTemplateView1);

                if (adLoaded) {
                    if (position == 1) {
                        templateView2.setNativeAd(unifiedNativeAd);
                        templateView2.setVisibility(View.VISIBLE);
                    } else {
                        templateView1.setNativeAd(unifiedNativeAd);
                        templateView1.setVisibility(View.VISIBLE);
                    }

                } else {
                    templateView1.setVisibility(View.GONE);
                    templateView2.setVisibility(View.GONE);
                }
            }
            else  {
                convertView = inflter.inflate(R.layout.moreapplay, null);// inflate the layout
                ImageView imageView = convertView.findViewById(R.id.appImage);
                TextView textView = convertView.findViewById(R.id.appName);

                textView.setText(moreAppData.get(position).name);
                Picasso.get().load(moreAppData.get(position).image).into(imageView);
            }



            return convertView;
        }
    }

}


