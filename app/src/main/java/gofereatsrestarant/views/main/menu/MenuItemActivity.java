package gofereatsrestarant.views.main.menu;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category MenuItemActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.adapters.main.menu.MenuAddOnAdapter;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.datamodels.main.FoodListModel;
import gofereatsrestarant.datamodels.menu.MenuItemModel;
import gofereatsrestarant.datamodels.menu.MenuModifier;
import gofereatsrestarant.datamodels.menu.MenuModifierModel;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.views.customize.CustomLayoutManager;
import gofereatsrestarant.views.customize.CustomRecyclerView;

/*****************************************************************
 Contain selected menu item price and add-ons also can change visibility
 ****************************************************************/
public class MenuItemActivity extends AppCompatActivity {

    public static int menuPostion;
    public ArrayList<MenuModifierModel> menuModifiermodel = new ArrayList<>();
    public ArrayList<FoodListModel> foodListModels = new ArrayList<>();
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @Inject
    ImageUtils imageUtils;
    public @InjectView(R.id.rvAddOnMenu)
    CustomRecyclerView rvAddOnMenu;
    public @InjectView(R.id.ivMenuImage)
    ImageView ivMenuImage;
    public @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    public @InjectView(R.id.rltMenuImage)
    RelativeLayout rltMenuImage;
    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.tvMenuName)
    TextView tvMenuName;
    public @InjectView(R.id.tvMenuDescription)
    TextView tvMenuDescription;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.header)
    View header;
    private MenuAddOnAdapter menuAddOnAdapter;
    private MenuItemModel menuModel;
    private ArrayList<MenuModifier> menuModifier = new ArrayList<>();


    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        commonMethods = new CommonMethods();
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        header.setBackgroundColor(getResources().getColor(R.color.transparent));
        ivBack.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        getintent();
        tvTitle.setText("");
        vBottom.setVisibility(View.GONE);
        tvMenuName.setText(menuModel.getMenuName());
        //menuModel.setDescription("النطق (مساعدة. · معلومات)) (/ ɪdliː /) هي نوع من كعكة الأرز اللذيذة ، والتي نشأت من شبه القارة الهندية ، وشائعة مثل الأطعمة الإفطار في جنوب الهند وبين التاميل من أصل هندي في سري لانكا. يتم صنع الكعك عن طريق تبخير الخليط المكون من عدس أسود مخمر (منزوع القشر) والأرز.");
        tvMenuDescription.setText(menuModel.getDescription());
        //System.out.println("Description   "+menuModel.getDescription());
        ivBack = (ImageView) findViewById(R.id.ivBack);
        commonMethods.rotateArrow(ivBack, this);
        if (menuModel.getMenuImage().getOriginal() != null) {
            imageUtils.loadImage(getApplicationContext(), ivMenuImage, progressBar, menuModel.getMenuImage().getOriginal());
        } else {
            ivMenuImage.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            //Convert DP to PiXel

            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int px = (int) (100 * scale + 0.5f);  // replace 100 with your dimensions

            rltMenuImage.getLayoutParams().height = px;
            ivBack.setColorFilter(ContextCompat.getColor(getApplicationContext(),
                    R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        //initRecyclerView();
    }

    /**
     * Setup recyclerview and load data
     **/
    public void initRecyclerView() {
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(this);
        rvAddOnMenu.setLayoutManager(customLayoutManager);
        menuModifier = menuModel.getModifier();

        if (foodListModels.size() == 0) {

            for (int i = 0; i < menuModifier.size(); i++) {
                menuModifiermodel = menuModifier.get(i).getModifierItem();
                for (int j = 0; j < menuModifiermodel.size(); j++) {
                    if (j == 0) {
                        FoodListModel foodListModel = new FoodListModel();
                        foodListModel.setMenuId(menuModifier.get(i).getMenuId());
                        foodListModel.setFoodName(menuModifier.get(i).getMenuName());
                        foodListModel.setType("Menu");
                        foodListModels.add(foodListModel);
                    }

                    FoodListModel foodListModel = new FoodListModel();
                    foodListModel.setType("Modifier");
                    foodListModel.setMenuId(menuModifiermodel.get(j).getMenuId());
                    foodListModel.setFoodName(menuModifiermodel.get(j).getMenuName());
                    foodListModel.setFoodPrice(menuModifiermodel.get(j).getMenuPrice());
                    foodListModel.setMenuVisible(menuModifiermodel.get(j).getMenuVisible());
                    foodListModels.add(foodListModel);
                }
            }
            menuAddOnAdapter = new MenuAddOnAdapter(this, foodListModels);
            rvAddOnMenu.setAdapter(menuAddOnAdapter);
            rvAddOnMenu.setHasFixedSize(true);
        }


    }

    /**
     * Get intent values from previous Activity
     **/
    public void getintent() {
        Intent intent = this.getIntent();
        //title = intent.getStringExtra("title");
        menuPostion = intent.getIntExtra("position", 0);
        menuModel = (MenuItemModel) intent.getSerializableExtra("menu_item");
    }

}
