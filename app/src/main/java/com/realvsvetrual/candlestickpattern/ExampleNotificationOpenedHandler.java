package com.realvsvetrual.candlestickpattern;

import android.content.Context;
import android.content.Intent;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
    Context mcontext;
    public ExampleNotificationOpenedHandler(Context context) {
        this.mcontext = context;
    }
    @Override
    public void notificationOpened(OSNotificationOpenedResult osNotificationOpenedResult) {
        OSNotificationAction.ActionType actionType = osNotificationOpenedResult.getAction().getType();
        JSONObject data = osNotificationOpenedResult.getNotification().toJSONObject();
        String customKey;

        try {
            Intent intent = new Intent(mcontext,NewsList.class);
            intent.putExtra("url",data.getString("launchURL"));
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            mcontext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
