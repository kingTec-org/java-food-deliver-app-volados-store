package gofereatsrestarant.adapters.main.menu;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category MenuSubAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.menu.MenuItemModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.main.menu.MenuItemActivity;


/*****************************************************************
 Adapter for sub Menu
 ****************************************************************/
public class MenuSubAdapter extends RecyclerView.Adapter<MenuSubAdapter.RecyclerViewHolder> implements ServiceListener {
    public static ArrayList<MenuItemModel> menuList;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    private Context context;
    private LayoutInflater inflater;
    private int selectedItem = -1;
    private BottomSheetDialog dialog;

    public MenuSubAdapter(Context context) {
        this.context = context;
        menuList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    public MenuSubAdapter(Context context, ArrayList<MenuItemModel> menuList) {
        this.context = context;
        MenuSubAdapter.menuList = menuList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        /*if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }*/
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_menu_sub_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MenuItemModel menuModel = menuList.get(position);
        if (menuModel != null) {
            if (!TextUtils.isEmpty(menuModel.getMenuName())) {
                holder.tvMenuName.setText(menuModel.getMenuName());
            }
            if (!TextUtils.isEmpty(menuModel.getMenuPrice())) {
                holder.tvMenuPrice.setText(sessionManager.getCurrencySymbol()+ menuModel.getMenuPrice());
            }
            if (!TextUtils.isEmpty(menuModel.getMenuImage().getSmallImage())) {
                holder.ivMenuImage.setVisibility(View.VISIBLE);
                imageUtils.loadImage(context, holder.ivMenuImage, holder.progressBar, menuModel.getMenuImage().getSmallImage());
            } else {
                holder.ivMenuImage.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
            }
            if (menuModel.getMenuVisible() == 1) {
                holder.ivMenuVisible.setImageResource(R.drawable.eye);
            } else {
                holder.ivMenuVisible.setImageResource(R.drawable.eyex);
            }
        }
        holder.rltMenuItem.setTag(holder);
        holder.rltMenuVisible.setTag(holder);
        holder.ivMenuVisible.setTag(holder);
        holder.rltMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();

                MenuItemModel menuModel = menuList.get(clickedPosition);
                selectedItem = clickedPosition;
                notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putSerializable("menu_item", menuModel);
                Intent subMenu = new Intent(context, MenuItemActivity.class);
                subMenu.putExtras(bundle);
                subMenu.putExtra("position", clickedPosition);
                context.startActivity(subMenu);
                ((Activity) context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });

        holder.rltMenuVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                int clickedPosition = holder.getAdapterPosition();
                //MenuItemModel menuModel = menuList.get(clickedPosition);
                selectedItem = clickedPosition;
                notifyDataSetChanged();
                bottomSheet();

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    /**
     * Update the SubMenu Items in the ArrayList
     *
     * @param menuList arrayList for the SubMenu
     */
    public void updateList(ArrayList<MenuItemModel> menuList) {
        MenuSubAdapter.menuList = menuList;
        notifyDataSetChanged();
    }

    /**
     * Bottom sheet Declaration For SubMenu Item
     */
    public void bottomSheet() {
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.menu_visible);
        BottomSheetInject bottomSheetInject = new BottomSheetInject();
        ButterKnife.inject(bottomSheetInject, dialog);
        bottomSheetInject.setvisibe();
        dialog.show();
    }

    /**
     * Call API to toggle visiblity of menu items
     **/
    private void toggle(int id) {
        commonMethods.showProgressDialog(((AppCompatActivity) context), customDialog);
        apiService.togglevisible(sessionManager.getToken(), "menu_item", id).enqueue(new RequestCallback(this));
    }

    /**
     * View Holder Class For SubMenu Items
     */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltMenuItem)
        RelativeLayout rltMenuItem;
        public @InjectView(R.id.ivMenuImage)
        ImageView ivMenuImage;
        public @InjectView(R.id.tvMenuName)
        TextView tvMenuName;
        public @InjectView(R.id.tvMenuPrice)
        TextView tvMenuPrice;
        public @InjectView(R.id.ivMenuVisible)
        ImageView ivMenuVisible;
        public @InjectView(R.id.progressBar)
        ProgressBar progressBar;
        public @InjectView(R.id.rltMenuVisible)
        RelativeLayout rltMenuVisible;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * Bottomsheet Class
     */
    class BottomSheetInject {
        public @InjectView(R.id.rltAvailable)
        RelativeLayout rltAvailable;
        public @InjectView(R.id.rltUnavailableToday)
        RelativeLayout rltUnavailableToday;
        public @InjectView(R.id.rltUnavailableIndefinite)
        RelativeLayout rltUnavailableIndefinite;

        public @InjectView(R.id.ivAvailableTick)
        ImageView ivAvailableTick;
        public @InjectView(R.id.ivUnavailableTodayTick)
        ImageView ivUnavailableTodayTick;
        public @InjectView(R.id.ivUnavailableIndefiniteTick)
        ImageView ivUnavailableIndefiniteTick;

        /**
         * Set SubMenu As available
         */
        @OnClick(R.id.rltAvailable)
        public void available() {
            dialog.dismiss();
            updateList(menuList);
            menuList.get(selectedItem).setMenuVisible(1);
            toggle(menuList.get(selectedItem).getMenuId());
            ivAvailableTick.setVisibility(View.VISIBLE);
            ivUnavailableTodayTick.setVisibility(View.GONE);
            ivUnavailableIndefiniteTick.setVisibility(View.GONE);
        }

        /**
         * Set SubMenu As unavailable for tdy
         */
        @OnClick(R.id.rltUnavailableToday)
        public void unavailable() {
            dialog.dismiss();
            menuList.get(selectedItem).setMenuVisible(0);
            updateList(menuList);
            ivAvailableTick.setVisibility(View.GONE);
            ivUnavailableTodayTick.setVisibility(View.VISIBLE);
            toggle(menuList.get(selectedItem).getMenuId());
        }

        /**
         * Set SubMenu As unavailable
         */
        @OnClick(R.id.rltUnavailableIndefinite)
        public void unavailableIndefinite() {
            dialog.dismiss();
            menuList.get(selectedItem).setMenuVisible(0);
            updateList(menuList);
            toggle(menuList.get(selectedItem).getMenuId());
            ivAvailableTick.setVisibility(View.GONE);
            ivUnavailableTodayTick.setVisibility(View.GONE);
        }

        /**
         * Set available subMenus Form the API
         */
        public void setvisibe() {
            if (menuList.get(selectedItem).getMenuVisible() == 1) {
                ivAvailableTick.setVisibility(View.VISIBLE);
                ivUnavailableTodayTick.setVisibility(View.GONE);
            } else {
                ivAvailableTick.setVisibility(View.GONE);
                ivUnavailableTodayTick.setVisibility(View.VISIBLE);
            }


        }
    }
}