package com.smart.lamp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * TODO base activity class
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 4/3/2019 20:26 PM
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 进度框
     */
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateLifecycle(savedInstanceState);
        setContentView(setLayoutRes());
        init();
    }

    protected void onCreateLifecycle(Bundle saveInstanceState) {
    }

    protected abstract int setLayoutRes();

    protected abstract void init();


    /**
     * 显示进度框
     */
    protected void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(this, R.style.App_Dialog);
            loadingDialog.setContentView(R.layout.dialog_loading);
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
    }

    /**
     * 关闭进度框
     */
    protected void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放 dialog
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
