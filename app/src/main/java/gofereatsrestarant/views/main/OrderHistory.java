package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category OrderHistory
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.OrderHistoryAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.datamodels.main.OrderResultModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;

/*****************************************************************
 *  Completed and cancel order list
 ****************************************************************/
public class OrderHistory extends BaseActivity implements ServiceListener {

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
    public @InjectView(R.id.rvOrderHistory)
    CustomRecyclerView rvOrderHistory;
    public @InjectView(R.id.rltEmptylayout)
    RelativeLayout rltEmptylayout;
    private AlertDialog dialog;
    private OrderHistoryAdapter orderHistoryAdapter;
    //private OrderModel orderModel;
    private OrderResultModel orderResultModel;
    private ArrayList<OrderModel> orderModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        super.onCreateDrawer(savedInstanceState, "orderHistory");
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        setUpToolbar();
        initRecyclerView();
        getOrders(true);
    }

    /**
     * Setup navigation toolbar from BaseActivity
     **/
    private void setUpToolbar() {
        tvToolBarTitle.setVisibility(View.VISIBLE);
        tvToolBarTitle.setText(getResources().getString(R.string.order_history));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvOrderHistory.setLayoutManager(customLayoutManager);


        orderHistoryAdapter = new OrderHistoryAdapter(this);
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setHasFixedSize(true);
    }


    /**
     * Call API to get Order history
     */
    public void getOrders(boolean isLoad) {
        if (isLoad)
            commonMethods.showProgressDialog(this, customDialog);
        apiService.getOrderHistory(sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (jsonResp.isSuccess()) {
            onSuccessViewOrder(jsonResp);
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
     * To get order history
     *
     * @param jsonResp success response of fetching order history
     */

    public void onSuccessViewOrder(JsonResponse jsonResp) {
        orderResultModel = gson.fromJson(jsonResp.getStrResponse(), OrderResultModel.class);
        if (orderResultModel != null) {
            if (orderResultModel.getOrderHistory() != null && (orderResultModel.getOrderHistory().size() > 0)) {
                rltEmptylayout.setVisibility(View.GONE);
                rvOrderHistory.setVisibility(View.VISIBLE);
                orderModels.clear();
                orderModels.addAll(orderResultModel.getOrderHistory());
                orderHistoryAdapter = new OrderHistoryAdapter(this, orderModels);
                rvOrderHistory.setAdapter(orderHistoryAdapter);
            } else {
                rvOrderHistory.setVisibility(View.GONE);
                rltEmptylayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(mContext, MainActivity.class);
        home.putExtra("type", "order");
        startActivity(home);
        finish();
    }
}
