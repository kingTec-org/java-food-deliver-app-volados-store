package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderRecentAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 Adapter for recent order list
 ****************************************************************/
public class OrderRecentAdapter extends RecyclerView.Adapter<OrderRecentAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;
    private onItemClickListener mItemClickListener;

    /**
     * OrderRecentAdapter Constructor to intialize the context of the activity it is used in
     *
     * @param context context of the activity it is used in
     */

    public OrderRecentAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderRecentAdapter Constructor to intialize the context of the activity it is used in and the ArrayList of OrderModel Object class
     *
     * @param context   context of the activity it is used in
     * @param orderList ArrayList of OrderModel Object class
     */

    public OrderRecentAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_recent_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final OrderModel orderModel = orderList.get(position);
        if (orderModel != null && orderModel.getOrderId() > 0) {
            if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                holder.tvOrderId.setText(context.getString(R.string.order_id) + " #"+orderModel.getOrderId());
            else
                holder.tvOrderId.setText(context.getString(R.string.order_id) + orderModel.getOrderId()+ "# ");
        }
        holder.rltOrderItem.setTag(holder);
        holder.rltOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();

                OrderModel orderModel = orderList.get(clickedPosition);
                //notifyDataSetChanged();

                Intent orderDetails = new Intent(context, OrderDetails.class);
                orderDetails.putExtra("orderId", orderModel.getOrderId());
                context.startActivity(orderDetails);
                ((Activity) context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
        if (!TextUtils.isEmpty(orderModel.getRestaurantDriverThumbs())) {
            if (orderModel.getRestaurantDriverThumbs().equals("0")) {
                holder.ivThumbsUp.setEnabled(false);
                holder.ivThumbsDown.setEnabled(false);
                holder.ivThumbsDown.setColorFilter(ContextCompat.getColor(context, R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.ivThumbsUp.setColorFilter(ContextCompat.getColor(context, R.color.appgreen), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.ivThumbsUp.setEnabled(false);
                holder.ivThumbsDown.setEnabled(false);
            }
        }
        holder.ivThumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivThumbsUp.setColorFilter(ContextCompat.getColor(context, R.color.appgreen), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.ivThumbsDown.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListeners("1", orderModel.getOrderId(), holder);
                }
                holder.ivThumbsUp.setEnabled(false);
                holder.ivThumbsDown.setEnabled(false);
            }
        });
        holder.ivThumbsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.ivThumbsUp.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                //holder.ivThumbsDown.setColorFilter(ContextCompat.getColor(context, R.color.app_red), android.graphics.PorterDuff.Mode.MULTIPLY);
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListeners("0", orderModel.getOrderId(), holder);
                }
                //holder.ivThumbsDown.setEnabled(false);
                //holder.ivThumbsUp.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public interface onItemClickListener {
        void onItemClickListeners(String type, int orderId, RecyclerViewHolder holder);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltOrderItem)
        RelativeLayout rltOrderItem;
        public @InjectView(R.id.tvOrderId)
        TextView tvOrderId;
        public @InjectView(R.id.ivThumbsDown)
        ImageView ivThumbsDown;
        public @InjectView(R.id.ivThumbsUp)
        ImageView ivThumbsUp;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}