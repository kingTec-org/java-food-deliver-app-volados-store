package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;


/*****************************************************************
 Adapter for New order list
 ****************************************************************/
public class OrderPendingAdapter extends RecyclerView.Adapter<OrderPendingAdapter.RecyclerViewHolder> implements ServiceListener {

    public Map<Integer, CountDownTimer> timerMap = new HashMap<Integer, CountDownTimer>();
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
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;
    private int type = 0;


    /**
     * OrderPendingAdapter Constructor to intialize context of the activity it is used in
     *
     * @param context context of the activity it is used in
     */

    public OrderPendingAdapter(Context context) {
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }


    /**
     * OrderPendingAdapter Constructor to intialize context of the activity it is used in and ArrayList of the OrderModel class
     *
     * @param context   intialize context of the activity it is used in
     * @param orderList ArrayList of the OrderModel class
     */


    public OrderPendingAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    public OrderPendingAdapter(Context context, ArrayList<OrderModel> orderList, int type) {
        this.context = context;
        this.orderList = orderList;
        this.type = type;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_pending_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        OrderModel orderModel = orderList.get(position);
        if (orderModel != null) {

            CountDownTimer countDownTimer = timerMap.get(position);
            if (countDownTimer == null) {
                countDownTimer = new CountDownTimer(orderModel.getRemainingSeconds() * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //this will be done every 1000 milliseconds ( 1 seconds )
                        int progress = (int) ((millisUntilFinished) / 1000);
                        holder.tvTimer.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onFinish() {
                        //the progressBar will be invisible after 60000 milliseconds ( 1 minute)
                        holder.tvTimer.setText("0");
                        if (orderList.size() > 0) {
                            refund(0);
                            timerMap.get(position).cancel();
                        }
                    }
                };
                countDownTimer.start();
                timerMap.put(position, countDownTimer);
            }

        }
        if (orderList.get(position).getOrderType() == 1) {
            holder.btnOrder.setText(context.getResources().getString(R.string.scheduled_order));
            holder.btnOrder.setBackground(context.getResources().getDrawable(R.drawable.background_orange));
        } else {
            holder.btnOrder.setText(context.getResources().getString(R.string.new_order));
            holder.btnOrder.setBackground(context.getResources().getDrawable(R.drawable.background_green));
        }
        holder.btnOrder.setTag(holder);


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    /**
     * To call Api Service when restaurant did not accept the request made by user
     *
     * @param position postion will be always 0 since it is show in 0th position always
     */


    public void refund(int position) {
        apiService.refund(orderList.get(position).getOrderId(), sessionManager.getToken()).enqueue(new RequestCallback(this));
        notifyItemRemoved(position);
        orderList.remove(position);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        /*if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }*/
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public @InjectView(R.id.btnOrder)
        Button btnOrder;
        public @InjectView(R.id.tvTimer)
        CustomTextView tvTimer;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

    }

}