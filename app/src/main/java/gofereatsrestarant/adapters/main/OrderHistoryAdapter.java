package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderHistoryAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.main.OrderHistoryDetails;


/*****************************************************************
 Adapter for completed or cancel order history list
 ****************************************************************/
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    SessionManager sessionManager;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;

    /**
     * OrderHistoryAdapter Constructor  to intialize context of the activity it is used in
     *
     * @param context context of the activity it is used in
     */


    public OrderHistoryAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderHistoryAdapter Constructor  to intialize context of the activity it is used in and the ArrayList of the OrderModel Class
     *
     * @param context   context of the activity it is used in
     * @param orderList ArrayList of the OrderModel Class
     */

    public OrderHistoryAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_history_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);
        if (orderModel != null) {
            if (!TextUtils.isEmpty(orderModel.getUserName())) {
                holder.tvOrderItem.setText(orderModel.getOrderItemCount() + " " +context.getResources().getString(R.string.item_for)+" " + orderModel.getUserName());
            }
            if (orderModel.getOrderId() > 0) {
                if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                    holder.tvOrderId.setText(context.getResources().getString(R.string.order_id) +" #"+ orderModel.getOrderId());
                else
                    holder.tvOrderId.setText(context.getResources().getString(R.string.order_id) + orderModel.getOrderId()+"# ");
            }
            holder.tvOrderName.setText(orderModel.getOrderItem());
            holder.tvPrice.setText(sessionManager.getCurrencySymbol() + orderModel.getOrderPrice());
            holder.tvDate.setText(orderModel.getOrderTime());
            if (orderModel.getOrderStatus().equals("cancelled")) {
                holder.tvOrderStatus.setText(R.string.cancelled);
            } else if (orderModel.getOrderStatus().equals("completed")) {
                holder.tvOrderStatus.setText(R.string.completed);
            }
        }
        holder.rltOrderItem.setTag(holder);
        holder.rltOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();

                OrderModel orderModel = orderList.get(clickedPosition);
                notifyDataSetChanged();

                Intent login = new Intent(context, OrderHistoryDetails.class);
                login.putExtra("orderId", orderModel.getOrderId());
                login.putExtra("type,", 0);
                context.startActivity(login);
                ((Activity) context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltOrderItem)
        RelativeLayout rltOrderItem;
        public @InjectView(R.id.tvOrderItem)
        TextView tvOrderItem;
        public @InjectView(R.id.tvOrderName)
        TextView tvOrderName;
        public @InjectView(R.id.tvOrderId)
        TextView tvOrderId;
        public @InjectView(R.id.tvTime)
        TextView tvPrice;
        public @InjectView(R.id.tvDate)
        TextView tvDate;
        public @InjectView(R.id.tvOrderStatus)
        TextView tvOrderStatus;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}