package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category OrderHistoryDetails
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.OrderDetailsAdapter;
import gofereatsrestarant.adapters.main.alert.AlertListAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.IssueModel;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsAllModel;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsModel;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsResultModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;

import static gofereatsrestarant.utils.Enums.REQ_ORDER_DETAILS;
import static gofereatsrestarant.utils.Enums.REQ_REVIEW_DRIVER;

/*****************************************************************
 Selected completed or cancel Order History Details
 ****************************************************************/
public class OrderHistoryDetails extends AppCompatActivity implements ServiceListener {

    private static ArrayList<IssueModel> issueModelArrayList = new ArrayList<>();
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
    public @InjectView(R.id.rvOrderHistoryDetails)
    CustomRecyclerView rvOrderHistoryDetails;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.nsvOrderDetails)
    NestedScrollView nsvOrderDetails;
    public @InjectView(R.id.tvOrderId)
    CustomTextView tvOrderId;
    public @InjectView(R.id.tvUserName)
    CustomTextView tvUserName;
    public @InjectView(R.id.tvDriverName)
    CustomTextView tvDriverName;
    public @InjectView(R.id.tvOrderStatus)
    CustomTextView tvOrderStatus;
    public @InjectView(R.id.tvPrice)
    CustomTextView tvPrice;
    public @InjectView(R.id.tvSubtotalPrice)
    CustomTextView tvSubtotalPrice;
    public @InjectView(R.id.tvTaxPrice)
    CustomTextView tvTaxPrice;
    public @InjectView(R.id.tvPaidPrice)
    CustomTextView tvPaidPrice;
    public @InjectView(R.id.tvPickup)
    CustomTextView tvPickup;
    public @InjectView(R.id.tvDrop)
    CustomTextView tvDrop;
    public @InjectView(R.id.civUserImage)
    CircleImageView civUserImage;
    public @InjectView(R.id.progressBarUser)
    ProgressBar progressBarUser;
    public @InjectView(R.id.civDriverImage)
    CircleImageView civDriverImage;
    public @InjectView(R.id.progressBarDriver)
    ProgressBar progressBarDriver;
    public @InjectView(R.id.lltDriver)
    LinearLayout lltDriver;
    public @InjectView(R.id.lltUser)
    LinearLayout lltUser;
    public @InjectView(R.id.rltorderhead)
    RelativeLayout rltorderhead;
    public @InjectView(R.id.lltdrop)
    LinearLayout lltdrop;
    public @InjectView(R.id.lltPick)
    LinearLayout lltpick;
    public @InjectView(R.id.rltReviews)
    RelativeLayout rltReviews;
    public @InjectView(R.id.ivThumbsUp)
    ImageView ivThumbsUp;
    public @InjectView(R.id.ivThumbsDown)
    ImageView ivThumbsDown;
    public @InjectView(R.id.tvAccessPrice)
    CustomTextView tvAccessPrice;
    public @InjectView(R.id.rltTax)
    RelativeLayout rltTax;
    public @InjectView(R.id.rltAccessFee)
    RelativeLayout rltAccessFee;
    public @InjectView(R.id.rltPenaltyFee)
    RelativeLayout rltPenaltyFee;
    public @InjectView(R.id.tvPenaltyPrice)
    CustomTextView tvPenaltyPrice;
    public @InjectView(R.id.rltAppliedPenalty)
    RelativeLayout rltAppliedPenalty;
    public @InjectView(R.id.tvAppliedPenaltyPrice)
    CustomTextView tvAppliedPenaltyPrice;
    public int type = 0;
    private String issueId = "";
    private int myOrder = 0;
    private AlertListAdapter alertListAdapter;
    private MainActivity mainActivity;
    private OrderDetailsAdapter orderDetailsAdapter;
    private ArrayList<OrderDetailsModel> orderDetailsModels = new ArrayList<>();
    private int orderId = 0;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.ivThumbsUp)
    public void thumbsUp() {
        ivThumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.appgreen), android.graphics.PorterDuff.Mode.MULTIPLY);
        ivThumbsDown.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        thumbs("1", myOrder);
    }

    @OnClick(R.id.ivThumbsDown)
    public void thumbsDown() {
        ivThumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        ivThumbsDown.setColorFilter(ContextCompat.getColor(this, R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);
        thumbs("0", myOrder);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack, this);
        tvTitle.setText(getResources().getString(R.string.order_details));
        vBottom.setVisibility(View.VISIBLE);

        mainActivity = new MainActivity();
        issueModelArrayList = mainActivity.getIssueModels();

        nsvOrderDetails.fullScroll(View.FOCUS_UP);
        nsvOrderDetails.scrollTo(0, 0);
        rvOrderHistoryDetails.setFocusable(false);
        nsvOrderDetails.setFocusable(true);

        getIntents();
        getOrdersDetails();
        initRecyclerView();
    }

    @Override
    public void onBackPressed() {
        if (type == 1) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            home.putExtra("type", "order");
            startActivity(home);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * To call api service while thumbs up and down based on type
     *
     * @param type    thumbs up down type
     * @param orderId particular order
     */

    private void thumbs(String type, final int orderId) {
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

            alertListAdapter = new AlertListAdapter(issueModelArrayList, getApplicationContext());
            lvIssues.setAdapter(alertListAdapter);
            btnCancelIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    issueId = "";
                    commonMethods.showProgressDialog(OrderHistoryDetails.this, customDialog);
                    apiService.reviewToDriver(sessionManager.getToken(), orderId, 0, issueId).enqueue(new RequestCallback(REQ_REVIEW_DRIVER, OrderHistoryDetails.this));
                    dialog.dismiss();
                }
            });
            alertListAdapter.setOnItemClickListener(new AlertListAdapter.onItemClickListener() {
                @Override
                public void onItemClickListeners(int id, int positionz) {
                    issueId = "";
                    for (int i = 0; i < issueModelArrayList.size(); i++) {
                        if (!issueModelArrayList.get(i).isSelected()) {
                            issueModelArrayList.get(i).getIssueId();

                            if (TextUtils.isEmpty(issueId)) {
                                issueId = issueId + issueModelArrayList.get(i).getIssueId();
                            } else {
                                issueId = issueId + "," + issueModelArrayList.get(i).getIssueId();
                            }
                        }
                    }
                }
            });
            btnOkIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    commonMethods.showProgressDialog(OrderHistoryDetails.this, customDialog);
                    apiService.reviewToDriver(sessionManager.getToken(), orderId, 0, issueId).enqueue(new RequestCallback(REQ_REVIEW_DRIVER, OrderHistoryDetails.this));
                    dialog.dismiss();

                }
            });


        }

    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvOrderHistoryDetails.setLayoutManager(customLayoutManager);

        orderDetailsAdapter = new OrderDetailsAdapter(this);
        rvOrderHistoryDetails.setAdapter(orderDetailsAdapter);
        rvOrderHistoryDetails.setHasFixedSize(true);

    }

    /**
     * Get intent data from previous activity
     **/
    public void getIntents() {
        orderId = getIntent().getIntExtra("orderId", 0);
        type = getIntent().getIntExtra("type", 0);
    }

    /**
     * Call API to get Order details
     */
    public void getOrdersDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.orderDetails(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_ORDER_DETAILS, this));
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
                if (jsonResp.isSuccess()) {
                    onSuccessViewOrder(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_REVIEW_DRIVER:
                if (jsonResp.isSuccess()) {
                    onSuccessReview();
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

    public void onSuccessViewOrder(JsonResponse jsonResp) {
        OrderDetailsResultModel orderDetailsResultModel = gson.fromJson(jsonResp.getStrResponse(), OrderDetailsResultModel.class);
        if (orderDetailsResultModel != null && orderDetailsResultModel.getOrderDetailsAllModels() != null) {
            OrderDetailsAllModel orderDetailsAllModel = orderDetailsResultModel.getOrderDetailsAllModels();
            orderDetailsModels.clear();
            orderDetailsModels.addAll(orderDetailsAllModel.getItemDetails());
            //OrderStatusModel orderStatusModel = orderDetailsAllModel.getStatusTimes();
            if (orderDetailsModels.size() > 0) {
                rltorderhead.setVisibility(View.VISIBLE);
            } else {
                rltorderhead.setVisibility(View.GONE);
            }
            orderDetailsAdapter = new OrderDetailsAdapter(this, orderDetailsModels);
            rvOrderHistoryDetails.setAdapter(orderDetailsAdapter);
            if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
                tvOrderId.setText(getResources().getString(R.string.order_id) + " #"+ orderDetailsAllModel.getOrderId());
            else
                tvOrderId.setText(getResources().getString(R.string.order_id)+ orderDetailsAllModel.getOrderId()+ "# ");
            myOrder = orderDetailsAllModel.getOrderId();
            tvUserName.setText(orderDetailsAllModel.getUserName());
            tvDriverName.setText(orderDetailsAllModel.getDriverName());
            if (orderDetailsAllModel.getDriverName().equals("")) {
                lltDriver.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getUserName().equals("")) {
                lltUser.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getOrderStatus().equals("cancelled")) {
                tvOrderStatus.setText(getResources().getString(R.string.cancelled) + "  " + orderDetailsAllModel.getCompletedDateTime());
                rltReviews.setVisibility(View.GONE);
            } else if (orderDetailsAllModel.getOrderStatus().equals("completed")) {
                tvOrderStatus.setText(getResources().getString(R.string.completed) + "  " + orderDetailsAllModel.getCompletedDateTime());
                rltReviews.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(orderDetailsAllModel.getRestaurantDriverThumbs())) {
                    if (orderDetailsAllModel.getRestaurantDriverThumbs().equals("0")) {
                        ivThumbsUp.setEnabled(false);
                        ivThumbsDown.setEnabled(false);
                        ivThumbsDown.setColorFilter(ContextCompat.getColor(this, R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        ivThumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.appgreen), android.graphics.PorterDuff.Mode.MULTIPLY);
                        ivThumbsUp.setEnabled(false);
                        ivThumbsDown.setEnabled(false);
                    }
                }
            }
            tvPrice.setText(sessionManager.getCurrencySymbol() + orderDetailsAllModel.getTotal());
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
            if (orderDetailsAllModel.getPickupLocation() == null || orderDetailsAllModel.getPickupLocation().equals("")) {
                lltpick.setVisibility(View.GONE);
            }
            if (orderDetailsAllModel.getDropLocation() == null || orderDetailsAllModel.getDropLocation().equals("")) {
                lltdrop.setVisibility(View.GONE);
            }
                /*if (!TextUtils.isEmpty(orderDetailsAllModel.getRestaurantDriverThumbs())) {
                    if (orderDetailsAllModel.getRestaurantDriverThumbs().equals("0")) {
                        ivThumbsUp.setEnabled(false);
                        ivThumbsDown.setEnabled(false);
                        ivThumbsDown.setColorFilter(ContextCompat.getColor(this, R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        ivThumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.app_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                        ivThumbsUp.setEnabled(true);
                        ivThumbsDown.setEnabled(true);
                    }
                }*/
            tvPickup.setText(orderDetailsAllModel.getPickupLocation());
            tvDrop.setText(orderDetailsAllModel.getDropLocation());
            if (!orderDetailsAllModel.getUserImage().equals("")) {
                imageUtils.loadImage(this, civUserImage, progressBarUser, orderDetailsAllModel.getUserImage());
            }
            if (!orderDetailsAllModel.getDriverImage().equals("")) {
                imageUtils.loadImage(this, civDriverImage, progressBarDriver, orderDetailsAllModel.getDriverImage());
            }

        }
    }

    private void onSuccessReview() {
        ivThumbsDown.setEnabled(false);
        ivThumbsUp.setEnabled(false);
    }
}
