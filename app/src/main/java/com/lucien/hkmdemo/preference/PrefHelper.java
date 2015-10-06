package com.lucien.hkmdemo.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

/**
 * Created by lucien.li on 2015/10/6.
 */
public class PrefHelper {

    private Context context;
    private static PrefHelper prefHelper;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public static PrefHelper getPrefInstance(Context context) {
        if (prefHelper == null) {
            prefHelper = new PrefHelper(context);
        }
        return prefHelper;
    }

    private PrefHelper(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public int getMovieListTypeFromPref() {
        return prefs.getInt(PrefConstant.TYPE, PrefConstant.DEFAULT_TYPE);
    }

    public void setMovieListTypeToPref(int type) {
        editor = prefs.edit();
        editor.putInt(PrefConstant.TYPE, type);
        editor.commit();
    }

    public static final class PrefConstant implements BaseColumns {
        public static final String TYPE = "type";

        public static final int DEFAULT_TYPE = 0;
    }
}
