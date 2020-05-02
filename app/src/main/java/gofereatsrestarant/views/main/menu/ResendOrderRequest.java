package gofereatsrestarant.views.main.menu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.pushnotification.Config;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;

public class ResendOrderRequest extends AppCompatActivity implements ServiceListener {

    private static final Integer CALL = 0x2;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @InjectView(R.id.tvorderstatus)
    CustomTextView tvorderstatus;
    public @InjectView(R.id.tvorderId)
    CustomTextView tvorderId;
    public @InjectView(R.id.tvContactnumb)
    CustomTextView tvContactnumb;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;

    private String orderId;
    private String message;
    private String supportNumber;
    private BroadcastReceiver mBroadcastReceiver;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnTryAgain)
    public void tryAgain() {
        resendOrders();
    }

    @OnClick(R.id.btnContact)
    public void contact() {
        String num = supportNumber.replace("-", "");
        phoneCall(num);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_order_request);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack, this);
        getIntents();
        updateview();
        receivePushNotification();
    }

    /**
     * Get intent data from previous activity
     **/
    public void getIntents() {
        orderId = getIntent().getStringExtra("orderId");
        message = getIntent().getStringExtra("message");
        supportNumber = getIntent().getStringExtra("supportNumber");
    }

    /**
     * Set data to the views
     **/
    public void updateview() {
        tvorderstatus.setText(message);
        tvContactnumb.setText(supportNumber);
        /*if()*/
        tvorderId.setText(getString(R.string.order) + " " + "#" + orderId);
    }

    /**
     * Call API to resend order request
     */
    public void resendOrders() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.doneOrders(Integer.valueOf(orderId), sessionManager.getToken()).enqueue(new RequestCallback(this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Get notification from Firebase broadcast
     */
    public void receivePushNotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // FCM successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    String JSON_DATA = sessionManager.getPushNotification();

                    try {
                        JSONObject jsonObject = new JSONObject(JSON_DATA);

                        if (jsonObject.getJSONObject("custom").has("type")) {
                            if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("no_drivers_found")) {
                                String orderId = jsonObject.getJSONObject("custom").getString("order_id");

                                resend(message, orderId);
                            } else if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("driver_accepted")) {
                                finish();
                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());

    }

    public void resend(String message, String orderId) {
        finish();
        Intent orderDetails = new Intent(this, ResendOrderRequest.class);
        orderDetails.putExtra("message", message);
        orderDetails.putExtra("orderId", orderId);
        startActivity(orderDetails);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    /**
     * Call permission
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ResendOrderRequest.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ResendOrderRequest.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ResendOrderRequest.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ResendOrderRequest.this, new String[]{permission}, requestCode);
            }
        } /*else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void phoneCall(String callnumber) {

        askForPermission(Manifest.permission.CALL_PHONE, CALL);

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callnumber));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


}
