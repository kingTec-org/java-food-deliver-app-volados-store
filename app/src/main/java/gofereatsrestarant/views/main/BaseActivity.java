package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category MenuActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.login.CurrencyModel;
import gofereatsrestarant.views.login.LanguageAdapter;
import gofereatsrestarant.views.login.Logout;
import gofereatsrestarant.views.main.menu.MenuActivity;

import static gofereatsrestarant.views.login.LoginActivity.alertDialogStores;
import static gofereatsrestarant.views.login.LoginActivity.langclick;

/*****************************************************************
 Base Activity for global navigation view
 ****************************************************************/
public class BaseActivity extends AppCompatActivity implements ServiceListener {

    public Context mContext;
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
    private androidx.appcompat.app.AlertDialog dialog;

    private String type;
    private Bundle bndlanimation;
    public RecyclerView languageView;
    public List<CurrencyModel> languagelist;
    public LanguageAdapter LanguageAdapter;




   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
    }*/

    protected void onCreateDrawer(Bundle savedInstanceState, String type) {
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        this.type = type;
        dialog = commonMethods.getAlertDialog(this);
        init();

    }


    private void init() {
        bindResources();
        setUpDrawer();
    }

    /**
     * Set selected menu and set animation
     **/
    public void bindResources() {
        mContext = this;
        //bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.ub__slide_in_right,R.anim.ub__slide_out_left).toBundle();
        bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.activity_open_fadein, R.anim.activity_close_fadeout).toBundle();

        if ("order".equalsIgnoreCase(type))
            navigationView.getMenu().getItem(0).setChecked(true);
        else if ("orderHistory".equalsIgnoreCase(type))
            navigationView.getMenu().getItem(1).setChecked(true);
        else if ("menu".equalsIgnoreCase(type))
            navigationView.getMenu().getItem(2).setChecked(true);
        else if ("change_language".equalsIgnoreCase(type))
            navigationView.getMenu().getItem(3).setChecked(true);
        else if ("logout".equalsIgnoreCase(type))
            navigationView.getMenu().getItem(4).setChecked(true);
    }

    /**
     * Setup navigation toolbar and OnClick
     **/
    private void setUpDrawer() {

        View header = navigationView.getHeaderView(0);
        NavigationHeader navigationHeader = new NavigationHeader();
        Log.v("sessionManager", "getRestaurantName()" + sessionManager.getRestaurantName());
        // 5. We bind the elements of the included layouts.
        ButterKnife.inject(navigationHeader, header);
        navigationHeader.tvTitle.setText(sessionManager.getRestaurantName());
        navigationView.setNavigationItemSelectedListener
                (
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(final MenuItem item) {
                                drawerLayout.closeDrawer(GravityCompat.START);

                                switch (item.getItemId()) {

                                    case R.id.orders:
                                        item.setChecked(true);
                                        tvToolBarTitle.setVisibility(View.GONE);

                                        Intent home = new Intent(mContext, MainActivity.class);
                                        home.putExtra("type", "order");
                                        startActivity(home, bndlanimation);
                                        finish();
                                        break;

                                    case R.id.order_history:
                                        item.setChecked(true);
                                        Intent orderHistory = new Intent(mContext, OrderHistory.class);
                                        orderHistory.putExtra("type", "orderHistory");
                                        startActivity(orderHistory, bndlanimation);
                                        finish();
                                        break;
                                    case R.id.menu:
                                        item.setChecked(true);
                                        Intent menu = new Intent(mContext, MenuActivity.class);
                                        menu.putExtra("type", "menu");
                                        startActivity(menu, bndlanimation);
                                        finish();
                                        break;
                                    case R.id.changelanguage:
                                        item.setChecked(true);
                                        languagelist();
                                        break;
                                    case R.id.logout:
                                        Intent log = new Intent(mContext, Logout.class);
                                        startActivity(log);
                                        break;
                                    default:
                                        break;
                                }

                                return true;
                            }
                        }
                );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        System.out.println("api succes");
        commonMethods.hideProgressDialog();
        if (jsonResp.isSuccess()) {

        } else {
            commonMethods.showMessage(this, dialog,jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
    }


    /**
     * Annotation  using ButterKnife lib to Injection and OnClick for Navigation header
     **/
    class NavigationHeader {
        public @InjectView(R.id.ivClose)
        ImageView ivClose;
        public @InjectView(R.id.tvTitle)
        TextView tvTitle;

        @OnClick(R.id.ivClose)
        public void close() {
            drawerLayout.closeDrawers();
        }

    }

    public void languagelist() {

        languageView = new RecyclerView(this);
        languagelist = new ArrayList<>();
        loadlang();

        LanguageAdapter = new LanguageAdapter(this, languagelist);
        languageView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        languageView.setAdapter(LanguageAdapter);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.currency_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(getString(R.string.selectlanguage));


        alertDialogStores = new android.app.AlertDialog.Builder(this)
                .setCustomTitle(view)
                .setView(languageView)
                .setCancelable(true)
                .show();

        alertDialogStores.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (langclick) {
                    langclick = false;
                    successLanguageChange();
                    //commonMethods.showProgressDialog(BaseActivity.this, customDialog);
                    apiService.updateLanguage(sessionManager.getType(),sessionManager.getToken(), sessionManager.getLanguageCode()).enqueue(new RequestCallback(BaseActivity.this));


                }
            }
        });

    }

    public void successLanguageChange() {
        String lan = sessionManager.getLanguage();
        //setLocale();
        recreate();
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sound",false);
        startActivity(intent);
    }


    public void loadlang() {

        String[] languages = getResources().getStringArray(R.array.language);
        String[] langCode = getResources().getStringArray(R.array.languageCode);
        for (int i = 0; i < languages.length; i++) {
            CurrencyModel listdata = new CurrencyModel();
            listdata.setCurrencyName(languages[i]);
            listdata.setCurrencySymbol(langCode[i]);
            languagelist.add(listdata);

        }
    }

    public void setLocale()
    {
        Locale myLocale = new Locale(sessionManager.getLanguageCode());
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}