package gofereatsrestarant.views.login;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.login
 * @category LoginActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.Constants;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.datamodels.main.JsonResponse;
import gofereatsrestarant.interfaces.ApiService;
import gofereatsrestarant.interfaces.ServiceListener;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.RequestCallback;
import gofereatsrestarant.views.customize.CustomDialog;
import gofereatsrestarant.views.main.MainActivity;

/*****************************************************************
 Restaurant email login
 ****************************************************************/
public class LoginActivity extends AppCompatActivity implements ServiceListener {
    public static android.app.AlertDialog alertDialogStores;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.edtEmail)
    EditText edtEmail;
    public @InjectView(R.id.edtPassWord)
    EditText edtPassWord;
    public @InjectView(R.id.btnSignIn)
    Button btnSignIn;
    public @InjectView(R.id.tvForgotPassword)
    TextView tvForgotPassWord;
    public @InjectView(R.id.tvError)
    TextView tvError;
    public AlertDialog dialog;
    private String accessToken;
    private String restaurantName;
    public RecyclerView languageView;
    public LanguageAdapter LanguageAdapter;
    public List<CurrencyModel> languagelist;
    public static android.app.AlertDialog alertDialogStores1;
    public static android.app.AlertDialog alertDialogStores2;
    public @InjectView(R.id.languagelayout)
    RelativeLayout languagelayout;
    public @InjectView(R.id.languagetext)
    TextView LanguageText;
    public @InjectView(R.id.language)
    TextView language;
    public String Language;
    public String LanguageCode;
    public static boolean langclick=false;
    @OnClick(R.id.btnSignIn)
    public void login() {
        if (!validate()) {
            return;
        } else {
            userLogin();
        }
    }

    @OnClick(R.id.tvForgotPassword)
    public void forgotPassword() {
        Intent login = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivity(login);
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    @OnTextChanged({R.id.edtEmail, R.id.edtPassWord})
    protected void handleTextChange(Editable editable) {
        //validate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);


        String lang = sessionManager.getLanguage();
        language.setText(lang);

        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            edtEmail.setGravity(Gravity.END);
            edtEmail.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        languagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languagelist(); // Show curtency list
            }
        });



    }

    /**
     * To check wheather a email is a valid or not
     *
     * @return returns a boolean value true valid email false invalid
     */
    public boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();
        String password = edtPassWord.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getResources().getString(R.string.email_error));
            valid = false;
            return valid;
        } else {
            tvError.setVisibility(View.GONE);
            tvError.setText("");
        }

        if (password.isEmpty() || password.length() < 6) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getResources().getString(R.string.password_error));
            valid = false;
        } else {
            tvError.setVisibility(View.GONE);
            tvError.setText("");
        }

        return valid;
    }

    /**
     * Restaurant Login Api
     */
    public void userLogin() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.login(sessionManager.getType(), getDatas()).enqueue(new RequestCallback(this));
    }

    /**
     * Setting User Login details in hashmap
     *
     * @return Hashmap that are to be passed
     */
    private HashMap<String, String> getDatas() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", edtEmail.getText().toString().trim());
        hashMap.put("password", edtPassWord.getText().toString().trim());
        hashMap.put("language", sessionManager.getLanguageCode());
        return hashMap;
    }

    /**
     * Result From API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.showMessage(this, dialog, data);
            return;
        }

        if (jsonResp.isSuccess()) {
            onSuccessLogin(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Functionality of getting success response
     *
     * @param jsonResponse Success response of successful login
     */
    private void onSuccessLogin(JsonResponse jsonResponse) {
        accessToken = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.ACCESS_TOKEN, String.class);
        restaurantName = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.RESTAURANT_NAME, String.class);
        sessionManager.setToken(accessToken);
        sessionManager.setRestaurantName(restaurantName);

        Intent login = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(login);
        finish();
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
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
                    //langclick = false;
                    String lan = sessionManager.getLanguage();
                    language.setText(lan);
                    recreate();
                    //Intent intent = new Intent(SigninSignupActivity.this, SigninSignupActivity.class);
                    Intent intent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            }
        });

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


}
