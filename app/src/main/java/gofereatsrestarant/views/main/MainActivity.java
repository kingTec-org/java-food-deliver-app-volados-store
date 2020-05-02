package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category MainActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.OrderAdapter;
import gofereatsrestarant.adapters.main.OrderOutDeliveryAdapter;
import gofereatsrestarant.adapters.main.OrderPendingAdapter;
import gofereatsrestarant.adapters.main.OrderRecentAdapter;
import gofereatsrestarant.adapters.main.OrderScheduledAdapter;
import gofereatsrestarant.adapters.main.alert.AlertListAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.IssueModel;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.datamodels.main.OrderResultModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.placesearch.RecyclerItemClickListener;
import gofereatsrestarant.pushnotification.Config;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;

import static gofereatsrestarant.utils.Enums.REQ_ACCEPT_ORDER;
import static gofereatsrestarant.utils.Enums.REQ_RESTAURANT_STATUS;
import static gofereatsrestarant.utils.Enums.REQ_REVIEW_DRIVER;
import static gofereatsrestarant.utils.Enums.REQ_SCHEDULE;
import static gofereatsrestarant.utils.Enums.REQ_UPDATE_DEVICE_ID;
import static gofereatsrestarant.utils.Enums.REQ_VIEW_ORDER;

/*****************************************************************
 Main Activity contain navigation and order details
 ****************************************************************/
public class MainActivity extends BaseActivity implements ServiceListener {

    public static boolean isMainActivity = false;
    private static ArrayList<IssueModel> issueModels = new ArrayList<>();
    public boolean isActive = true;
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
    public @Inject
    ImageUtils imageUtils;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.toolbar)
    Toolbar toolbar;
    public @InjectView(R.id.drawer)
    DrawerLayout drawerLayout;
    public @InjectView(R.id.navigation_view)
    NavigationView navigationView;
    public @InjectView(R.id.tvToolBarTitle)
    TextView tvToolBarTitle;
    public @InjectView(R.id.ivOrderStatus)
    ImageView ivOrderStatus;
    public @InjectView(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    public @InjectView(R.id.tvOrderTime)
    TextView tvOrderTime;
    public @InjectView(R.id.rvPendingOrder)
    CustomRecyclerView rvPendingOrder;
    public @InjectView(R.id.rvOrder)
    CustomRecyclerView rvOrder;
    public @InjectView(R.id.rvOutDeliver)
    CustomRecyclerView rvOutDeliver;
    public @InjectView(R.id.rvRecent)
    CustomRecyclerView rvRecent;
    public @InjectView(R.id.rvScheduled)
    CustomRecyclerView rvScheduled;
    public @InjectView(R.id.lltOutDeliver)
    LinearLayout lltOutDeliver;
    public @InjectView(R.id.lltRecent)
    LinearLayout lltRecent;
    public @InjectView(R.id.lltScheduled)
    LinearLayout lltScheduled;
    public @InjectView(R.id.rltEmptylayout)
    RelativeLayout rltEmptylayout;
    public @InjectView(R.id.nsvOrder)
    NestedScrollView nsvOrder;
    public @InjectView(R.id.rltStatus)
    RelativeLayout rltStatus;
    public @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    boolean isSound = false;
    private AlertListAdapter alertListAdapter;
    private AlertDialog dialog;
    private BroadcastReceiver mBroadcastReceiver;
    private ArrayList<OrderModel> orderModels = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private ArrayList<OrderModel> orderPendingModels = new ArrayList<>();
    private OrderPendingAdapter orderPendingAdapter;
    private ArrayList<OrderModel> orderOutDeliveryModels = new ArrayList<>();
    private OrderOutDeliveryAdapter orderOutDeliveryAdapter;
    private ArrayList<OrderModel> orderRecentModels = new ArrayList<>();
    private OrderRecentAdapter orderRecentAdapter;
    private ArrayList<OrderModel> orderScheduleModels = new ArrayList<>();
    private OrderScheduledAdapter orderScheduledAdapter;
    private OrderResultModel orderResultModel;
    private Dialog dialog1;
    private int backPressed = 0;
    private String issueId = "";

    @OnClick(R.id.rltStatus)
    public void updateStatus() {
        showPictureialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer(savedInstanceState, "order");
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        getIntents();
        initRecyclerView();
        setUpToolbar();
        updateDeviceId();
        receivePushNotification();
    }

    /**
     * Get Intent form previous activity
     **/
    public void getIntents() {
        isSound = getIntent().getBooleanExtra("sound", false);
        isSound=false;
        System.out.println("First Sound "+isSound);
        if (isSound){
            makeSound();
        }
        else
        {
            System.out.println("Sound Reached "+ isSound);
        }
    }

    /**
     * Make Sound while receiving push notification
     */

    public void makeSound() {
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.gofer);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    /**
     * Setup navigation toolbar and OnClick
     **/
    private void setUpToolbar() {

        tvToolBarTitle.setVisibility(View.VISIBLE);
        //tvToolBarTitle.setText(getApplicationContext().getResources().getString(R.string.app_name));
        tvToolBarTitle.setText(sessionManager.getRestaurantName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

       // sessionManager.setCurrencySymbol("$");
    }

    /**
     * Update restaurant status is available or not
     **/
    private void updateStatus(boolean check) {
        if (check) {

            tvOrderTime.setVisibility(View.GONE);
            tvOrderStatus.setText(getResources().getString(R.string.accepting_order));
            tvOrderStatus.setTextColor(getResources().getColor(R.color.apptheme));
            ivOrderStatus.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
            ivOrderStatus.setImageResource(R.drawable.check);
            rltStatus.setBackground(getResources().getDrawable(R.drawable.background_black_full));
        } else {
            // tvOrderTime.setVisibility(View.VISIBLE);
            tvOrderStatus.setText(getResources().getString(R.string.orders_paused));
            tvOrderStatus.setTextColor(getResources().getColor(R.color.white));
            ivOrderStatus.setImageResource(R.drawable.pause);
            ivOrderStatus.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            rltStatus.setBackground(getResources().getDrawable(R.drawable.background_red_full));
        }
    }

    /**
     * To Show dialog of accept order and pause new order
     */

    private void showPictureialog() {
        dialog1 = new Dialog(this);

        // Setting dialogview
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.setTitle(null);
        dialog1.setContentView(R.layout.order_status);
        dialog1.setCancelable(true);

        DialogInject dialogInject = new DialogInject();
        // 5. We bind the elements of the included layouts.
        ButterKnife.inject(dialogInject, dialog1);
        if (isActive) {
            dialogInject.ivPauseOrderTick.setVisibility(View.GONE);
            dialogInject.ivAcceptOrderTick.setVisibility(View.VISIBLE);
        } else {
            dialogInject.ivPauseOrderTick.setVisibility(View.VISIBLE);
            dialogInject.ivAcceptOrderTick.setVisibility(View.GONE);
        }
        dialog1.show();

    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        LinearLayoutManager customLayoutManager = new LinearLayoutManager(this);
        rvPendingOrder.setLayoutManager(customLayoutManager);
        CustomLayoutManager customLayoutManager0 = new CustomLayoutManager(this);
        rvOrder.setLayoutManager(customLayoutManager0);
        CustomLayoutManager customLayoutManager1 = new CustomLayoutManager(this);
        rvOutDeliver.setLayoutManager(customLayoutManager1);
        CustomLayoutManager customLayoutManager2 = new CustomLayoutManager(this);
        rvRecent.setLayoutManager(customLayoutManager2);
        CustomLayoutManager customLayoutManager3 = new CustomLayoutManager(this);
        rvScheduled.setLayoutManager(customLayoutManager3);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if (orderPendingAdapter != null && orderPendingAdapter.timerMap.size() > 0) {
                    for (CountDownTimer timer : orderPendingAdapter.timerMap.values()) {
                        timer.cancel();
                    }
                }
                getOrders(false);
            }
        });

        orderPendingAdapter = new OrderPendingAdapter(this);
        rvPendingOrder.setAdapter(orderPendingAdapter);
        rvPendingOrder.setHasFixedSize(true);

        orderAdapter = new OrderAdapter(this);
        rvOrder.setAdapter(orderAdapter);
        rvOrder.setHasFixedSize(true);

        orderOutDeliveryAdapter = new OrderOutDeliveryAdapter(this);
        rvOutDeliver.setAdapter(orderOutDeliveryAdapter);
        rvOutDeliver.setHasFixedSize(true);

        orderRecentAdapter = new OrderRecentAdapter(this);
        rvRecent.setAdapter(orderRecentAdapter);
        rvRecent.setHasFixedSize(true);

        orderScheduledAdapter = new OrderScheduledAdapter(this);
        rvScheduled.setAdapter(orderScheduledAdapter);
        rvScheduled.setHasFixedSize(true);


        rvPendingOrder.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (orderPendingModels != null && orderPendingModels.size() > 0) {
                    // List Pending order List

                    acceptOrders(orderPendingModels.get(position).getOrderId(), orderPendingModels.get(position).getOrderType());

                    orderPendingModels.remove(position);

                    orderPendingAdapter.timerMap.get(position).cancel();

                    if (orderPendingModels.size() > 0) {
                        if (orderPendingAdapter != null && orderPendingAdapter.timerMap.size() > 0) {
                            for (CountDownTimer timer : orderPendingAdapter.timerMap.values()) {
                                timer.cancel();
                            }
                        }
                        rvPendingOrder.setVisibility(View.VISIBLE);
                        orderPendingAdapter = new OrderPendingAdapter(getApplicationContext(), orderPendingModels);
                        rvPendingOrder.setAdapter(orderPendingAdapter);
                    } else {
                        rvPendingOrder.setVisibility(View.GONE);
                    }
                } else {
                    rvPendingOrder.setVisibility(View.GONE);
                }

            }
        }));


    }

    /**
     * Call API to update restaurant device id
     */
    private void updateDeviceId() {
        /**
         *  Type 0 for Eater 1 for restaurant 2 for Driver
         *  Device type 2 for android and 1 for iOS
         */

        if (TextUtils.isEmpty(sessionManager.getDeviceId())) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sessionManager.setDeviceId(refreshedToken);
        }
        apiService.updateDeviceId(1, sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
    }

    /**
     * Call API to get Order list
     */
    private void getOrders(boolean isLoad) {
        if (isLoad && !isSound)
            commonMethods.showProgressDialog(this, customDialog);
        apiService.getOrders(sessionManager.getToken()).enqueue(new RequestCallback(REQ_VIEW_ORDER, this));
        isSound=false;
    }

    /**
     * Call API to accept order
     */
    private void acceptOrders(Integer orderId, int orderType) {
        commonMethods.showProgressDialog(this, customDialog);
        if (orderType == 1) {
            apiService.acceptOrders(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_SCHEDULE, this));
        } else {
            apiService.acceptOrders(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_ACCEPT_ORDER, this));
        }


    }

    /**
     * Call API to Update Restaurant status
     */
    private void updateRestaurantStatus(int status) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.storeavailtoggle(sessionManager.getToken(), status).enqueue(new RequestCallback(REQ_RESTAURANT_STATUS, this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_DEVICE_ID:
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_VIEW_ORDER:
                if (jsonResp.isSuccess()) {
                    onSuccessViewOrder(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_ACCEPT_ORDER:
                if (jsonResp.isSuccess()) {
                    onSuccessAcceptOrder(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_REVIEW_DRIVER:
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_RESTAURANT_STATUS:
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    updateStatus(false);
                    isActive = false;
                }
                break;
            case REQ_SCHEDULE:
                if (jsonResp.isSuccess()) {
                    onSuccessScheduleOrder();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * on Successfully accepting order
     *
     * @param jsonResponse json response of successful accept order api call
     */

    public void onSuccessAcceptOrder(JsonResponse jsonResponse) {
        getOrders(true);
        /*orderResultModel = gson.fromJson(jsonResponse.getStrResponse(), OrderResultModel.class);
        if (orderResultModel != null) {

            // List Current order List
            if (orderResultModel.getAcceptedOrderDetails() != null && (orderResultModel.getAcceptedOrderDetails().size() > 0)) {
                orderModels.clear();
                rvOrder.setVisibility(View.VISIBLE);
                orderModels.addAll(orderResultModel.getAcceptedOrderDetails());
                orderAdapter = new OrderAdapter(this, orderModels);
                rvOrder.setAdapter(orderAdapter);
            } else {
                rvOrder.setVisibility(View.GONE);
            }
        }*/
    }


    private void onSuccessScheduleOrder() {
        getOrders(true);
    }


    public void onSuccessViewOrder(JsonResponse jsonResponse) {
        swipeContainer.setRefreshing(false);
        orderResultModel = gson.fromJson(jsonResponse.getStrResponse(), OrderResultModel.class);
        if (orderResultModel != null) {
            sessionManager.setCurrencySymbol(orderResultModel.getDefaultCurrencysymbol());
            sessionManager.setRestaurantName(orderResultModel.getRestaurantName());
            if (orderResultModel.getStatus() == 1) {
                updateStatus(true);
                isActive = true;
            } else {
                updateStatus(false);
                isActive = false;
            }
            if (orderResultModel.getPendingOrderDetails().size() == 0 && orderResultModel.getCurrentOrderDetails().size() == 0 && orderResultModel.getDeliveryOrderDetails().size() == 0 && orderResultModel.getCompletedOrderDetails().size() == 0 && orderResultModel.getScheduledOrderDetails().size() == 0) {
                rltEmptylayout.setVisibility(View.VISIBLE);
                rvPendingOrder.setVisibility(View.GONE);
                rvOrder.setVisibility(View.GONE);
                lltOutDeliver.setVisibility(View.GONE);
                lltRecent.setVisibility(View.GONE);
                lltScheduled.setVisibility(View.GONE);
            } else {
                rltEmptylayout.setVisibility(View.GONE);
                // List Pending order List
                if (orderResultModel.getPendingOrderDetails() != null && (orderResultModel.getPendingOrderDetails().size() > 0)) {
                    orderPendingModels.clear();
                    rvPendingOrder.setVisibility(View.VISIBLE);
                    orderPendingModels.addAll(orderResultModel.getPendingOrderDetails());
                    for (int o = 0; o < (orderResultModel.getPendingOrderDetails().size() - 1); o++) {
                        /*if(orderPendingAdapter.countDownTimer!=null)
                            orderPendingAdapter.countDownTimer.cancel();*/
                        if (orderPendingAdapter != null && orderPendingAdapter.timerMap.size() > 0) {
                            for (CountDownTimer timer : orderPendingAdapter.timerMap.values()) {
                                timer.cancel();
                            }
                        }
                    }
                    orderPendingAdapter = new OrderPendingAdapter(this, orderPendingModels);
                    rvPendingOrder.setAdapter(orderPendingAdapter);
                } else {
                    rvPendingOrder.setVisibility(View.GONE);
                }
                // List Current order List
                if (orderResultModel.getCurrentOrderDetails() != null && (orderResultModel.getCurrentOrderDetails().size() > 0)) {
                    orderModels.clear();
                    rvOrder.setVisibility(View.VISIBLE);
                    orderModels.addAll(orderResultModel.getCurrentOrderDetails());

                    orderAdapter = new OrderAdapter(this, orderModels);
                    rvOrder.setAdapter(orderAdapter);
                } else {
                    rvOrder.setVisibility(View.GONE);
                }
                // List Out for Delivery order List
                if (orderResultModel.getDeliveryOrderDetails() != null && (orderResultModel.getDeliveryOrderDetails().size() > 0)) {
                    orderOutDeliveryModels.clear();
                    lltOutDeliver.setVisibility(View.VISIBLE);
                    orderOutDeliveryModels.addAll(orderResultModel.getDeliveryOrderDetails());

                    orderOutDeliveryAdapter = new OrderOutDeliveryAdapter(this, orderOutDeliveryModels);
                    rvOutDeliver.setAdapter(orderOutDeliveryAdapter);
                } else {
                    lltOutDeliver.setVisibility(View.GONE);
                }
                // List Completed or recent order List
                if (orderResultModel.getCompletedOrderDetails() != null && (orderResultModel.getCompletedOrderDetails().size() > 0)) {
                    orderRecentModels.clear();
                    lltRecent.setVisibility(View.VISIBLE);
                    orderRecentModels.addAll(orderResultModel.getCompletedOrderDetails());

                    orderRecentAdapter = new OrderRecentAdapter(this, orderRecentModels);
                    rvRecent.setAdapter(orderRecentAdapter);
                    orderRecentAdapter.setOnItemClickListener(new OrderRecentAdapter.onItemClickListener() {
                        @Override
                        public void onItemClickListeners(String type, int orderId, OrderRecentAdapter.RecyclerViewHolder holder) {
                            thumbs(type, orderId, holder);
                        }
                    });
                    orderRecentAdapter.notifyDataSetChanged();
                } else {
                    lltRecent.setVisibility(View.GONE);
                }

                // List Scheduled order List
                if (orderResultModel.getScheduledOrderDetails() != null && (orderResultModel.getScheduledOrderDetails().size() > 0)) {
                    orderScheduleModels.clear();
                    lltScheduled.setVisibility(View.VISIBLE);
                    orderScheduleModels.addAll(orderResultModel.getScheduledOrderDetails());

                    orderScheduledAdapter = new OrderScheduledAdapter(this, orderScheduleModels);
                    rvScheduled.setAdapter(orderScheduledAdapter);
                } else {
                    lltScheduled.setVisibility(View.GONE);
                }
            }
            if (orderResultModel.getDriverIssueModels() != null && (orderResultModel.getDriverIssueModels().size() > 0)) {
                issueModels.clear();
                issueModels.addAll(orderResultModel.getDriverIssueModels());
            }
        }

        nsvOrder.getParent().requestChildFocus(nsvOrder, nsvOrder);
        nsvOrder.scrollTo(0, 0);
        //nsvOrder.fullScroll(View.FOCUS_UP);

    }

    public ArrayList<IssueModel> getIssueModels() {
        return issueModels;
    }


    private void thumbs(String type, final int orderId, final OrderRecentAdapter.RecyclerViewHolder holder) {
        if ("1".equalsIgnoreCase(type)) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.reviewToDriver(sessionManager.getToken(), orderId, 1, "").enqueue(new RequestCallback(REQ_REVIEW_DRIVER, this));
        } else {
            issueId = "";
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.issue_listview_dialog);
            dialog.show();

            ListView lvIssues = (ListView) dialog.findViewById(R.id.lvIssues);
            CustomButton btnCancelIssue = (CustomButton) dialog.findViewById(R.id.btnCancelIssue);
            CustomButton btnOkIssue = (CustomButton) dialog.findViewById(R.id.btnOkIssue);

            alertListAdapter = new AlertListAdapter(issueModels, getApplicationContext());
            lvIssues.setAdapter(alertListAdapter);
            btnCancelIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    issueId = "";
                    //commonMethods.showProgressDialog(MainActivity.this, customDialog);
                    //apiService.reviewToDriver(sessionManager.getToken(), orderId, 0, issueId).enqueue(new RequestCallback(REQ_REVIEW_DRIVER, MainActivity.this));
                    //getOrders(true);
                    dialog.dismiss();
                }
            });
            alertListAdapter.setOnItemClickListener(new AlertListAdapter.onItemClickListener() {
                @Override
                public void onItemClickListeners(int id, int positionz) {
                    issueId = "";
                    for (int i = 0; i < issueModels.size(); i++) {
                        if (!issueModels.get(i).isSelected()) {
                            issueModels.get(i).getIssueId();

                            if (TextUtils.isEmpty(issueId)) {
                                issueId = issueId + issueModels.get(i).getIssueId();
                            } else {
                                issueId = issueId + "," + issueModels.get(i).getIssueId();
                            }
                        }
                    }
                }
            });
            btnOkIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivThumbsUp.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                    holder.ivThumbsDown.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);

                    holder.ivThumbsDown.setEnabled(false);
                    holder.ivThumbsUp.setEnabled(false);
                    commonMethods.showProgressDialog(MainActivity.this, customDialog);

                    apiService.reviewToDriver(sessionManager.getToken(), orderId, 0, issueId).enqueue(new RequestCallback(REQ_REVIEW_DRIVER, MainActivity.this));
                    dialog.dismiss();

                }
            });


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

                    //String message = intent.getStringExtra("message");
//{custom={"order_data":{"order_item_count":1,"total_seconds":60,"user_image":"http:\/\/localhost\/product\/gofereats\/public\/images\/user.png","user_name":"abdul hameed","id":41,"status_text":"pending","remaining_seconds":60},"type":"new_order","order_id":41}}
                    String JSON_DATA = sessionManager.getPushNotification();

                    try {
                        JSONObject jsonObject = new JSONObject(JSON_DATA);
                        if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("new_order") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("schedule_order")) {
                            getOrders(true);
                        } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_cancelled")) {
                            rvPendingOrder.setVisibility(View.GONE);
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
        isMainActivity = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMainActivity = true;
        if (orderPendingAdapter != null && orderPendingAdapter.timerMap.size() > 0) {
            for (CountDownTimer timer : orderPendingAdapter.timerMap.values()) {
                timer.cancel();
            }
        }

        getOrders(true);

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    public void onBackPressed() {

        if (backPressed >= 1) {
            finishAffinity();
            super.onBackPressed();
        } else {
            // clean up
            backPressed = backPressed + 1;
            Toast.makeText(this, R.string.back_to_exit, Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick for Accept or pause dialog
     **/
    class DialogInject {

        public @InjectView(R.id.ivAcceptOrderTick)
        ImageView ivAcceptOrderTick;
        public @InjectView(R.id.ivPauseOrderTick)
        ImageView ivPauseOrderTick;


        public @InjectView(R.id.rltAcceptOrder)
        RelativeLayout rltAcceptOrder;
        public @InjectView(R.id.rltPauseOrder)
        RelativeLayout rltPauseOrder;
        public @InjectView(R.id.rltOrderStatus)
        RelativeLayout rltOrderStatus;

        @OnClick(R.id.rltAcceptOrder)
        public void acceptOrder() {
            // rltOrderStatus.setVisibility(View.GONE);
            updateStatus(true);
            isActive = true;
            updateRestaurantStatus(1);
            ivPauseOrderTick.setVisibility(View.GONE);
            ivAcceptOrderTick.setVisibility(View.VISIBLE);
            dialog1.dismiss();
        }

        @OnClick(R.id.rltPauseOrder)
        public void pauseOrder() {
            //rltOrderStatus.setVisibility(View.GONE);
            updateRestaurantStatus(0);
            isActive = false;
            updateStatus(false);
            ivPauseOrderTick.setVisibility(View.VISIBLE);
            ivAcceptOrderTick.setVisibility(View.GONE);
            dialog1.dismiss();
        }

        @OnClick(R.id.rltOrderStatus)
        public void dialogDismiss() {
            dialog1.dismiss();
        }

    }
}
