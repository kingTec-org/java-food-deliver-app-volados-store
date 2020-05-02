package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category DelayOrderActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;

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
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;

import static gofereatsrestarant.utils.Enums.REQ_DELAY_ORDER;

/*****************************************************************
 Set delay time for Selected Order
 ****************************************************************/
public class DelayOrderActivity extends AppCompatActivity implements ServiceListener {
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/

    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.btnDelay)
    CustomButton btnDelay;
    public @InjectView(R.id.edtDelayReason)
    EditText edtDelayReason;
    public @InjectView(R.id.spinner)
    Spinner spinner;

    private int orderId = 0;
    private String[] delayTime ;
    private int[] delayMins = {0, 5, 10, 15, 20, 25, 30};
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnDelay)
    public void onDelay() {
        delayOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_order);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack, this);
        delayTime = new String[]{getResources().getString(R.string.select_delay_time), "5 "+getResources().getString(R.string.minutes),
                "10 "+getResources().getString(R.string.minutes),
                "15 "+getResources().getString(R.string.minutes),
                "20 "+getResources().getString(R.string.minutes),
                "25 "+getResources().getString(R.string.minutes),
                "30 "+getResources().getString(R.string.minutes)};
        tvTitle.setText(getResources().getString(R.string.delay_order));
        vBottom.setVisibility(View.VISIBLE);

        getIntents();
        getDelayTime();
    }

    /**
     * Get intent data from previous activity
     **/
    public void getIntents() {
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    /**
     * Call API to cancel order
     */
    public void delayOrders() {

        String delayMessage = edtDelayReason.getText().toString();

        int position = spinner.getSelectedItemPosition();

        if (position > 0) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.delayOrders(orderId, delayMins[position], delayMessage, sessionManager.getToken()).enqueue(new RequestCallback(REQ_DELAY_ORDER, this));
        } else {
            commonMethods.showMessage(this, dialog, getResources().getString(R.string.select_delay_time));
        }
    }

    public void getDelayTime() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancel_reason_layout, delayTime);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /**
         *  Cancel trip reasons in spinner
         */
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_DELAY_ORDER:
                if (jsonResp.isSuccess()) {
                    //onBackPressed();
                    Intent intent = new Intent();
                    setResult(REQ_DELAY_ORDER, intent);
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }
}
