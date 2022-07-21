package com.jwpeng.mymic.Uilt;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopWindowFactory {

    private Context mContext;
    private PopupWindow mPop;
    private Button cancelBut;

    public PopWindowFactory(Context mContext, View view){
        this(mContext,view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * @param mContext 上下文
     * @param view 布局文件
     * @param width 宽
     * @param heigth 高
     */
    public PopWindowFactory(Context mContext, View view, int width, int heigth) {
        init(mContext,view,width,heigth);
    }

    private void init(Context mContext, View view, int width, int heigth) {
        this.mContext =mContext;

        //必须设置
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);

        // PopupWindow(布局，宽度，高度)
        mPop = new PopupWindow(view,width,heigth,true);
        mPop.setFocusable(true);

        // 按返回键消失
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        // 点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPop != null && mPop.isShowing()){
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public PopupWindow getmPopWindow() {
        return mPop;
    }

    /**
     * 以触发弹出窗的view为基准，显示在view的正下方，弹出的popview在左上角，正对view的左下角
     * @param parent view
     * @param gravity 在view的什么位置 Gravity.CENTER、Gravity.TOP
     * @param x 与控件的x坐标距离
     * @param y 与控件的y坐标距离
     */
    public void showAtLocation(View parent,int gravity,int x,int y){
        if (mPop.isShowing()){
            return;
        }
        mPop.showAtLocation(parent,gravity,x,y);
    }

    public void showAsDropDown(View view){
        showAsDropDown(view,0,0);
    }

    /**
     * 以触发弹出窗的view为基准，显示在view的正下方，弹出的popview在左上角，正对view的左下角
     * @param view
     * @param xoff 与view的x坐标距离
     * @param yoff 与view的y坐标距离
     */
    private void showAsDropDown(View view, int xoff, int yoff) {
        if (mPop.isShowing()){
            return;
        }
        mPop.showAsDropDown(view,xoff,yoff);
    }

    /**
     * 隐藏PopWindow
     */
    public void dismiss(){
        if (mPop.isShowing()){
            mPop.dismiss();
        }
    }
}
