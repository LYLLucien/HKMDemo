package com.lucien.hkmdemo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.lucien.hkmdemo.R;
import com.lucien.hkmdemo.api.Api;
import com.lucien.hkmdemo.api.ApiHelper;
import com.lucien.hkmdemo.db.DBDao;
import com.lucien.hkmdemo.db.DBService;
import com.lucien.hkmdemo.model.AccountModel;
import com.lucien.hkmdemo.ui.MainActivity;
import com.lucien.hkmdemo.utils.common.CommonLog;
import com.lucien.hkmdemo.utils.common.CommonUtils;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class LoginHandler extends AsyncTask<AccountModel, Integer, Boolean> {

    private final String CLASSTAG = LoginHandler.class.getSimpleName();

    private Activity activity;
    private Api api;

    public LoginHandler(Activity activity) {
        this.activity = activity;
        this.api = Api.getApiInstance(activity);
    }

    @Override
    protected Boolean doInBackground(AccountModel... params) {
        return ApiHelper.loginAccount(api, params[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        CommonLog.i(CLASSTAG, "progress: " + values[0]);
    }

    @Override
    protected void onPostExecute(Boolean isLogin) {
        super.onPostExecute(isLogin);
        if (isLogin) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } else {
            CommonUtils.showShortToast(activity, activity.getResources().getString(R.string.login_failed));
        }
    }

}