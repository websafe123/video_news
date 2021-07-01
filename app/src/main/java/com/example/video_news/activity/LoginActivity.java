package com.example.video_news.activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.video_news.R;
import com.example.video_news.api.Api;
import com.example.video_news.api.ApiConfig;
import com.example.video_news.api.TtitCallback;
import com.example.video_news.entity.LoginResponse;
import com.example.video_news.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;
    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        initData();
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                login(account, pwd);
            }
        });
    }

    private void login(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
//        navigateToWithFlag(HomeActivity.class,
//                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", account);
        params.put("password", pwd);
        Api.config(ApiConfig.LOGIN, params).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res,LoginResponse.class);
                if(loginResponse.getCode()==0){
                    String token  = loginResponse.getToken();
                    insertVal("token",token);
                    navigateToWithFlag(HomeActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showToastSync("登录成功");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("登录失败");
            }
        });
    }

}