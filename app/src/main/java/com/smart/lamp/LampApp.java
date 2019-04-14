package com.smart.lamp;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.smart.lamp.model.VerifyInfo;
import com.smart.lamp.util.Constants;
import com.smart.lamp.util.SecurityUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TODO
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 5/3/2019 9:12 AM
 */
public class LampApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Gson gson = new Gson();
        String path = Environment.getExternalStorageDirectory() + File.separator + Constants.ROOT_DIR;
        File file = new File(path + File.separator + "verify.json");
        File dir = new File(path);
        if (!dir.exists() || !file.exists()) {
            if(dir.mkdirs()) {
                long currentTimeMillis = System.currentTimeMillis();
                String currentTimeMillisString = String.valueOf(currentTimeMillis);
                Log.e(">>>>>加密", currentTimeMillisString);
                String timeStamp = SecurityUtil.encrypt(currentTimeMillisString, 60);

                VerifyInfo info = new VerifyInfo(timeStamp);
                String json = gson.toJson(info);
                Log.e(">>>>>加密", json);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(json.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}