package gofereatsrestarant.adapters.main.menu;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category MenuAddOnAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import gofereatsrestarant.datamodels.main.FoodListModel;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;

import static gofereatsrestarant.views.main.menu.MenuItemActivity.menuPostion;


/*****************************************************************
 Adapter for menu details add-ons price list
 ****************************************************************/
public class MenuAddOnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
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
    private int lastpos = -1;
    private Context context;
    private ArrayList<FoodListModel> menuList;
    private int selectedItem = -1;
    private BottomSheetDialog dialog;

    public MenuAddOnAdapter(Context context) {
        this.context = context;
        menuList = new ArrayList<>();
        AppController.getAppComponent().inject(this);
    }

    public MenuAddOnAdapter(Context context, ArrayList<FoodListModel> menuList) {
        this.context = context;
        this.menuList = menuList;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            ViewGroup vGroup = (ViewGroup) mInflater.inflate(R.layout.modifier_header, parent, false);
            HeaderHolder vhGroup = new HeaderHolder(vGroup);
            return vhGroup;
        } else {
            ViewGroup vGroup = (ViewGroup) mInflater.inflate(R.layout.row_menu_add_on_list, parent, false);
            ItemViewHolder vhGroup = new ItemViewHolder(vGroup);
            return vhGroup;

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //
        if (holder.getItemViewType() == TYPE_HEADER) {
            FoodListModel menuModel = menuList.get(position);
            HeaderHolder headerHolder = (HeaderHolder) holder;
            if (!TextUtils.isEmpty(menuModel.getFoodName())) {
                headerHolder.tvModifier.setText(menuModel.getFoodName());
            }

        } else {
            FoodListModel menuModel = menuList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            if (menuModel != null) {
                if (!TextUtils.isEmpty(menuModel.getFoodName())) {
                    viewHolder.tvMenuName.setText(menuModel.getFoodName());
                }
                if (!TextUtils.isEmpty(menuModel.getFoodPrice())) {
                    viewHolder.tvMenuPrice.setText(sessionManager.getCurrencySymbol()+ menuModel.getFoodPrice());
                }
                if (menuModel.getMenuVisible() == 1) {
                    viewHolder.ivMenuVisible.setImageResource(R.drawable.eye);
                } else {
                    viewHolder.ivMenuVisible.setImageResource(R.drawable.eyex);
                }
            }
            viewHolder.rltMenuItem.setTag(holder);
            viewHolder.rltMenuVisible.setTag(holder);
            viewHolder.ivMenuVisible.setTag(holder);
            viewHolder.rltMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemViewHolder holder = (ItemViewHolder) v.getTag();
                    int clickedPosition = holder.getAdapterPosition();

                    //FoodListModel menuModel = menuList.get(clickedPosition);
                    selectedItem = clickedPosition;
                    notifyDataSetChanged();

                /*Intent subMenu=new Intent(context,MenuItemActivity.class);
                context.startActivity(subMenu);
                ((Activity)context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);*/
                }
            });

            viewHolder.rltMenuVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemViewHolder holder = (ItemViewHolder) v.getTag();
                    int clickedPosition = holder.getAdapterPosition();

                    //FoodListModel menuModel = menuList.get(clickedPosition);
                    selectedItem = clickedPosition;
                    notifyDataSetChanged();

                    bottomSheet();

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*if(modelItems.get(position).getWishlistImage().equals("load")){
            return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}*/
        if (menuList.get(position).getType().equals("Menu")) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    /**
     * Updates The Menus List
     *
     * @param menuList FoodList
     */
    public void updateList(ArrayList<FoodListModel> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    /**
     * BottomSheet For Menus Available and Unavailable
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
     * Call API For Available and unAvailable
     *
     * @param id Mennu Id
     */
    private void toggle(int id) {
        commonMethods.showProgressDialog((AppCompatActivity) context, customDialog);
        apiService.togglevisible(sessionManager.getToken(), "modifier_item", id).enqueue(new RequestCallback(this));
    }

    /**
     * ViewHolder For Menu items in Adapater Class
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltMenuItem)
        RelativeLayout rltMenuItem;
        public @InjectView(R.id.tvMenuName)
        TextView tvMenuName;
        public @InjectView(R.id.tvMenuPrice)
        TextView tvMenuPrice;
        public @InjectView(R.id.ivMenuVisible)
        ImageView ivMenuVisible;
        public @InjectView(R.id.rltMenuVisible)
        RelativeLayout rltMenuVisible;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * View Holder For Modifier Title in adapter
     */
    public class HeaderHolder extends RecyclerView.ViewHolder {

        public @InjectView(R.id.tvModifier)
        TextView tvModifier;

        public HeaderHolder(View itemView) {

            super(itemView);
            ButterKnife.inject(this, itemView);

        }

    }

    /**
     * BottomSheet Class
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
         * Set Menu Add On Available
         */
        @OnClick(R.id.rltAvailable)
        public void available() {
            dialog.dismiss();
            menuList.get(selectedItem).setMenuVisible(1);
            MenuSubAdapter.menuList.get(menuPostion).getModifier().get(lastpos - 1).getModifierItem().get(selectedItem - (lastpos + 1)).setMenuVisible(1);
            updateList(menuList);
            toggle(menuList.get(selectedItem).getMenuId());
            ivAvailableTick.setVisibility(View.VISIBLE);
            ivUnavailableTodayTick.setVisibility(View.GONE);
            //   ivUnavailableIndefiniteTick.setVisibility(View.GONE);
        }

        /**
         * Set AddOn Unavailable on Today
         */
        @OnClick(R.id.rltUnavailableToday)
        public void unavailable() {
            dialog.dismiss();
            menuList.get(selectedItem).setMenuVisible(0);
            updateList(menuList);
            toggle(menuList.get(selectedItem).getMenuId());
            ivAvailableTick.setVisibility(View.GONE);
            ivUnavailableTodayTick.setVisibility(View.VISIBLE);
            // ivUnavailableIndefiniteTick.setVisibility(View.GONE);
        }

        /**
         * Set unavailable For a Menu add on
         */
        @OnClick(R.id.rltUnavailableIndefinite)
        public void unavailableIndefinite() {
            dialog.dismiss();
            menuList.get(selectedItem).setMenuVisible(0);
            updateList(menuList);
            toggle(menuList.get(selectedItem).getMenuId());
            ivAvailableTick.setVisibility(View.GONE);
            ivUnavailableTodayTick.setVisibility(View.GONE);
            // ivUnavailableIndefiniteTick.setVisibility(View.VISIBLE);
        }

        /**
         * Set Availabilty of a Menu
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