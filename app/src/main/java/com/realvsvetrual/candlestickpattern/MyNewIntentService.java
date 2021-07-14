package com.realvsvetrual.candlestickpattern;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyNewIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.realvsvetrual.candlestickpattern.action.FOO";
    private static final String ACTION_BAZ = "com.realvsvetrual.candlestickpattern.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.realvsvetrual.candlestickpattern.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.realvsvetrual.candlestickpattern.extra.PARAM2";
    private static final int NOTIFICATION_ID = 3;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> message = new ArrayList<>();

    public MyNewIntentService() {
        super("MyNewIntentService");

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyNewIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyNewIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

        title.add("Check Pre-Open market");
        message.add("Always check Pre-Open market on Official website to know market's mood");
        title.add("Learn and Earn");
        message.add("Keep learning and earning, Increase your wealth by learning chart pattern");
        title.add("Learn Harami Pattern");
        message.add("Do you know what is Harami pattern");
        title.add("You should know these Trap");
        message.add("Market always try to make these types of traps");



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Intent notIntent = new Intent(this, MainActivity.class);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                    notIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            builder.setSmallIcon(R.drawable.notification);
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,34);
            calendar.set(Calendar.SECOND,00);
            Calendar now=Calendar.getInstance();
            long _alarm = 0;
            if (calendar.getTimeInMillis()<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+60*60*1000)
            {
                builder.setContentTitle(title.get(0));
                builder.setContentText(message.get(0));
            }
            else if (calendar.getTimeInMillis()+60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+2*60*60*1000)
            {
                builder.setContentTitle(title.get(1));
                builder.setContentText(message.get(1));
            }
            else if (calendar.getTimeInMillis()+2*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+3*60*60*1000)
            {
                builder.setContentTitle(title.get(2));
                builder.setContentText(message.get(2));
            }
            else if (calendar.getTimeInMillis()+3*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+4*60*60*1000)
            {
                builder.setContentTitle(title.get(3));
                builder.setContentText(message.get(3));
            }
            else if (calendar.getTimeInMillis()+4*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+5*60*60*1000)
            {
                builder.setContentTitle(title.get(4));
                builder.setContentText(message.get(4));
            }


            manager.notify(NOTIFICATION_ID, builder.build());




        }
        else
        {
            Notification.Builder builder = new Notification.Builder(this);
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,19);
            calendar.set(Calendar.SECOND,00);
            Calendar now=Calendar.getInstance();
            long _alarm = 0;
            if (calendar.getTimeInMillis()<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+60*60*1000)
            {
                builder.setContentTitle(title.get(0));
                builder.setContentText(message.get(0));
            }
            else if (calendar.getTimeInMillis()+60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+2*60*60*1000)
            {
                builder.setContentTitle(title.get(1));
                builder.setContentText(message.get(1));
            }
            else if (calendar.getTimeInMillis()+2*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+3*60*60*1000)
            {
                builder.setContentTitle(title.get(2));
                builder.setContentText(message.get(2));
            }
            else if (calendar.getTimeInMillis()+3*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+4*60*60*1000)
            {
                builder.setContentTitle(title.get(3));
                builder.setContentText(message.get(3));
            }
            else if (calendar.getTimeInMillis()+4*60*60*1000<=now.getTimeInMillis() && now.getTimeInMillis()<calendar.getTimeInMillis()+5*60*60*1000)
            {
                builder.setContentTitle(title.get(4));
                builder.setContentText(message.get(4));
            }

            builder.setSmallIcon(R.drawable.notification);
            Intent notifyIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //to be able to launch your activity from the notification
            builder.setContentIntent(pendingIntent);
            Notification notificationCompat = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, notificationCompat);

        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
