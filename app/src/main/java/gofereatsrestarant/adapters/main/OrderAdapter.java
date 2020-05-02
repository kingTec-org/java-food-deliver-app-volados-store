package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.datamodels.main.OrderModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.main.OrderDetails;


/*****************************************************************
 Adapter for New order list
 ****************************************************************/
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderModel> orderList;

    /**
     * Order Adapter constructor to intialize context
     *
     * @param context of the activity it is used in
     */
    public OrderAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderAdapter constructor to intialize context and order list ArrayList of Order Model class
     *
     * @param context   context of the activity it is used in
     * @param orderList order list ArrayList of Order Model class
     */


    public OrderAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);
        if (orderModel != null) {
            if (!TextUtils.isEmpty(orderModel.getUserName())) {
                holder.tvOrderName.setText(orderModel.getOrderItemCount() + " "+context.getResources().getString(R.string.item_for) +" "+ orderModel.getUserName());
            }
            if (orderModel.getOrderId() > 0) {
                if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                    holder.tvOrderId.setText(context.getResources().getString(R.string.order_id) +" #"+ orderModel.getOrderId());
                else
                    holder.tvOrderId.setText(context.getResources().getString(R.string.order_id) + orderModel.getOrderId()+"# ");
            }
            if (!TextUtils.isEmpty(orderModel.getDriverImage())) {
                holder.civUserImage.setVisibility(View.VISIBLE);
                imageUtils.loadCircleImage(context, holder.civUserImage, orderModel.getDriverImage());
            } else {
                holder.civUserImage.setVisibility(View.GONE);
            }

            if (orderModel.getRemainingSeconds() > 0) {

                int progress = orderModel.getRemainingSeconds();
                if (progress > 0) {

                    ProgressBar downloadProgress = holder.progressBar;
                    downloadProgress.setMax((orderModel.getTotalSeconds()) / 60);
                    if (downloadProgress.isIndeterminate()) {
                        downloadProgress.setIndeterminate(false);
                    }

                    setProgressLoader(holder.tvTime, holder.progressBar, orderModel.getTotalSeconds(), orderModel.getRemainingSeconds());
                }
            } else {
                holder.progressBar.setProgress(100);
                holder.tvTime.setText(0 + "\n"+ context.getResources().getString(R.string.minutes) );
            }
        }
        holder.rltOrderItem.setTag(holder);
        holder.rltOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();

                OrderModel orderModel = orderList.get(clickedPosition);

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


    public void setProgressLoader(final TextView tvTime, final ProgressBar progressBar, final long totalTime, final int remainingTime) {
        new CountDownTimer(remainingTime * 1000, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be done every 1000 milliseconds ( 1 seconds )
                int progress = (int) (((totalTime * 1000) - millisUntilFinished) / 1000);
                progressBar.setProgress(progress / 60);

                tvTime.setText((((totalTime / 60) - (progress / 60))) /*+ 1*/ + "\n"+context.getResources().getString(R.string.minutes));
            }

            @Override
            public void onFinish() {
                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
                //progressBar.dismiss();
                progressBar.setProgress(100);
                tvTime.setText(0 + "\n"+context.getResources().getString(R.string.minutes));
            }

        }.start();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltOrderItem)
        RelativeLayout rltOrderItem;
        public @InjectView(R.id.tvOrderName)
        TextView tvOrderName;
        public @InjectView(R.id.civUserImage)
        CircleImageView civUserImage;
        public @InjectView(R.id.tvOrderId)
        TextView tvOrderId;
        public @InjectView(R.id.progressBar)
        ProgressBar progressBar;
        public @InjectView(R.id.tvTime)
        TextView tvTime;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}