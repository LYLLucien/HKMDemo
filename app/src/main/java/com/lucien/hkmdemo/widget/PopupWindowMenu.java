package com.lucien.hkmdemo.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lucien.hkmdemo.R;


/**
 * Created by lucien.li on 2015/9/1.
 */
public class PopupWindowMenu extends PopupWindow implements View.OnClickListener {

    private View mRootView;
    private LayoutInflater inflater;
    private OnMenuItemClickListener listener;
    private Button btn_up, btn_down;

    public interface OnMenuItemClickListener {
        void onMenuItemClick(View view);
    }

    public PopupWindowMenu(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.item_pop_menu, null);
        btn_up = (Button) mRootView.findViewById(R.id.btn_up);
        btn_up.setOnClickListener(this);
        btn_down = (Button) mRootView.findViewById(R.id.btn_down);
        btn_down.setOnClickListener(this);

        setContentView(mRootView);
        this.setWidth(context.getResources().getDimensionPixelOffset(R.dimen.dimen_130dp));
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);

        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources()));
        update();

        getContentView().setFocusableInTouchMode(true);
        getContentView().setFocusable(true);
        setAnimationStyle(R.style.AppTheme);
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        System.out.println("onMenuItemClick");

        if (listener != null) {
            listener.onMenuItemClick(v);
        }
    }
}
