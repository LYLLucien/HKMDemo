package com.lucien.hkmdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucien.hkmdemo.R;
import com.lucien.hkmdemo.api.Api;
import com.lucien.hkmdemo.api.ApiHelper;
import com.lucien.hkmdemo.db.DBDao;
import com.lucien.hkmdemo.db.DBService;
import com.lucien.hkmdemo.handler.LoginHandler;
import com.lucien.hkmdemo.model.AccountModel;
import com.lucien.hkmdemo.utils.anim.AnimUtils;
import com.lucien.hkmdemo.widget.CleanableEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends Activity implements View.OnClickListener {

    private final String CLASSTAG = LoginActivity.class.getSimpleName();

    private Context context;
    private Activity activity;

    private LinearLayout layout_slider;
    private CircleImageView iv_institution_photo;
    private LinearLayout layout_login;
    private TextView tv_institution_title;
    private CleanableEditText et_username;
    private CleanableEditText et_password;
    private Button btn_login;
    private ProgressBar pb_init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initConfig();
        initView();
    }

    private void initConfig() {
        context = getApplicationContext();
        activity = this;
    }

    public void initView() {
        layout_slider = (LinearLayout) this.findViewById(R.id.layout_slider);
        iv_institution_photo = (CircleImageView) this.findViewById(R.id.iv_institution_photo);
        layout_login = (LinearLayout) this.findViewById(R.id.layout_login);
        tv_institution_title = (TextView) this.findViewById(R.id.tv_institution_title);
        et_username = (CleanableEditText) this.findViewById(R.id.et_username);
        et_password = (CleanableEditText) this.findViewById(R.id.et_password);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        pb_init = (ProgressBar) this.findViewById(R.id.pb_init);
        pb_init.setMax(100);

        btn_login.setOnClickListener(this);

        new InitTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                new LoginHandler(LoginActivity.this).
                        execute(new AccountModel(et_username.getText().toString(), et_password.getText().toString()));
                break;
        }
    }

    private class InitTask extends AsyncTask<Void, Integer, Void> {
        private Api api;
        private DBService service;
        int i = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            this.api = Api.getApiInstance(activity);
            this.service = DBDao.getDBDaoInstance(activity);
            publishProgress(1);
            ApiHelper.initMovieList(api, service);
            publishProgress(2);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pb_init.setProgress(progress[0].intValue());
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Animation slideUpAnimation = AnimUtils.slideUpAnimation(context);
            Animation fadeInAnim = AnimUtils.fadeInAnimation(context);
            final Animation showLoginAnim = AnimUtils.showLoginAnimation(context);

            layout_slider.startAnimation(slideUpAnimation);
            pb_init.startAnimation(fadeInAnim);
            fadeInAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    pb_init.setVisibility(View.GONE);
                    layout_login.setVisibility(View.VISIBLE);
                    layout_login.startAnimation(showLoginAnim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            super.onPostExecute(aVoid);
        }

    }


}
