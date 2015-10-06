package com.lucien.hkmdemo.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucien.hkmdemo.R;
import com.lucien.hkmdemo.model.MovieModel;
import com.lucien.hkmdemo.utils.img.ImgUtils;

import java.util.List;

/**
 * Created by lucien.li on 2015/10/5.
 */
public class MovieAdapter extends BaseAdapter {

    private Context context;
    private List<MovieModel> movieList;
    private LayoutInflater inflater;

    private class ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_name;
        TextView tv_total_revenue;
        TextView tv_open_date;
    }

    public MovieAdapter(Context context, List<MovieModel> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        bindView(item, movieList.get(position));
        return convertView;
    }

    private void bindView(ViewHolder item, MovieModel model) {
        ImgUtils.setBackImage(context, model.getThumbnailUrl(), item.iv_thumbnail);
        if (!TextUtils.isEmpty(model.getName()))
            item.tv_name.setText(model.getName());
        item.tv_total_revenue.setText(model.getTotalRevenue());
        if (!TextUtils.isEmpty(model.getOpenDate()))
            item.tv_open_date.setText(model.getOpenDate());
    }
}
