package gofereatsrestarant.adapters.main;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category OrderDetailsAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.orderdetails.OrderDetailsModel;
import gofereatsrestarant.utils.ImageUtils;


/*****************************************************************
 Adapter for update order list and details
 ****************************************************************/
public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    SessionManager sessionManager;
    private LayoutInflater inflater;
    private ArrayList<OrderDetailsModel> orderDetailsList;

    /**
     * OrderDetailsAdapter Constructor  to intialize context of the activity it is used in
     *
     * @param context context of the activity it is used in
     */

    public OrderDetailsAdapter(Context context) {

        orderDetailsList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    /**
     * OrderDetailsAdapter Constructor to intialize context and arraylist of OrderdetailsModel class
     *
     * @param context          of the activity it is used in
     * @param orderDetailsList arraylist of OrderdetailsModel class
     */

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetailsModel> orderDetailsList) {

        this.orderDetailsList = orderDetailsList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_order_details_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        OrderDetailsModel orderDetailsModel = orderDetailsList.get(position);
        if (orderDetailsModel != null) {
            if (!TextUtils.isEmpty(orderDetailsModel.getOrderItem())) {
                holder.tvOrderCount.setText(String.valueOf(orderDetailsModel.getOrderQuantity())/*+"X "*/);
                holder.tvOrderItem.setText(orderDetailsModel.getOrderItem());
            }
            if (!TextUtils.isEmpty(orderDetailsModel.getOrderItemPrice())) {
                holder.tvOrderPrice.setText(sessionManager.getCurrencySymbol() + orderDetailsModel.getOrderItemPrice());
            }
            if (!TextUtils.isEmpty(orderDetailsModel.getUserNeed())) {
                holder.tvExtra.setText(orderDetailsModel.getUserNeed());
                holder.tvExtra.setVisibility(View.VISIBLE);
            } else {
                holder.tvExtra.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(orderDetailsModel.getAddOns())) {
                String addons = "-" + orderDetailsModel.getAddOns().replaceAll(",", "\n-");
                holder.tvaddons.setText(addons);
                holder.tvaddons.setVisibility(View.VISIBLE);
                holder.tvAddonsTitle.setVisibility(View.VISIBLE);
            } else {
                holder.tvaddons.setVisibility(View.GONE);
                holder.tvAddonsTitle.setVisibility(View.GONE);
            }
        }
        holder.rltOrderItem.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltOrderItem)
        LinearLayout rltOrderItem;
        public @InjectView(R.id.tvOrderItem)
        TextView tvOrderItem;
        public @InjectView(R.id.tvOrderPrice)
        TextView tvOrderPrice;
        public @InjectView(R.id.tvaddons)
        TextView tvaddons;
        public @InjectView(R.id.tvExtra)
        TextView tvExtra;
        public @InjectView(R.id.tvAddonsTitle)
        TextView tvAddonsTitle;
        public @InjectView(R.id.tvOrderCount)
        CustomTextView tvOrderCount;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}