package gofereatsrestarant.utils;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage utils
 * @category RequestCallback
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONObject;

import javax.inject.Inject;

import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.Constants;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.views.login.LoginActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*****************************************************************
 RequestCallback
 ****************************************************************/
public class RequestCallback implements Callback<ResponseBody> {

    public @Inject
    JsonResponse jsonResp;
    public @Inject
    Context context;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ApiService apiService;
    private ServiceListener listener;
    private int requestCode = 0;
    private String requestData = "";
    //private String oldToken = "";

    public RequestCallback() {
        AppController.getAppComponent().inject(this);
    }

    public RequestCallback(ServiceListener listener) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
    }

    public RequestCallback(int requestCode, ServiceListener listener) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
        this.requestCode = requestCode;
    }

    public RequestCallback(int requestCode, ServiceListener listener, String requestData) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
        this.requestCode = requestCode;
        this.requestData = requestData;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        this.listener.onSuccess(getSuccessResponse(call, response), requestData);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        this.listener.onFailure(getFailureResponse(call, t), requestData);
    }

    private JsonResponse getFailureResponse(Call<ResponseBody> call, Throwable t) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method());
                jsonResp.setRequestCode(requestCode);
                jsonResp.setUrl(call.request().url().toString());
                LogManager.i(call.request().toString());
            }
            jsonResp.setOnline(isOnline());
            jsonResp.setErrorMsg(t.getMessage());
            jsonResp.setSuccess(false);
            jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
            requestData = (context != null && !isOnline()) ? context.getResources().getString(R.string.network_failure) : t.getMessage();
            LogManager.e(t.getMessage());
            LogManager.e(String.valueOf(requestCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }

    private JsonResponse getSuccessResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method());
                jsonResp.setRequestCode(requestCode);
                jsonResp.setUrl(call.request().url().toString());
                LogManager.i(call.request().toString());
            }
            if (response != null) {
                jsonResp.setResponseCode(response.code());
                jsonResp.setSuccess(false);
                jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
                if (response.isSuccessful() && response.body() != null) {
                    String strJson = response.body().string();
                    jsonResp.setStrResponse(strJson);
                    jsonResp.setStatusMsg(getStatusMsg(strJson));
                    if (jsonResp.getStatusMsg().equalsIgnoreCase("Token Expired")) {
                        jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
                        //String urls = call.request().url().toString();
                        //urls=urls.replace(oldToken, sessionManager.getToken());

                    }
                    jsonResp.setSuccess(isSuccess(strJson));
                    LogManager.e(strJson);
                } else {
                    System.out.println("Request Call for response: " + response);
                    String strJson = response.errorBody().string();
                    jsonResp.setErrorMsg(response.errorBody().string());
                    if (response.errorBody() != null) {
                        System.out.println("Request Call Error" + response.errorBody().string());
                        System.out.println("Request Call Error" + strJson);
                        jsonResp.setStrResponse(strJson);
                        jsonResp.setStatusMsg(getStatusMsg(strJson));
                        if (jsonResp.getStatusMsg().equalsIgnoreCase("Token Expired")) {
                            jsonResp.setStatusMsg("");
                        }


                        if (response.code() == 401 || response.code() == 404) {
                            sessionManager.clearToken();
                            sessionManager.clearAll();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    }
                }
                jsonResp.setRequestData(requestData);
                jsonResp.setOnline(isOnline());
                requestData = (context != null && !isOnline()) ? context.getResources().getString(R.string.network_failure) : "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }

    private boolean isOnline() {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private boolean isSuccess(String jsonString) {
        boolean isSuccess = false;
        try {
            if (!TextUtils.isEmpty(jsonString)) {
                String statusCode = (String) getJsonValue(jsonString, Constants.STATUS_CODE, String.class);
                isSuccess = !TextUtils.isEmpty(statusCode) && statusCode.equals("1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private String getStatusMsg(String jsonString) {
        String statusMsg = "";
        try {
            if (!TextUtils.isEmpty(jsonString)) {
                statusMsg = (String) getJsonValue(jsonString, Constants.STATUS_MSG, String.class);
                if (statusMsg.equalsIgnoreCase("Token Expired")) {
                    //oldToken = sessionManager.getToken();
                    String token = (String) getJsonValue(jsonString, Constants.REFRESH_ACCESS_TOKEN, String.class);
                    sessionManager.setToken(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusMsg;
    }

    private Object getJsonValue(String jsonString, String key, Object object) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) object = jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return object;
    }


}
