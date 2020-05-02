package gofereatsrestarant.pushnotification;
/**
 * @package com.gofereatsrestarant
 * @subpackage pushnotification
 * @category FirebaseMessagingService
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.views.commondialog.ShowDialog;
import gofereatsrestarant.views.main.MainActivity;
import gofereatsrestarant.views.main.OrderDetails;


/**************************************************************
 Fire base Messaging Service to base push notification message to activity
 ****************************************************************/
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public @Inject
    SessionManager sessionManager;


    public int orderId = 0, type = 1;
    String message = "", status = "", title = "", isRedirect = "";
    MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.getAppComponent().inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message Notification remoteMessage: " + remoteMessage.toString());

        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage == null) return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    /**
     * To Handle Json Response Received from Push Notification
     *
     * @param json Json Response Received from Push Notification
     */

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        /*custom={"type":"order_cancelled","title":"Order has been cancelled.","order_id":549}
        {custom={"type":"no_drivers_found","support_mobile":"98745612310","title":"No drivers found for your order #96","order_id":96}}
        {custom={"type":"driver_accepted","title":"Driver accepted your order #96","order_id":96}}
        {custom={"type":"order_delivery_completed","title":"Order delivery completed #99","order_id":99}}*/
        //push json: {"custom":{"redirect":"0","type":"order_cancelled","title":"Your Order Id #370 has been cancelled by driver","order_id":370}}
        try {
            //JSONObject data = json.getJSONObject("custom");

            sessionManager.setPushNotification(json.toString());

            if (json.getJSONObject("custom").has("type")) {
                status = json.getJSONObject("custom").getString("type");
            }

            if (json.getJSONObject("custom").has("title")) {
                message="";
                title = json.getJSONObject("custom").getString("title");
            }

            if (json.getJSONObject("custom").has("order_id")) {
                orderId = json.getJSONObject("custom").getInt("order_id");
            }

            if (json.getJSONObject("custom").has("redirect")) {
                isRedirect = json.getJSONObject("custom").getString("redirect");
            }

            if ("".equals(message)) {
                message = title;
            }

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e(TAG, "IF: " + json.toString());

                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                /**
                 *  Order Status
                 *  1  --- Order Cancelled by User or Driver
                 *  2  --- No Driver Found
                 *  3  --- Driver Accepted
                 *  4  --- Order Delivery Completed
                 */
                if ("new_order".equalsIgnoreCase(status) || "schedule_order".equalsIgnoreCase(status)) {
                    makeSound();
                    if (!MainActivity.isMainActivity) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else if ("order_cancelled".equalsIgnoreCase(status)) {
                    int types;
                    /* Before Start Trip
                    {"redirect":"0","type":"order_cancelled","title":"Your Order Id #242 has been cancelled by driver","order_id":242}}*/

                    /* After Start  Trip
                    {custom={"redirect":"1","type":"order_cancelled","title":"Your Order Id #241 has been cancelled by driver","order_id":241}}*/

                    if("0".equalsIgnoreCase(isRedirect))
                        types=3;
                    else
                        types=1;
                    //if (OrderDetails.isShown) {
                        Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                        dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialogs.putExtra("message", title);
                        dialogs.putExtra("type", types);
                        dialogs.putExtra("orderId", orderId);
                        startActivity(dialogs);
                   // }
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                } else if ("order_delivery_completed".equalsIgnoreCase(status)) {
                    if (OrderDetails.isShown) {
                        Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                        dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialogs.putExtra("message", title);
                        dialogs.putExtra("type", 4);
                        dialogs.putExtra("orderId", orderId);
                        startActivity(dialogs);
                    }
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                } else {
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                }

                /*else if ("no_drivers_found".equalsIgnoreCase(status)){
                    Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.putExtra("message",title);
                    dialogs.putExtra("type", 2);
                    startActivity(dialogs);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else if ("driver_accepted".equalsIgnoreCase(status)){
                    Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.putExtra("message",title);
                    dialogs.putExtra("type", 3);
                    dialogs.putExtra("orderId", orderId);
                    startActivity(dialogs);
                }*/
            } else {
                Log.e(TAG, "ELSE: " + json.toString());

                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                if (!sessionManager.getToken().equals("")) {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }


                if ("new_order".equalsIgnoreCase(status) || "schedule_order".equalsIgnoreCase(status)) {
                    makeSound();
                    if (!MainActivity.isMainActivity) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("sound", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    if ("no_drivers_found".equalsIgnoreCase(status)){
                        OrderDetails.isLoad=true;
                    }
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Make Sound while receiving push notification
     */

    public void makeSound() {
        mPlayer = MediaPlayer.create(this, R.raw.gofer);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

}

