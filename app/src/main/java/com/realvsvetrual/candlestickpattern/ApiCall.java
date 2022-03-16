package com.realvsvetrual.candlestickpattern;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class ApiCall {
    Context mcontext;
    public ApiCall(Context context) {
        this.mcontext = context;
    }
    public void getAnalysisData(final Callback callback) {
        try {
            String url = "http://candlestickschart.com/api/webservice.php?service=analysis";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            // response
                            callback.onResponse(response);
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
    public void getIPOData(final Callback callback) {
        try {
            String url = "http://candlestickschart.com/api/webservice.php?service=ipo";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            callback.onResponse(response);
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
    public void getDividendData(final Callback callback) {
        try {
            String url = "http://candlestickschart.com/api/webservice.php?service=dividend";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            callback.onResponse(response);
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
    public void getBonusSplitData(final Callback callback) {
        try {
            String url = "http://candlestickschart.com/api/webservice.php?service=bonus";
            Log.e("Response", url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            callback.onResponse(response);
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
