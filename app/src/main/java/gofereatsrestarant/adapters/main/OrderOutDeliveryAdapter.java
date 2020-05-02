package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderOutDeliveryAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
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
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.main.OrderDetails;


/*****************************************************************
 Adapter for Out for delivery list
 ****************************************************************/
public class OrderOutDeliveryAdapter extends RecyclerView.Adapter<OrderOutDeliveryAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;

    /**
     * OrderOutDeliveryAdapter Constructor to intialize context of the the activity it is used in
     *
     * @param context context of the the activity it is used in
     */

    public OrderOutDeliveryAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderOutDeliveryAdapter Constructor to intialize context of the the activity it is used in and array list OrderModel class
     *
     * @param context   context of the the activity it is used in
     * @param orderList array list OrderModel class
     */


    public OrderOutDeliveryAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_out_delivery_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);
        if (orderModel != null && orderModel.getOrderId() > 0) {
            if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                holder.tvOrderId.setText(context.getString(R.string.order_id) +" #"+ orderModel.getOrderId());
            else
                holder.tvOrderId.setText(context.getString(R.string.order_id) + orderModel.getOrderId()+"# ");
        }
        holder.rltOrderItem.setTag(holder);
        holder.rltOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();

                OrderModel orderModel = orderList.get(clickedPosition);
                notifyDataSetChanged();

                Intent orderDetails = new Intent(context, OrderDetails.class);
                orderDetails.putExtra("orderId", orderModel.getOrderId());
                context.startActivity(orderDetails);
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
        public @InjectView(R.id.tvOrderId)
        TextView tvOrderId;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}