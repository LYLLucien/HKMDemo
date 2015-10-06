package com.lucien.hkmdemo.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucien.hkmdemo.R;
import com.lucien.hkmdemo.db.DBHelper;
import com.lucien.hkmdemo.model.MovieModel;
import com.lucien.hkmdemo.utils.img.ImgUtils;

/**
 * Created by lucien.li on 2015/10/6.
 */
public class MovieCursorAdapter extends SimpleCursorAdapter {

    private Context context;
    private LayoutInflater inflater;

    private class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_name;
        TextView tv_total_revenue;
        TextView tv_open_date;
    }

    public MovieCursorAdapter(Context context, Cursor c, String[] from) {
        super(context, 0, c, from, null, 0);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder item = null;
        if (convertView == null) {
            item = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_movie, null, false);
            item.iv_thumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
            item.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            item.tv_total_revenue = (TextView) convertView.findViewById(R.id.tv_total_revenue);
            item.tv_open_date = (TextView) convertView.findViewById(R.id.tv_open_date);
            convertView.setTag(item);
        } else {
            item = (ViewHolder) convertView.getTag();
        }
        bindView(item, getMovieFromCursor(position));
        return convertView;
    }

    private MovieModel getMovieFromCursor(int position) {
        Cursor cursor = this.getCursor();
        cursor.moveToPosition(position);
        MovieModel movie = new MovieModel();
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.NAME));
        String imgUrl = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.THUMBNAIL_URL));
        String openDate = cursor.getString(cursor.getColumnIndex(DBHelper.DBConstants.OPEN_DATE));
        int totalRevenue = cursor.getInt(cursor.getColumnIndex(DBHelper.DBConstants.TOTAL_REVENUE));
        if (!TextUtils.isEmpty(name))
            movie.setName(name);
        if (!TextUtils.isEmpty(imgUrl))
            movie.setThumbnailUrl(imgUrl);
        if (!TextUtils.isEmpty(openDate))
            movie.setOpenDate(openDate);
        movie.setTotalRevenue(totalRevenue);
        return movie;
    }

    private void bindView(ViewHolder item, MovieModel model) {
        ImgUtils.setBackImage(context, model.getThumbnailUrl(), item.iv_thumbnail);
        if (!TextUtils.isEmpty(model.getName()))
            item.tv_name.setText(model.getName());
        item.tv_total_revenue.setText(model.getTotalRevenue() + "");
        if (!TextUtils.isEmpty(model.getOpenDate()))
            item.tv_open_date.setText(model.getOpenDate());
    }
}
