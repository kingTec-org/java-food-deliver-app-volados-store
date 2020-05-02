package gofereatsrestarant.adapters.main.menu;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category MenuMainAdapter
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
import gofereatsrestarant.datamodels.main.MenuModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.main.menu.SubMenuItemActivity;


/*****************************************************************
 Adapter for Main Menu
 ****************************************************************/
public class MenuMainAdapter extends RecyclerView.Adapter<MenuMainAdapter.RecyclerViewHolder> {
    public @Inject
    ImageUtils imageUtils;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MenuModel> menuList;

    public MenuMainAdapter(Context context) {
        this.context = context;
        menuList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    public MenuMainAdapter(Context context, ArrayList<MenuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
        inflater = LayoutInflater.from(context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_menu_main_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        MenuModel menuModel = menuList.get(position);
        if (menuModel != null) {
            if (!TextUtils.isEmpty(menuModel.getMenuName())) {
                holder.tvMainMenu.setText(menuModel.getMenuName());
            }
        }
        holder.rltMenuItem.setTag(holder);
        holder.rltMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RecyclerViewHolder holder = (RecyclerViewHolder) v.getTag();
                //int clickedPosition = holder.getAdapterPosition();

                //MenuModel menuModel = menuList.get(clickedPosition);
                notifyDataSetChanged();

                Intent subMenu = new Intent(context, SubMenuItemActivity.class);
                context.startActivity(subMenu);
                ((Activity) context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    /**
     * View Holder Class For Menu
     */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public @InjectView(R.id.rltMenuItem)
        RelativeLayout rltMenuItem;
        public @InjectView(R.id.tvMainMenu)
        TextView tvMainMenu;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}