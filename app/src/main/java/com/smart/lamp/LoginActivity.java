package com.smart.lamp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.smart.lamp.net.request.SignIn;
import com.smart.lamp.net.response.UserInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;
import com.smart.lamp.net.util.RetrofitCallBack;
import com.smart.lamp.net.util.BusinessManager;
import com.smart.lamp.util.DataCache;

import retrofit2.Call;

/**
 * TODO login page
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 5/3/2019 11:50 PM
 */
public class LoginActivity extends BaseActivity {
    /**
     * 用户名输入框
     */
    private EditText etUserName;
    /**
     * 密码输入框
     */
    private EditText etPwd;
    /**
     * 交易管理类实例
     */
    private BusinessManager businessManager;

    @Override
    protected void onCreateLifecycle(Bundle saveInstanceState) {
        super.onCreateLifecycle(saveInstanceState);
        businessManager = new BusinessManager("");
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        etUserName = findViewById(R.id.userName);
        etPwd = findViewById(R.id.pwd);
        etUserName.setText("15961117089");
        etPwd.setText("zj12345678");
        findViewById(R.id.signIn).setOnClickListener(view -> signIn());
    }

    /**
     * 登录
     */
    private void signIn() {
        final String userName = etUserName.getText().toString();
        final String pwd = etPwd.getText().toString();
        // 数据校验
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        businessManager.signIn(new SignIn(userName, pwd), new RetrofitCallBack<BaseResponseEntity<UserInfo>>(getApplicationContext()) {
            @Override
            protected void onResponse(BaseResponseEntity<UserInfo> response) {
                dismissLoading();
                if (response.getStatus() == 0) {
                    // 登录成功，缓存 token，跳转至主界面
                    String accessToken = response.getResultObj().AccessToken;
                    DataCache.updateAccessToken(accessToken);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userBaseResponseEntity", response);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<UserInfo>> call, Throwable t) {
                super.onFailure(call, t);
                dismissLoading();

            }
        });
    }
}

