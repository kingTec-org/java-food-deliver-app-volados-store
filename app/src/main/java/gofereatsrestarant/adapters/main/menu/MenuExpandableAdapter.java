package gofereatsrestarant.adapters.main.menu;
/**
 * @package com.trioangle.GoferEats
 * @subpackage adapters.main
 * @category MenuExpandableAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.MenuModel;
import gofereatsrestarant.datamodels.menu.MenuCategoryModel;
import gofereatsrestarant.datamodels.menu.MenuItemModel;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.customize.ExpandableRecyclerView;
import gofereatsrestarant.views.main.menu.SubMenuItemActivity;

/*****************************************************************
 Adapter for Main Menu
 ****************************************************************/
public class MenuExpandableAdapter extends ExpandableRecyclerView.Adapter<MenuExpandableAdapter.ChildViewHolder, ExpandableRecyclerView.SimpleGroupViewHolder, String, String> {

    public @Inject
    ImageUtils imageUtils;
    public @Inject
    SessionManager sessionManager;
    private ArrayList<MenuCategoryModel> restaurantMenuCategoriesModel;
    private Context context;
    private ArrayList<MenuModel> menuList;

    public MenuExpandableAdapter() {
    }

    public MenuExpandableAdapter(Context context) {
        this.context = context;
        menuList = new ArrayList<>();
        AppController.getAppComponent().inject(this);
    }

    public MenuExpandableAdapter(Context context, ArrayList<MenuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
        AppController.getAppComponent().inject(this);
    }

    @Override
    public int getGroupItemCount() {
        return menuList.size() - 1;
    }

    @Override
    public int getChildItemCount(int group) {
        if(menuList.size()<=group)
            group=group-1;
        restaurantMenuCategoriesModel = menuList.get(group).getMenuCategory();
        return restaurantMenuCategoriesModel.size();
    }

    @Override
    public String getGroupItem(int position) {
        return menuList.get(position).getMenuName();
    }

    @Override
    public String getChildItem(int group, int position) {
        return menuList.get(group).getMenuCategory().get(position).getMenuName();
    }

    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }

    @Override
    protected ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_drawer, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public int getChildItemViewType(int group, int position) {
        return 1;
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, final int group) {
        super.onBindGroupViewHolder(holder, group);
        holder.setText(getGroupItem(group));
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, final int group, final int position) {
        super.onBindChildViewHolder(holder, group, position);


        holder.tv.setText(getChildItem(group, position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MenuItemModel> menuItemModels = menuList.get(group).getMenuCategory().get(position).getMenuItem();
                Bundle bundle = new Bundle();
                bundle.putSerializable("menu_item", menuItemModels);

                Intent subMenu = new Intent(context, SubMenuItemActivity.class);
                subMenu.putExtra("title", getChildItem(group, position));
                subMenu.putExtras(bundle);
                context.startActivity(subMenu);
                ((Activity) context).overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
    }

    /**
     * View Holder Class For SubMenuItem
     */
    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.subMenuItem);
        }
    }


}
