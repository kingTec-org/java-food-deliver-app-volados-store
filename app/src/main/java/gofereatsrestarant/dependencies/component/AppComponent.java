package gofereatsrestarant.dependencies.component;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import javax.inject.Singleton;

import dagger.Component;
import gofereatsrestarant.adapters.main.OrderAdapter;
import gofereatsrestarant.adapters.main.OrderDetailsAdapter;
import gofereatsrestarant.adapters.main.OrderHistoryAdapter;
import gofereatsrestarant.adapters.main.OrderOutDeliveryAdapter;
import gofereatsrestarant.adapters.main.OrderPendingAdapter;
import gofereatsrestarant.adapters.main.OrderRecentAdapter;
import gofereatsrestarant.adapters.main.OrderScheduledAdapter;
import gofereatsrestarant.adapters.main.menu.MenuAddOnAdapter;
import gofereatsrestarant.adapters.main.menu.MenuExpandableAdapter;
import gofereatsrestarant.adapters.main.menu.MenuMainAdapter;
import gofereatsrestarant.adapters.main.menu.MenuSubAdapter;
import gofereatsrestarant.backgroundtask.ImageCompressAsyncTask;
import gofereatsrestarant.configs.RunTimePermission;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.dependencies.module.AppContainerModule;
import gofereatsrestarant.dependencies.module.ApplicationModule;
import gofereatsrestarant.dependencies.module.NetworkModule;
import gofereatsrestarant.pushnotification.MyFirebaseInstanceIDService;
import gofereatsrestarant.pushnotification.MyFirebaseMessagingService;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.DateTimeUtility;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.utils.WebServiceUtils;
import gofereatsrestarant.views.login.ForgotPassword;
import gofereatsrestarant.views.login.LanguageAdapter;
import gofereatsrestarant.views.login.LoginActivity;
import gofereatsrestarant.views.login.Logout;
import gofereatsrestarant.views.main.BaseActivity;
import gofereatsrestarant.views.main.CancelActivity;
import gofereatsrestarant.views.main.DelayOrderActivity;
import gofereatsrestarant.views.main.MainActivity;
import gofereatsrestarant.views.main.MapsActivity;
import gofereatsrestarant.views.main.OrderDetails;
import gofereatsrestarant.views.main.OrderHistory;
import gofereatsrestarant.views.main.OrderHistoryDetails;
import gofereatsrestarant.views.main.menu.MenuActivity;
import gofereatsrestarant.views.main.menu.MenuItemActivity;
import gofereatsrestarant.views.main.menu.ResendOrderRequest;
import gofereatsrestarant.views.main.menu.SubMenuItemActivity;
import gofereatsrestarant.views.splash.SplashActivity;


/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(ForgotPassword forgotPassword);

    void inject(BaseActivity baseActivity);

    void inject(MainActivity mainActivity);

    void inject(MapsActivity mapsActivity);

    void inject(OrderDetails orderDetails);

    void inject(OrderHistory orderHistory);

    void inject(OrderHistoryDetails orderHistoryDetails);

    void inject(MenuActivity menuActivity);

    void inject(SubMenuItemActivity subMenuItemActivity);

    void inject(MenuItemActivity menuItemActivity);

    void inject(CancelActivity cancelActivity);

    void inject(DelayOrderActivity delayOrderActivity);

    void inject(Logout logout);

    void inject(ResendOrderRequest resendOrderRequest);

    // Fragments
   /* void inject(ProfileFragment profileFragment);*/


    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);


    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(OrderAdapter orderAdapter);

    void inject(OrderPendingAdapter orderPendingAdapter);

    void inject(OrderOutDeliveryAdapter orderOutDeliveryAdapter);

    void inject(OrderRecentAdapter orderRecentAdapter);

    void inject(OrderScheduledAdapter orderScheduledAdapter);

    void inject(OrderDetailsAdapter orderDetailsAdapter);

    void inject(OrderHistoryAdapter orderHistoryAdapter);

    void inject(MenuMainAdapter menuMainAdapter);

    void inject(MenuExpandableAdapter menuExpandableAdapter);

    void inject(MenuSubAdapter menuSubAdapter);

    void inject(MenuAddOnAdapter menuAddOnAdapter);

    //void inject(MenuAddOnAdapterNew menuAddOnAdapter);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);
    void inject(LanguageAdapter languageAdapter);


}
