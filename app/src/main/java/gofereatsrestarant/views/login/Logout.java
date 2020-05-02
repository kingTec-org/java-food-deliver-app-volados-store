package gofereatsrestarant.views.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;

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

/**
 * Created by trioangle on 15/6/18.
 */

public class Logout extends Activity implements ServiceListener {

    public Context mContext;
    public @InjectView(R.id.tvcancel)
    TextView tvcancel;
    public @InjectView(R.id.tvsignout)
    TextView tvsignout;
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
    private AlertDialog dialog;

    @OnClick(R.id.tvcancel)
    public void cancel() {
        finish();
    }

    @OnClick(R.id.tvsignout)
    public void signout() {
        logout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        mContext = this;
        dialog = commonMethods.getAlertDialog(mContext);
    }

    /**
     * Functionality after getting  successfull logout response
     */


    private void onSuccessLogOut() {
        String lang=sessionManager.getLanguage();
        String languagecode=sessionManager.getLanguageCode();
        sessionManager.clearToken();
        sessionManager.clearAll();
        sessionManager.setLanguage(lang);
        sessionManager.setLanguageCode(languagecode);
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        sessionManager.setType("1");
        finish();

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessLogOut();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            //commonMethods.showMessage(mContext, dialog, jsonResp.getStatusMsg());
            sessionManager.clearToken();
            sessionManager.clearAll();
            finishAffinity();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            sessionManager.setType("1");
            finish();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            tvsignout.setEnabled(true);
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * logout api calling function
     */

    public void logout() {
        //commonMethods.showProgressDialog(this,customDialog);
        tvsignout.setEnabled(false);
        apiService.logout(sessionManager.getToken()).enqueue(new RequestCallback(this));
    }


}
