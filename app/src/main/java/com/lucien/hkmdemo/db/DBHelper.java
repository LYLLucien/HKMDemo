package com.lucien.hkmdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.lucien.hkmdemo.utils.common.CommonLog;

/**
 * Created by lucien.li on 2015/10/6.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String CLASSTAG = DBHelper.class.getSimpleName();
    private Context context;

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + DBConstants.TABLE_MOVIE + " (" +
                        DBConstants._ID + " " + DBConstants.INTEGER + " PRIMARY KEY," +
                        DBConstants.NAME + " " + DBConstants.VARCHAR(32) + " UNIQUE ON CONFLICT REPLACE, " +
                        DBConstants.THUMBNAIL_URL + " " + DBConstants.TEXT + "," +
                        DBConstants.OPEN_DATE + " " + DBConstants.VARCHAR(64) + "," +
                        DBConstants.TOTAL_REVENUE + " " + DBConstants.TEXT + "," +
                        DBConstants.URL + " " + DBConstants.TEXT + "," +
                        DBConstants.CREATED_AT + " " + DBConstants.VARCHAR(32) + "," +
                        DBConstants.UPDATED_AT + " " + DBConstants.VARCHAR(32) + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CommonLog.i(CLASSTAG, "onUpgrade: from " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            default:
                db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_MOVIE);
                onCreate(db);
        }
    }

    public static final class DBConstants implements BaseColumns {
        public static final String DB_NAME = "hkm.db";
        public static final int VERSION = 1;

        public static final String TEXT = "text";
        public static final String INTEGER = "INTEGER";

        public static String VARCHAR(int num) {
            return "varchar(" + num + ")";
        }

        public static final String TABLE_MOVIE = "movie";

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String THUMBNAIL_URL = "thumbnailUrl";
        public static final String OPEN_DATE = "openDate";
        public static final String TOTAL_REVENUE = "totalRevenue";
        public static final String URL = "url";

        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";


    }
}
