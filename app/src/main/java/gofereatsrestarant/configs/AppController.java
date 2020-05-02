package gofereatsrestarant.configs;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage configs
 * @category AppController
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import gofereatsrestarant.R;
import gofereatsrestarant.dependencies.component.AppComponent;
import gofereatsrestarant.dependencies.component.DaggerAppComponent;
import gofereatsrestarant.dependencies.module.ApplicationModule;
import gofereatsrestarant.dependencies.module.NetworkModule;
//import instagram.InstagramHelper;

/*****************************************************************
 AppController
 ****************************************************************/
public class AppController extends Application {

    private static AppComponent appComponent;
    // private static InstagramHelper instagramHelper;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule(getResources().getString(R.string.base_url)))
                .build();
        MultiDex.install(this); // Multiple dex initialize
     /*instagramHelper = new InstagramHelper.Builder()
             .withClientId(getResources().getString(R.string.instagram_client_id))
                .withRedirectUrl(getResources().getString(R.string.redirect_url))
                .withScope("basic+public_content+relationships")
                .build();*/
    }

   /* public static InstagramHelper getInstagramHelper() {
        return instagramHelper;
    }*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
