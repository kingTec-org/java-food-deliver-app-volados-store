package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category OrderDetails
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.OrderDetailsAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsAllModel;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsModel;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsResultModel;
import gofereatsrestarant.datamodels.orderdetails.OrderStatusModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.pushnotification.Config;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.commondialog.ShowDialog;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;
import gofereatsrestarant.views.main.menu.ResendOrderRequest;

import static gofereatsrestarant.utils.Enums.REQ_DELAY_ORDER;
import static gofereatsrestarant.utils.Enums.REQ_DONE_ORDER;
import static gofereatsrestarant.utils.Enums.REQ_ORDER_DETAILS;
import static gofereatsrestarant.utils.Enums.REQ_REFRESH;

/*****************************************************************
 Selected Order Details
 ****************************************************************/
public class OrderDetails extends AppCompatActivity implements ServiceListener {

    public static boolean isShown = false;
    public static boolean isReFresh = false;
    public static boolean isLoad = false;
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

    public @InjectView(R.id.rvOrderDetails)
    CustomRecyclerView rvOrderDetails;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.nsvOrderDetails)
    NestedScrollView nsvOrderDetails;
    public @InjectView(R.id.orderCancel)
    View orderCancel;
    public @InjectView(R.id.driverDetails)
    View driverDetails;
    public @InjectView(R.id.tvOrderId)
    CustomTextView tvOrderId;
    public @InjectView(R.id.tvPickup)
    CustomTextView tvPickup;
    public @InjectView(R.id.ivStep1)
    ImageView ivStep1;
    public @InjectView(R.id.ivStep2)
    ImageView ivStep2;
    public @InjectView(R.id.ivStep3)
    ImageView ivStep3;
    public @InjectView(R.id.tvAcceptTime)
    CustomTextView tvAcceptTime;
    public @InjectView(R.id.tvPickupTime)
    CustomTextView tvPickupTime;
    public @InjectView(R.id.tvDeliveryTime)
    CustomTextView tvDeliveryTime;
    public @InjectView(R.id.civUserImage)
    CircleImageView civUserImage;
    public @InjectView(R.id.civDriverImage)
    CircleImageView civDriverImage;
    public @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    public @InjectView(R.id.driverprogressBar)
    ProgressBar driverprogressBar;
    public @InjectView(R.id.tvUserName)
    CustomTextView tvUserName;
    public @InjectView(R.id.tvUserPhone)
    CustomTextView tvUserPhone;
    public @InjectView(R.id.tvSupport)
    CustomTextView tvSupport;
    public @InjectView(R.id.tvSupportno)
    CustomTextView tvSupportno;
    public @InjectView(R.id.tvSubtotalPrice)
    CustomTextView tvSubtotalPrice;
    public @InjectView(R.id.tvTaxPrice)
    CustomTextView tvTaxPrice;
    public @InjectView(R.id.tvPaidPrice)
    CustomTextView tvPaidPrice;
    public @InjectView(R.id.btnDelayOrder)
    CustomButton btnDelayOrder;
    public @InjectView(R.id.lltOrderData)
    LinearLayout lltOrderData;
    public @InjectView(R.id.lltFoodDone)
    LinearLayout lltFoodDone;
    public @InjectView(R.id.lltOrderCancel)
    LinearLayout lltOrderCancel;
    public @InjectView(R.id.btnCancelOrder)
    CustomButton btnCancelOrder;
    public @InjectView(R.id.btnFoodDone)
    CustomButton btnFoodDone;
    //public @InjectView(R.id.swipeContainer)  SwipeRefreshLayout swipeContainer;
    public @InjectView(R.id.btnTrackOrder)
    CustomButton btnTrackOrder;
    public @InjectView(R.id.ivMapTrack)
    ImageView ivMapTrack;
    public @InjectView(R.id.ivCancelOrder)
    ImageView ivCancelOrder;
    public @InjectView(R.id.tvOrderReadytext)
    CustomTextView tvOrderReadytext;
    public @InjectView(R.id.tvContactno)
    CustomTextView tvContactno;
    public @InjectView(R.id.tvOrderNotes)
    CustomTextView tvOrderNotes;
    public @InjectView(R.id.rltOrderNotes)
    RelativeLayout rltOrderNotes;
    public @InjectView(R.id.orderStatus)
    View orderStatus;
    public @InjectView(R.id.tvPickupLater)
    CustomTextView tvPickupLater;
    public @InjectView(R.id.tvAccessPrice)
    CustomTextView tvAccessPrice;
    public @InjectView(R.id.rltAccessFee)
    RelativeLayout rltAccessFee;
    public @InjectView(R.id.rltTax)
    RelativeLayout rltTax;
    public @InjectView(R.id.rltPenaltyFee)
    RelativeLayout rltPenaltyFee;
    public @InjectView(R.id.tvPenaltyPrice)
    CustomTextView tvPenaltyPrice;
    public @InjectView(R.id.rltAppliedPenalty)
    RelativeLayout rltAppliedPenalty;
    public @InjectView(R.id.tvAppliedPenaltyPrice)
    CustomTextView tvAppliedPenaltyPrice;
    public @InjectView(R.id.lltAmount)
    LinearLayout lltAmount;
    public @InjectView(R.id.tvDriverName)
    CustomTextView tvDriverName;
    public @InjectView(R.id.tvDriverNumber)
    CustomTextView tvDriverNumber;
    public @InjectView(R.id.tvVechileNumber)
    CustomTextView tvVechileNumber;
    private ArrayList markerPoints = new ArrayList();
    private Integer status = -1;
    private AlertDialog dialog;
    private BroadcastReceiver mBroadcastReceiver;
    private OrderDetailsAdapter orderDetailsAdapter;
    private ArrayList<OrderDetailsModel> orderDetailsModels = new ArrayList<>();
    private int orderId = 0;
    private int orderType = 0;

    @OnClick(R.id.btnDelayOrder)
    public void setDelayOrder() {
        isShown = true;
        Intent cancel = new Intent(this, DelayOrderActivity.class);
        cancel.putExtra("orderId", orderId);
        startActivityForResult(cancel, REQ_DELAY_ORDER);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @OnClick(R.id.btnCancelOrder)
    public void setCancelOrder() {
        isShown = true;
        Intent cancel = new Intent(this, CancelActivity.class);
        cancel.putExtra("orderId", orderId);
        startActivity(cancel);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @OnClick(R.id.btnFoodDone)
    public void setDoneOrder() {
        doneOrders();
    }

    @OnClick(R.id.btnTrackOrder)
    public void setTrackOrder() {
        isShown = true;
        Intent maps = new Intent(this, MapsActivity.class);
        maps.putExtra("PickupDrop", markerPoints);
        maps.putExtra("status", status);
        maps.putExtra("orderId", orderId);
        startActivityForResult(maps, REQ_REFRESH);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @OnClick(R.id.ivMapTrack)
    public void mapTrack() {
        isShown = true;
        Intent maps = new Intent(this, MapsActivity.class);
        maps.putExtra("PickupDrop", markerPoints);
        maps.putExtra("status", status);
        maps.putExtra("orderId", orderId);
        startActivityForResult(maps, REQ_REFRESH);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @OnClick(R.id.ivCancelOrder)
    public void cancelOrder() {
        if (orderCancel.getVisibility() == View.VISIBLE) {
            orderCancel.setVisibility(View.GONE);
        } else {
            orderCancel.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        isShown = true;
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack, this);
        tvTitle.setText(getResources().getString(R.string.order_details));
        vBottom.setVisibility(View.VISIBLE);
        lltOrderData.setVisibility(View.GONE);
        lltFoodDone.setVisibility(View.GONE);

        nsvOrderDetails.fullScroll(View.FOCUS_UP);
        nsvOrderDetails.scrollTo(0, 0);
        rvOrderDetails.setFocusable(false);
        nsvOrderDetails.setFocusable(true);

        getIntents();
        initRecyclerView();
        getOrdersDetails(true, "OnCreate");
        receivePushNotification();
    }

    /**
     * Get intent data from previous activity
     **/
    public void getIntents() {
        orderId = getIntent().getIntExtra("orderId", 0);
        orderType = getIntent().getIntExtra("ordertype", 0);
    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvOrderDetails.setLayoutManager(customLayoutManager);

        orderDetailsAdapter = new OrderDetailsAdapter(this);
        rvOrderDetails.setAdapter(orderDetailsAdapter);
        rvOrderDetails.setHasFixedSize(true);


        // Setup refresh listener which triggers new data loading
       /* swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getOrdersDetails(false);
            }
        });*/

        nsvOrderDetails.fullScroll(View.FOCUS_UP);
        nsvOrderDetails.scrollTo(0, 0);

    }

    /**
     * Call API to get Order details
     */
    public void getOrdersDetails(boolean isLoad, String type) {
        System.out.println("Order details called status" + type);
        if (isLoad) commonMethods.showProgressDialog(this, customDialog);
        isReFresh = false;
        apiService.orderDetails(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_ORDER_DETAILS, this));
    }

    /**
     * Call API to delay order
     */
    public void delayOrders() {
        commonMethods.showProgressDialog(this, customDialog);
        //apiService.delayOrders(orderId,sessionManager.getToken()).enqueue(new RequestCallback(REQ_DELAY_ORDER,this));
    }

    /**
     * Call API to order done
     */
    public void doneOrders() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.doneOrders(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_DONE_ORDER, this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_ORDER_DETAILS:
                //swipeContainer.setRefreshing(false);
                if (jsonResp.isSuccess()) {
                    onSuccessOrderDetails(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }

                break;
            case REQ_DELAY_ORDER:
                /*if (jsonResp.isSuccess()) {
                    //onSuccessOrderDetails(jsonResp);
                } else*/
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_DONE_ORDER:
                if (jsonResp.isSuccess()) {
                    onSuccessDoneOrder();
                    getOrdersDetails(true, "Order Done");
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
        switch (jsonResp.getRequestCode()) {

            case REQ_DONE_ORDER:
                break;
            default:
                if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
        }

    }

    public void onSuccessDoneOrder() {
        lltFoodDone.setVisibility(View.GONE);
        //lltOrderCancel.setVisibility(View.GONE);
        btnTrackOrder.setVisibility(View.VISIBLE);
        ivStep1.setImageDrawable(getResources().getDrawable(R.drawable.check1));
        ivStep2.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
        ivStep3.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
    }

    /**
     * Functionality of successful order details api call
     *
     * @param jsonResponse json response of successful order details api call
     */

    public void onSuccessOrderDetails(JsonResponse jsonResponse) {

        lltFoodDone.setVisibility(View.GONE);
        lltOrderCancel.setVisibility(View.GONE);
        OrderDetailsResultModel orderDetailsResultModel = gson.fromJson(jsonResponse.getStrResponse(), OrderDetailsResultModel.class);
        if (orderDetailsResultModel != null) {
            // List Pending order List
            OrderDetailsAllModel orderDetailsAllModel = orderDetailsResultModel.getOrderDetailsAllModels();
            if (orderDetailsAllModel != null && orderDetailsAllModel.getItemDetails() != null && orderDetailsAllModel.getItemDetails().size() > 0) {
                orderDetailsModels.clear();
                rvOrderDetails.setVisibility(View.VISIBLE);
                orderDetailsModels.addAll(orderDetailsAllModel.getItemDetails());

                orderDetailsAdapter = new OrderDetailsAdapter(this, orderDetailsModels);
                rvOrderDetails.setAdapter(orderDetailsAdapter);
            } else {
                rvOrderDetails.setVisibility(View.GONE);
            }

            /**
             * @TripStatus 'pending' => 0, Driver accept the request
             * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
             * 'declined' => 2,  Driver declined the order list
             * 'started' => 3,  Driver start the trip (pickup the order)
             * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
             * 'completed' => 5, Driver complete the trip
             * 'cancelled' => 6, Driver or restaurant cancel the trip
             */
            status = orderDetailsAllModel.getOrderDeliveryStatus();
            if (status != null && (status == 0 || status == 1 || status == 3 || status == 4) && (!TextUtils.isEmpty(orderDetailsAllModel.getPickupLatitude()) || !TextUtils.isEmpty(orderDetailsAllModel.getDropLatitude()))) {
                ivMapTrack.setVisibility(View.GONE);
                btnTrackOrder.setVisibility(View.VISIBLE);
                LatLng pickupLatlng = new LatLng(Double.valueOf(orderDetailsAllModel.getPickupLatitude()), Double.valueOf(orderDetailsAllModel.getPickupLongitude()));
                LatLng dropLatlng = new LatLng(Double.valueOf(orderDetailsAllModel.getDropLatitude()), Double.valueOf(orderDetailsAllModel.getDropLongitude()));
                LatLng driverLatlng = new LatLng(Double.valueOf(orderDetailsAllModel.getDriverLatitude()), Double.valueOf(orderDetailsAllModel.getDriverLongitude()));
                markerPoints.add(0, pickupLatlng);
                markerPoints.add(1, dropLatlng);
                markerPoints.add(2, driverLatlng);
            } else {
                ivMapTrack.setVisibility(View.GONE);
                btnTrackOrder.setVisibility(View.GONE);
            }

            if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                tvOrderId.setText(getResources().getString(R.string.order_id) + " #"+orderDetailsAllModel.getOrderId());
            else
                tvOrderId.setText(getResources().getString(R.string.order_id) + orderDetailsAllModel.getOrderId()+ "# ");
            tvPickup.setText(orderDetailsAllModel.getOrderStatus());
            tvSubtotalPrice.setText(sessionManager.getCurrencySymbol() + orderDetailsAllModel.getSubTotal());
            if (orderDetailsAllModel.getAccessFee() != null && Float.valueOf(orderDetailsAllModel.getAccessFee()) > 0) {
                rltAccessFee.setVisibility(View.VISIBLE);
                tvAccessPrice.setText("-" + sessionManager.getCurrencySymbol() + orderDetailsAllModel.getAccessFee());
            } else {
                rltAccessFee.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getTax() != null && Float.valueOf(orderDetailsAllModel.getTax()) > 0) {
                rltTax.setVisibility(View.VISIBLE);
                tvTaxPrice.setText(sessionManager.getCurrencySymbol() + orderDetailsAllModel.getTax());
            } else {
                rltTax.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getRestaurantPenality() != null && Float.valueOf(orderDetailsAllModel.getRestaurantPenality()) > 0) {
                //rltPenaltyFee.setVisibility(View.VISIBLE);  //if the  Penalty amount to show uncomment the visible
                tvPenaltyPrice.setText("-" + sessionManager.getCurrencySymbol() + orderDetailsAllModel.getRestaurantPenality());
            } else {
                rltPenaltyFee.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getAppliedPenality() != null && Float.valueOf(orderDetailsAllModel.getAppliedPenality()) > 0) {
                //rltAppliedPenalty.setVisibility(View.VISIBLE);  //if the applied Penalty amount to show uncomment the visible
                tvAppliedPenaltyPrice.setText("-" + sessionManager.getCurrencySymbol() + orderDetailsAllModel.getAppliedPenality());
            } else {
                rltAppliedPenalty.setVisibility(View.GONE);
            }
            tvPaidPrice.setText(sessionManager.getCurrencySymbol() + orderDetailsAllModel.getTotal());
            if (TextUtils.isEmpty(orderDetailsAllModel.getOrderNotes()) || orderDetailsAllModel.getOrderNotes() == null) {
                rltOrderNotes.setVisibility(View.GONE);
            } else {
                tvOrderNotes.setText(orderDetailsAllModel.getOrderNotes());
                tvOrderNotes.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tvOrderNotes.getLineCount();

                        // Action Using lineCount
                        if (lineCount > 1) {
                            tvOrderNotes.setGravity(Gravity.START);
                        } else {
                            tvOrderNotes.setGravity(Gravity.END);
                        }
                    }
                });

                rltOrderNotes.setVisibility(View.VISIBLE);
            }

            imageUtils.loadImage(this, civUserImage, progressBar, orderDetailsAllModel.getUserImage());
            tvUserName.setText(orderDetailsAllModel.getUserName());
            tvUserPhone.setText(orderDetailsAllModel.getUserPhone());

            OrderStatusModel orderStatusModel = orderDetailsAllModel.getStatusTimes();
            if (orderStatusModel != null) {
                tvAcceptTime.setText(orderStatusModel.getAccepted());
                tvPickupTime.setText(orderStatusModel.getDelivery());
                tvDeliveryTime.setText(orderStatusModel.getCompleted());
                if (orderDetailsAllModel.getOrderStatus().equals("accepted") && orderType == 1) {
                    tvPickupLater.setVisibility(View.VISIBLE);
                    tvPickup.setTextSize(14f);
                    tvPickup.setGravity(Gravity.CENTER);
                    tvPickup.setPadding(0, 5, 0, 5);
                    tvPickup.setText(getResources().getString(R.string.scheduled) + "\n" + orderDetailsAllModel.getOrderDate() + " " + orderDetailsAllModel.getOrderTime());
                    orderStatus.setVisibility(View.GONE);
                    lltFoodDone.setVisibility(View.GONE);
                    lltOrderCancel.setVisibility(View.VISIBLE);
                    btnDelayOrder.setVisibility(View.GONE);
                } else if (orderDetailsAllModel.getOrderStatus().equals("accepted")) {
                    tvPickup.setText(getResources().getString(R.string.pickup) +" " +orderStatusModel.getDelivery().replaceAll(" ",""));
                    System.out.println("Orderstatusmodel"+ orderStatusModel.getDelivery());
                    ivStep1.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                    ivStep1.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                    ivStep2.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
                    ivStep3.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
                    if (orderDetailsAllModel.getIsRequest() == 0) {
                        lltFoodDone.setVisibility(View.VISIBLE);
                        lltOrderCancel.setVisibility(View.VISIBLE);
                        tvOrderReadytext.setText(getResources().getString(R.string.order_msg));
                    } else {
                        lltFoodDone.setVisibility(View.VISIBLE);
                        lltOrderCancel.setVisibility(View.VISIBLE);
                        btnFoodDone.setText(getResources().getString(R.string.tryagain));
                        tvOrderReadytext.setText(getResources().getString(R.string.contact_admin) + "-" );
                        tvContactno.setText( orderDetailsAllModel.getSupportPhone());
                    }
                } else if (orderDetailsAllModel.getOrderStatus().equals("delivery")) {
                    tvPickup.setText(getResources().getString(R.string.pickup) + " " + orderStatusModel.getDelivery());
                    ivStep1.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                    ivStep1.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                    ivStep3.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
                    if (orderDetailsAllModel.getOrderDeliveryStatus() == 3 || orderDetailsAllModel.getOrderDeliveryStatus() == 4) {
                        lltOrderCancel.setVisibility(View.GONE);
                        ivStep2.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                        ivStep2.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                    } else {
                        lltOrderCancel.setVisibility(View.VISIBLE);
                        ivStep2.setImageDrawable(getResources().getDrawable(R.drawable.check1gray));
                    }
                } else if (orderDetailsAllModel.getOrderStatus().equals("completed")) {
                    tvPickup.setText(getResources().getString(R.string.completed) + "  " + orderDetailsAllModel.getCompletedDateTime());
                    ivStep1.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                    ivStep1.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                    ivStep2.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                    ivStep2.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                    ivStep3.setColorFilter(ContextCompat.getColor(this, R.color.apptheme), PorterDuff.Mode.SRC_IN);
                    ivStep3.setImageDrawable(getResources().getDrawable(R.drawable.check1));
                }
                /**
                 *  Note Driver Values are Static in Model
                 */
                if (orderDetailsAllModel.getDriverName() != null && !TextUtils.isEmpty(orderDetailsAllModel.getDriverName())) {
                    driverDetails.setVisibility(View.VISIBLE);
                    imageUtils.loadImage(this, civDriverImage, driverprogressBar, orderDetailsAllModel.getDriverImage());
                    tvDriverName.setText(orderDetailsAllModel.getDriverName());
                    tvDriverNumber.setText(orderDetailsAllModel.getDriverContactNumber());
                    if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                        tvVechileNumber.setText(orderDetailsAllModel.getDriverVehicleNumber() + "  " + getResources().getString(R.string.courier) + " • " + orderDetailsAllModel.getDriverVehicleType());
                    else
                        tvVechileNumber.setText(orderDetailsAllModel.getDriverVehicleType() + " • "+ getResources().getString(R.string.courier)  + "  " +orderDetailsAllModel.getDriverVehicleNumber() );
                } else {
                    driverDetails.setVisibility(View.GONE);
                }
                if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction))&& orderDetailsAllModel.getSupportPhone().equalsIgnoreCase("-")) {
                    tvSupport.setText(getResources().getString(R.string.support)+" ");
                    tvSupportno.setText(orderDetailsAllModel.getSupportPhone());
                }
                else {
                    tvSupport.setText(getResources().getString(R.string.support)+" ");
                    tvSupportno.setText(orderDetailsAllModel.getSupportPhone());
                }
                System.out.println("Contact Number"+ orderDetailsAllModel.getSupportPhone());
            }
            lltOrderData.setVisibility(View.VISIBLE);
            commonMethods.hideProgressDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_DELAY_ORDER || requestCode == REQ_REFRESH) {
            getOrdersDetails(true, "Delay order");
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

                    String JSON_DATA = sessionManager.getPushNotification();

                    try {
                        JSONObject jsonObject = new JSONObject(JSON_DATA);
                        if (jsonObject.getJSONObject("custom").has("type")) {
                            if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("no_drivers_found")) {
                                int orderId = jsonObject.getJSONObject("custom").getInt("order_id");
                                String title = jsonObject.getJSONObject("custom").getString("title");
                                String supportNumber = jsonObject.getJSONObject("custom").getString("support_mobile");
                                commonMethods.hideProgressDialog();

                                Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                                dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialogs.putExtra("message", title);
                                dialogs.putExtra("type", 0);
                                startActivity(dialogs);

                                tryAgain(supportNumber, orderId);
                            } else if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("driver_accepted")) {
                                onSuccessDoneOrder();

                            } else if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_completed") || jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_started")) {
                                getOrdersDetails(true, "Delivery");
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
        //getOrdersDetails(false);

        if (!sessionManager.getDriverUpdatedLat().equals("") && !sessionManager.getDriverUpdatedLng().equals("")) {
            LatLng driverLatlng = new LatLng(Double.valueOf(sessionManager.getDriverUpdatedLat()), Double.valueOf(sessionManager.getDriverUpdatedLng()));
            if (markerPoints.size() > 1) {
                markerPoints.add(2, driverLatlng);
            }
        }
        isShown = false;
        if (isLoad){
            isLoad=false;
            commonMethods.hideProgressDialog();
        }
        if (OrderDetails.isReFresh) {
            isReFresh = false;
            getOrdersDetails(true, "Referesh");
        }
    }

    /**
     * To resend order request
     *
     * @param message       message to be sent
     * @param orderId       id of the order
     * @param supportNumber support number
     */

    public void resend(String message, String orderId, String supportNumber) {
        Intent orderDetails = new Intent(this, ResendOrderRequest.class);
        orderDetails.putExtra("message", message);
        orderDetails.putExtra("orderId", orderId);
        orderDetails.putExtra("supportNumber", supportNumber);
        startActivity(orderDetails);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    private void tryAgain(String supportNumber, int order_Id) {
        if (orderId == order_Id) {
            lltFoodDone.setVisibility(View.VISIBLE);
            lltOrderCancel.setVisibility(View.VISIBLE);
            btnFoodDone.setText(getString(R.string.tryagain));
            tvOrderReadytext.setText(getResources().getString(R.string.contact_admin) );
            tvContactno.setText(" " + supportNumber);
        }
    }

}