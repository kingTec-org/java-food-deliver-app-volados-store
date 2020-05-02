package gofereatsrestarant.views.login;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.login
 * @category ForgotPasswordActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.views.main.MainActivity;

/*****************************************************************
 Restaurant forgot password - Reset password link send to mail
 ****************************************************************/
public class ForgotPassword extends AppCompatActivity {

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.edtEmail)
    EditText edtEmail;
    public @InjectView(R.id.btnSubmit)
    Button btnSubmit;
    public @InjectView(R.id.tvError)
    TextView tvError;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @Inject
    CommonMethods commonMethods;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;

    @OnClick(R.id.btnSubmit)
    public void submit() {
        if (!validate()) {
            return;
        } else {
            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login);
            overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
        }
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        commonMethods.rotateArrow(ivBack, this);
        tvTitle.setText(getResources().getString(R.string.forgot_passwords));
        vBottom.setVisibility(View.GONE);
    }

    /**
     * To check wheather a email is a valid or not
     *
     * @return returns a boolean value true valid email false invalid
     */
    public boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getResources().getString(R.string.email_error));
            valid = false;
            return valid;
        } else {
            tvError.setVisibility(View.GONE);
            tvError.setText("");
        }
        return valid;
    }
}
