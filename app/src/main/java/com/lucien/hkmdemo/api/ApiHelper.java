package com.lucien.hkmdemo.api;

import android.content.ContentValues;

import com.google.gson.reflect.TypeToken;
import com.lucien.hkmdemo.constant.Enumeration.ApiStatus;
import com.lucien.hkmdemo.db.DBContentCreator;
import com.lucien.hkmdemo.db.DBService;
import com.lucien.hkmdemo.model.AccountModel.*;
import com.lucien.hkmdemo.model.AccountModel;
import com.lucien.hkmdemo.model.MovieModel;
import com.lucien.hkmdemo.utils.common.CommonLog;
import com.lucien.hkmdemo.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucien.hkmdemo.db.DBHelper.DBConstants;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class ApiHelper {

    public static final String CLASSTAG = ApiHelper.class.getSimpleName();

    public static boolean loginAccount(Api api, AccountModel loginAccount) {
        ApiResult result = new ApiResult();
        if (api.httpsLogin(result, loginAccount) == ApiStatus.Success) {
            CommonLog.i("result.getMsg(): " + result.getMsg());

            AccountModel.LoginStatus status = (LoginStatus) JsonUtils.parseJson(result.getMsg(), LoginStatus.class);
            if (status.getSuccess() == 1) {
                return true;
            }
            return false;
        }
        CommonLog.e("login error!");
        return false;
    }

    public static void initMovieList(Api api, DBService service) {
        CommonLog.i(CLASSTAG, "initMovieList");

        ApiResult result = new ApiResult();
        if (api.getHttpsMovies(result) == ApiStatus.Success) {
            CommonLog.d(CLASSTAG, "api result: " + result.getMsg());
            Type listType = new TypeToken<ArrayList<MovieModel>>() {
            }.getType();
            List<MovieModel> movieList = (List<MovieModel>) JsonUtils.parseListJson(result.getMsg(), listType);
            CommonLog.i(CLASSTAG, "movieList size: " + movieList.size());
            List<ContentValues> contentValuesList = new ArrayList<>();
            for (MovieModel movie : movieList) {
//                MovieModel movieTmp = (MovieModel) JsonUtils.parseJson(movie.getUrl(), MovieModel.class);
//                movie.setCreated_at(movieTmp.getCreated_at());
//                movie.setUpdated_at(movieTmp.getUpdated_at());
                CommonLog.i(CLASSTAG, "movie: " + movie.toString());

                ContentValues values = DBContentCreator.MovieCreator(movie);
                contentValuesList.add(values);
            }
            service.bulkInsert(DBConstants.TABLE_MOVIE, contentValuesList);
        } else {
            CommonLog.e("initMovieList failed!");
        }
    }
}
