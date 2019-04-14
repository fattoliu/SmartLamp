package com.smart.lamp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smart.lamp.model.VerifyInfo;
import com.smart.lamp.util.Constants;
import com.smart.lamp.util.SecurityUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * TODO
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 4/3/2019 20:26 PM
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateLifecycle(savedInstanceState);
        setContentView(setLayoutRes());
        initView();
        initData();
        registerListener();

        verify();
    }

    private void verify() {
        // 从本地取出信息，做校验
        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + Constants.ROOT_DIR;
            File file = new File(path + File.separator + "verify.json");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            int read = fis.read(buffer);
            if (read == file.length()) {
                String json = new String(buffer);
                Log.e("<<<<<<<<解密", json);
                VerifyInfo info = new Gson().fromJson(json, VerifyInfo.class);
                String time = SecurityUtil.decrypt(info.timeStamp, 13);
                long timeLong = Long.parseLong(time);
                Log.e("<<<<<<<<解密", String.valueOf(timeLong));
                if ((System.currentTimeMillis() - timeLong) / 1000 > 30 * 24 * 60 * 60) {
                    Toast.makeText(this, "抱歉，您的体验期已结束!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onCreateLifecycle(Bundle saveInstanceState) {
    }

    protected abstract int setLayoutRes();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void registerListener();


}
