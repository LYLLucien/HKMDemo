package com.lucien.hkmdemo.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lucien.hkmdemo.R;
import com.lucien.hkmdemo.app.MyApplication;
import com.lucien.hkmdemo.db.DBDao;
import com.lucien.hkmdemo.db.DBHelper.DBConstants;
import com.lucien.hkmdemo.preference.PrefHelper;
import com.lucien.hkmdemo.ui.adapter.MovieCursorAdapter;
import com.lucien.hkmdemo.widget.PopupWindowMenu;

public class MainActivity extends Activity implements
        PopupWindow.OnDismissListener, PopupWindowMenu.OnMenuItemClickListener {

    private static final String[] PROJECTION_FROM = new String[]{
            DBConstants._ID, DBConstants.NAME, DBConstants.THUMBNAIL_URL,
            DBConstants.OPEN_DATE, DBConstants.TOTAL_REVENUE
    };
    public MyApplication application;

    private long firstTime;
    private DBDao dbDao;
    private PrefHelper prefHelper;
    private Cursor cursor;

    private int type = PrefHelper.PrefConstant.DEFAULT_TYPE;

    private TextView tv_rank_title;
    private Button btn_overflow;
    private ListView lv_movies;
    private MovieCursorAdapter adapter;
    private PopupWindowMenu popMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConfig();
        initView();
    }

    private void initConfig() {
        application = (MyApplication) getApplicationContext();
        dbDao = DBDao.getDBDaoInstance(application);
        prefHelper = PrefHelper.getPrefInstance(application);
        type = prefHelper.getMovieListTypeFromPref();
    }

    private void initView() {
        lv_movies = (ListView) this.findViewById(R.id.lv_movies);
        popMenu = new PopupWindowMenu(this);
        popMenu.setOnDismissListener(this);
        popMenu.setOnMenuItemClickListener(this);
        tv_rank_title = (TextView) this.findViewById(R.id.tv_rank_title);
        btn_overflow = (Button) this.findViewById(R.id.btn_overflow);
        btn_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopMenu();
            }
        });
        setRankTitle(type);
    }

    private void setRankTitle(int type) {
        switch (type) {
            case 0:
                tv_rank_title.setText(getResources().getString(R.string.movie_title) + getResources().getString(R.string.rank_up));
                break;
            case 1:
                tv_rank_title.setText(getResources().getString(R.string.movie_title) + getResources().getString(R.string.rank_down));
                break;
            default:
                tv_rank_title.setText(getResources().getString(R.string.movie_title) + getResources().getString(R.string.rank_up));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindData(type);
    }

    private void bindData(int type) {
        new AsyncTask<Integer, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Integer... params) {
                return dbDao.getMoviesByType(params[0]);
            }

            @Override
            protected void onPostExecute(Cursor result) {
                super.onPostExecute(result);
                cursor = result;
                if (adapter == null) {
                    adapter = new MovieCursorAdapter(application, result, PROJECTION_FROM);
                    lv_movies.setAdapter(adapter);
                } else {
                    adapter.changeCursor(result);
                    adapter.notifyDataSetChanged();
                }
            }
        }.execute(type);

    }

    private void openPopMenu() {
        if (popMenu == null) {
            return;
        }

        int[] location = new int[2];
        View view = btn_overflow;
        if (view != null) {
            view.getLocationOnScreen(location);
        }
        popMenu.setAnimationStyle(R.style.AppTheme);

        popMenu.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                location[0], location[1] + getResources().getDimensionPixelOffset(R.dimen.dimen_20dp));
    }

    @Override
    public void onBackPressed() {
        if (exitByDoubleClick())
            super.onBackPressed();
    }

    private boolean exitByDoubleClick() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            return true;
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(this, getResources().getString(R.string.alert_exit), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onMenuItemClick(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                type = 0;
                break;
            case R.id.btn_down:
                type = 1;
                break;
        }
        prefHelper.setMovieListTypeToPref(type);
        bindData(type);
        setRankTitle(type);
        popMenu.dismiss();
    }
}
