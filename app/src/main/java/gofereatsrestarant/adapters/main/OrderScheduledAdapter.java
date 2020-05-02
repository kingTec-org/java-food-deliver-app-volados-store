package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderScheduledAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

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
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.main.OrderDetails;


/*****************************************************************
 Adapter for schedule order list
 ****************************************************************/
public class OrderScheduledAdapter extends RecyclerView.Adapter<OrderScheduledAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;

    /**
     * OrderScheduledAdapter Constructor to intialize context of the activity it is used in
     *
     * @param context context of the activity it is used in
     */

    public OrderScheduledAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderScheduleAdapter constructor to intialize the context of the activity it is used in and orderList is the array list object of the Order Model Class
     *
     * @param context   context of the activity it is used in
     * @param orderList orderList is the array list of the Order Model Class
     */

    public OrderScheduledAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_scheduled_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);
        if (orderModel != null) {
            if (!TextUtils.isEmpty(orderModel.getUserName())) {
                holder.tvOrderName.setText(orderModel.getOrderItemCount() + " "+ context.getResources().getString(R.string.item_for) + " " +orderModel.getUserName());
            }
            if (orderModel.getOrderId() > 0) {
                if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                    holder.tvOrderId.setText(context.getString(R.string.order_id)+" #" + orderModel.getOrderId());
                else
                    holder.tvOrderId.setText(context.getString(R.string.order_id) + orderModel.getOrderId()+"# ");
            }
            if (!TextUtils.isEmpty(orderModel.getOrderTime())) {
                //holder.ivClock.setVisibility(View.VISIBLE);
                holder.rltTime.setVisibility(View.VISIBLE);
                if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                    Drawable img = context.getResources().getDrawable(R.drawable.clock);
                    img.setBounds(0, 0, 60, 60);
                    holder.tvTime.setCompoundDrawables(img, null, null, null);
                    holder.tvTime.setText(orderModel.getOrderTime());
                    holder.tvTime.setCompoundDrawablePadding(10);
                    holder.tvTime.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }
                else
                {
                    Drawable img = context.getResources().getDrawable(R.drawable.clock);
                    img.setBounds(0, 0, 60, 60);
                    holder.tvTime.setCompoundDrawables(null, null, img, null);
                    holder.tvTime.setText(orderModel.getOrderTime());
                    holder.tvTime.setCompoundDrawablePadding(10);
                    holder.tvTime.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }
                holder.tvDate.setText(orderModel.getOrderDate());
            } else {
                //holder.ivClock.setVisibility(View.GONE);
                holder.rltTime.setVisibility(View.GONE);
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

                Intent orderDetails = new Intent(context, OrderDetails.class);
                orderDetails.putExtra("orderId", orderModel.getOrderId());
                orderDetails.putExtra("ordertype", 1);
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
        public @InjectView(R.id.tvOrderName)
        TextView tvOrderName;
        public @InjectView(R.id.tvOrderId)
        TextView tvOrderId;
        public @InjectView(R.id.tvTime)
        TextView tvTime;
        public @InjectView(R.id.tvDate)
        TextView tvDate;
       /* public @InjectView(R.id.ivClock)
        ImageView ivClock;*/
        public @InjectView(R.id.rltTime)
        RelativeLayout rltTime;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}