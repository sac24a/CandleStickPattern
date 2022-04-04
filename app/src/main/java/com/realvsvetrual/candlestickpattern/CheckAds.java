package com.realvsvetrual.candlestickpattern;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class CheckAds {
    Context mcontext;
    int VersionCode = BuildConfig.VERSION_CODE;
    public CheckAds(Context context) {
        this.mcontext = context;
    }
    public void checkAdsEnableStatus(final ApiCallback callback) {
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

                                if(version.equals("3")) {
                                    callback.onResponse(true);
                                }
                                else {
                                    callback.onResponse(false);
                                }
                            }
                            catch (JSONException e) {
                                Toast.makeText(mcontext,"Some error",Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

//                                Log.e("volley_error", error.toString());
//                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) ;
            Mysingleton.getInstance(mcontext).addToRequestque(postRequest);
        }
        catch (NullPointerException e) {
            Toast.makeText(mcontext,"Some fields are missing",Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.GONE);
        }
    }
    public void checkAppVersion(final ApiCallback callback) {
        try {

//            progressBar.setVisibility(View.VISIBLE);

            String url = "https://candlestickschart.com/api/Candlestick/getReq.php?service=papertradingversion";
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
//                                progressBar.setVisibility(View.GONE);
                                if (version.equals(String.valueOf(VersionCode))) {
                                    callback.onResponse(true);
                                }
                                else {
                                    callback.onResponse(false);
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
            Mysingleton.getInstance(mcontext).addToRequestque(postRequest);
        }
        catch (NullPointerException e) {
        }
    }

}
