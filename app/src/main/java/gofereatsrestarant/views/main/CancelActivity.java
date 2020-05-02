package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main
 * @category CancelActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.CancelReasonModel;
import gofereatsrestarant.datamodels.main.CancelResultModel;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.ImageUtils;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;

import static gofereatsrestarant.utils.Enums.REQ_CANCEL_ORDER;
import static gofereatsrestarant.utils.Enums.REQ_GET_CANCEL_ORDER_REASON;

/*****************************************************************
 Cancel Selected Order
 ****************************************************************/
public class CancelActivity extends AppCompatActivity implements ServiceListener {
    public ArrayList<CancelReasonModel> cancelReasonModels;
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
    public @InjectView(R.id.btnCancelReason)
    CustomButton btnCancelReason;
    public @InjectView(R.id.edtCancelReason)
    EditText edtCancelReason;
    public @InjectView(R.id.spinner)
    Spinner spinner;
    int orderId = 0;
    private AlertDialog dialog;

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnCancelReason)
    public void onCancel() {
        cancelOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBack, this);
        tvTitle.setText(getResources().getString(R.string.cancel_order));
        vBottom.setVisibility(View.VISIBLE);

        getIntents();
        getCancelReason();
    }

    /**
     * Get intent data from previous activity
     **/
    public void getIntents() {
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    /**
     * Call API to get Order details
     */
    public void getCancelReason() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.cancelOrdersReason(1, sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_CANCEL_ORDER_REASON, this));
    }

    /**
     * Call API to cancel order
     */
    public void cancelOrders() {

        String cancelMessage = edtCancelReason.getText().toString();

        int position = spinner.getSelectedItemPosition();

        if (position > 0) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.cancelOrders(cancelReasonModels.get(position).getCancelId(), cancelMessage, orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_CANCEL_ORDER, this));
        } else {
            commonMethods.showMessage(this, dialog, getResources().getString(R.string.select_cancel_reason));
        }
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

            case REQ_GET_CANCEL_ORDER_REASON:
                //swipeContainer.setRefreshing(false);
                if (jsonResp.isSuccess()) {
                    onSuccessCancelReason(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_CANCEL_ORDER:
                if (jsonResp.isSuccess()) {
                    // onSuccessOrderDetails(jsonResp);
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main);
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

    /**
     * Success response when request gets cancelled
     *
     * @param jsonResponse successful cancellation json response
     */

    public void onSuccessCancelReason(JsonResponse jsonResponse) {
        CancelResultModel cancelResultModel = gson.fromJson(jsonResponse.getStrResponse(), CancelResultModel.class);
        if (cancelResultModel != null) {
            cancelReasonModels = new ArrayList<>();
            CancelReasonModel cancelReasonModel = new CancelReasonModel();
            cancelReasonModel.setCancelId(0);
            cancelReasonModel.setReason(getResources().getString(R.string.select_cancel_reason));
            cancelReasonModels.add(cancelReasonModel);
            cancelReasonModels.addAll(cancelResultModel.getCancelReasonModels());

            String[] cancelReason = new String[cancelReasonModels.size()];

            for (int i = 0; i < cancelReasonModels.size(); i++) {
                cancelReason[i] = cancelReasonModels.get(i).getReason();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancel_reason_layout, cancelReason);

            spinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            /**
             *  Cancel trip reasons in spinner
             */
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }
    }
}
