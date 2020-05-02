package gofereatsrestarant.views.splash;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category SplashActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.Locale;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.views.login.LoginActivity;
import gofereatsrestarant.views.main.MainActivity;

/*****************************************************************
 Application splash screen
 ****************************************************************/
public class SplashActivity extends AppCompatActivity {

    public @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppController.getAppComponent().inject(this);
        sessionManager.setType("1");
        setSplashTimer();
       // checkForUpdates();
        setLocale();
    }

    /**
     * Set wait time for splash screen
     **/
    private void setSplashTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goIntent();
            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
       // checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
       // unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    /**
     * Call home page or login page based on token
     **/
    public void goIntent() {

        if (TextUtils.isEmpty(sessionManager.getToken())) {

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("sound", false);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("sound", false);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    public void setLocale() {
        String lang = sessionManager.getLanguage();

        if (!lang.equals("")) {
            String langC = sessionManager.getLanguageCode();
            Locale locale = new Locale(langC);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            SplashActivity.this.getResources().updateConfiguration(config, SplashActivity.this.getResources().getDisplayMetrics());
        } else {
            sessionManager.setLanguage("English");
            sessionManager.setLanguageCode("en");
        }


    }
}
