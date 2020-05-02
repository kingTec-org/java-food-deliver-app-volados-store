package gofereatsrestarant.views.main.menu;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category MenuActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.menu.MenuExpandableAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.datamodels.main.MenuModel;
import gofereatsrestarant.datamodels.menu.MenuResultModel;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.ExpandableRecyclerView;
import gofereatsrestarant.views.main.BaseActivity;
import gofereatsrestarant.views.main.MainActivity;


/*****************************************************************
 Main Menu contain main menu list details
 ****************************************************************/
public class MenuActivity extends BaseActivity implements ServiceListener {

    //public ArrayList<MenuModifierModel> menuModifiermodel = new ArrayList<>();
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.toolbar)
    Toolbar toolbar;
    public @InjectView(R.id.drawer)
    DrawerLayout drawerLayout;
    public @InjectView(R.id.navigation_view)
    NavigationView navigationView;
    public @InjectView(R.id.tvToolBarTitle)
    TextView tvToolBarTitle;
    public @InjectView(R.id.rvMainMenu)
    ExpandableRecyclerView rvMainMenu;
    public @InjectView(R.id.rltEmptylayout)
    RelativeLayout rltEmptylayout;
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
    //private MenuMainAdapter menuMainAdapter;
    private MenuExpandableAdapter menuExpandableAdapter;
    private ArrayList<MenuModel> menuModels = new ArrayList<>();
    private AlertDialog dialog;
    private MenuResultModel menuResultModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        super.onCreateDrawer(savedInstanceState, "menu");
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);

        setUpToolbar();
        initRecyclerView();
    }

    /**
     * Setup navigation toolbar from BaseActivity
     **/
    private void setUpToolbar() {
        tvToolBarTitle.setVisibility(View.VISIBLE);
        tvToolBarTitle.setText(getResources().getString(R.string.menu));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMenuDetails(true);
    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvMainMenu.setLayoutManager(customLayoutManager);


        menuExpandableAdapter = new MenuExpandableAdapter(this);
        rvMainMenu.setAdapter(menuExpandableAdapter);
        rvMainMenu.setHasFixedSize(true);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            onSuccessGetRestaurantList(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Call API to get Menu list
     */
    private void getMenuDetails(boolean isload) {
        if (isload)
            commonMethods.showProgressDialog(this, customDialog);
        apiService.getstoreMenu(sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    private void onSuccessGetRestaurantList(JsonResponse jsonResponse) {
        menuResultModel = gson.fromJson(jsonResponse.getStrResponse(), MenuResultModel.class);
        if (menuResultModel != null) {
            updateView();
        }
    }

    /**
     * Parse restaurant menu to model object
     **/
    private void updateView() {
        ArrayList<MenuModel> restaurantMenuModel = menuResultModel.getRestaurantMenu();
        if (restaurantMenuModel != null && restaurantMenuModel.size() > 0) {
            for (int k = 0; k < restaurantMenuModel.size(); k++) {
                if (restaurantMenuModel.get(0).getMenuCategory() != null && (restaurantMenuModel.get(0).getMenuCategory().size() > 0)) {
                    rltEmptylayout.setVisibility(View.GONE);
                    rvMainMenu.setVisibility(View.VISIBLE);
                    menuModels.clear();
                    for (int i = 0; i < restaurantMenuModel.size(); i++) {
                        MenuModel menuModel = new MenuModel();
                        menuModel.setMenuName(restaurantMenuModel.get(i).getMenuName());
                        menuModel.setMenuId(restaurantMenuModel.get(i).getMenuId());
                        menuModel.setMenuCategory(restaurantMenuModel.get(i).getMenuCategory());
                        menuModels.add(menuModel);
                    }
                    menuExpandableAdapter = new MenuExpandableAdapter(this, menuModels);
                    menuExpandableAdapter.expand(sessionManager.getClickedMenu());
                    rvMainMenu.setAdapter(menuExpandableAdapter);
                } else {
                    rvMainMenu.setVisibility(View.GONE);
                    rltEmptylayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            rvMainMenu.setVisibility(View.GONE);
            rltEmptylayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(mContext, MainActivity.class);
        home.putExtra("type", "order");
        startActivity(home);
        finish();
    }
}
