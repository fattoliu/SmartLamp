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

/**
 * TODO
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 5/3/2019 11:50 PM
 */
public class LoginActivity extends BaseActivity {
    private EditText etUserName;
    private EditText etPwd;

    @Override
    protected void onCreateLifecycle(Bundle saveInstanceState) {
        super.onCreateLifecycle(saveInstanceState);
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUserName = findViewById(R.id.userName);
        etPwd = findViewById(R.id.pwd);
    }

    @Override
    protected void initData() {
        etUserName.setText("15961117089");
        etPwd.setText("zj12345678");
    }

    @Override
    protected void registerListener() {
        findViewById(R.id.signIn).setOnClickListener(view -> signIn());
    }

    private void signIn() {
        final String userName = etUserName.getText().toString();
        final String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        BusinessManager netWorkBusiness = new BusinessManager("");
        netWorkBusiness.signIn(new SignIn(userName, pwd), new RetrofitCallBack<BaseResponseEntity<UserInfo>>(getApplicationContext()) {
            @Override
            protected void onResponse(BaseResponseEntity<UserInfo> response) {
                if (response.getStatus() == 0) {
                    String accessToken = response.getResultObj().AccessToken;
                    DataCache.updateAccessToken(getApplicationContext(), accessToken);
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
        });
    }
}

