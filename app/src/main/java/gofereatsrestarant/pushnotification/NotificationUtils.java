package gofereatsrestarant.pushnotification;
/**
 * @package com.gofereatsrestarant
 * @subpackage pushnotification
 * @category NotificationUtils
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gofereatsrestarant.R;
import gofereatsrestarant.views.main.MainActivity;
import gofereatsrestarant.views.main.OrderDetails;
import gofereatsrestarant.views.main.OrderHistoryDetails;

import static android.content.Context.NOTIFICATION_SERVICE;

/*************************************************************
 NotificationUtils for generate and design notification
 ****************************************************************/
public class NotificationUtils {


    private Context mContext;
    private String isRedirect = "";
    private static String notificationChannelName = "HumHumRestaurant";


    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    public void showNotificationMessage(final String title, String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            //message="GoferEats is in background";
            return;


        // notification icon
        int icon=R.drawable.app_notify_logo ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.app_notify_logo;
            //builder.setColor(Color.RED);
        } else {
            icon = R.mipmap.ic_launcher;
        }

        System.out.println("messagemessagemessage" + message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                //PendingIntent.FLAG_CANCEL_CURRENT
                PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext,notificationChannelName);

        //mBuilder.setContentIntent(resultPendingIntent);
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/ub_reminder");
        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startMyOwnForeground(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    }else {
                        showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    }
                    playNotificationSound();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startMyOwnForeground(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    }else {
                        showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    }
                    playNotificationSound();
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startMyOwnForeground(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            }else {
                showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            }
            // playNotificationSound();
        }
    }

        /*if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    Log.v("Bigmessage", "message" + message);
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent);
                    playNotificationSound();
                } else {
                    Log.v("Smallmessage", "message" + message);
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent);
                    playNotificationSound();
                }
            }
        } else {
            Log.v("Smallmessage", "message" + message);
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent);
            // playNotificationSound();
        }
    }*/

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                // .setSound(alarmSound)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(icon)
                //.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(icon)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)

                // .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound){

        String channelName =createNotificationChannel(this.mContext);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, channelName);
        Notification notification = notificationBuilder.setTicker(title)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(mContext, R.color.white))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.app_notify_logo)
                .setContentText(message)
                .build();
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = context.getString(R.string.app_name);
            // The user-visible description of the channel.
            String channelDescription =  context.getString(R.string.app_name);
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
//            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
//            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;


        }
    }
    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateNotification(Context context, String message, String status, String jsons) {

        //Some Vars
        final int NOTIFICATION_ID = 1; //this can be any int
        String title = context.getString(R.string.app_name);
        // Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = context.getResources().getColor(R.color.apptheme);

        //Building the Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setColor(color);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Intent notificationIntent;
        notificationIntent = newMessage(context, jsons, status);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        showNotificationMessage(title, message, timeStamp, notificationIntent, null);


        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    public Intent newMessage(Context context, String jsons, String status) {
        Intent notificationIntent = null;
        try {
            //{"custom":{"type":"order_delivery_started","order_id":111}}
            JSONObject json = new JSONObject(jsons);
            if (json.getJSONObject("custom").has("redirect")) {
                isRedirect = json.getJSONObject("custom").getString("redirect");
            }
            if (status.equalsIgnoreCase("new_order") || status.equalsIgnoreCase("schedule_order")) {
                notificationIntent = new Intent(context, MainActivity.class);
                //notificationIntent.putExtra("matchId",Integer.valueOf(data.getJSONObject("chat_status").getString("match_id")));
                //notificationIntent.putExtra("userId",Integer.valueOf(data.getJSONObject("chat_status").getString("sender_id")));
                //notificationIntent.putExtra("reservationid",data.getJSONObject("chat_status").getString("reservation_id"));
                //notificationIntent.putExtra("message",jsons);

                //order_cancelled -> Driver cancel from contact cancel option
            } else if ("driver_accepted".equalsIgnoreCase(status)) {
                int orderId = json.getJSONObject("custom").getInt("order_id");
                notificationIntent = new Intent(context, OrderDetails.class);
                notificationIntent.putExtra("orderId", orderId);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            } else if ("no_drivers_found".equalsIgnoreCase(status) || "order_delivery_started".equalsIgnoreCase(status)) {
                int orderId = json.getJSONObject("custom").getInt("order_id");
                OrderDetails.isReFresh = true;
                notificationIntent = new Intent(context, OrderDetails.class);
                notificationIntent.putExtra("orderId", orderId);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            }


            /* For Check getting the Redirect Status

              Before Start Trip
                {"redirect":"0","type":"order_cancelled","title":"Your Order Id #242 has been cancelled by driver","order_id":242}}

              After Start  Trip
                {custom={"redirect":"1","type":"order_cancelled","title":"Your Order Id #241 has been cancelled by driver","order_id":241}}
             */

            else if ("order_delivery_completed".equalsIgnoreCase(status) || ("order_cancelled".equalsIgnoreCase(status) && "1".equalsIgnoreCase(isRedirect))) {
                int orderId = json.getJSONObject("custom").getInt("order_id");
                notificationIntent = new Intent(context, OrderHistoryDetails.class);
                notificationIntent.putExtra("orderId", orderId);
                notificationIntent.putExtra("type", 1);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            } else if ("order_cancelled".equalsIgnoreCase(status) && "0".equalsIgnoreCase(isRedirect)) {
                notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notificationIntent;
    }
}