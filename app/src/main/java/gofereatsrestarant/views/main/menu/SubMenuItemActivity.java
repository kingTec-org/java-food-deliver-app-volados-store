package gofereatsrestarant.views.main.menu;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category SubMenuItemActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.menu.MenuSubAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.datamodels.menu.MenuItemModel;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;

/*****************************************************************
 Show selected main menu sub items (list)
 ****************************************************************/
public class SubMenuItemActivity extends AppCompatActivity {
    public @InjectView(R.id.rvSubMenu)
    CustomRecyclerView rvSubMenu;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    private MenuSubAdapter menuSubAdapter;
    private ArrayList<MenuItemModel> menuItemModels = new ArrayList<>();
    private String title;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu_item);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        commonMethods.rotateArrow(ivBack, this);
        getintent();
        tvTitle.setText(title);
        vBottom.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    /**
     * Setup recycler view and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvSubMenu.setLayoutManager(customLayoutManager);


        menuSubAdapter = new MenuSubAdapter(this, menuItemModels);
        rvSubMenu.setAdapter(menuSubAdapter);
        rvSubMenu.setHasFixedSize(true);
    }

    /**
     * Get Intent values from previous Activity
     **/
    public void getintent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        title = intent.getStringExtra("title");
        menuItemModels.addAll((ArrayList<MenuItemModel>) bundle.getSerializable("menu_item"));
    }

}