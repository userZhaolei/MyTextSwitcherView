package com.zl.mytextswitcherview;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Zhaolei
 * 时间:2018/3/15
 */

public class TextSwitchView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private int index = -1;
    private Context context;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next(); //取得下标值
                    Toast.makeText(context, "resources.size()" + (resources.size() - 1) + "  index" + index, Toast.LENGTH_SHORT).show();
                    if (resources.size() == index) {
                        timer.cancel();
                        if (onVisibilityCallBack != null) {
                            onVisibilityCallBack.viewHide();
                        }
                    } else {
                        updateText();  //更新TextSwitcherd显示内容;
                    }
                    break;
            }
        }
    };
    private Timer timer; //
    private List<String> resources;
    private OnVisibilityCallBack onVisibilityCallBack;

    public TextSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (timer == null)
            timer = new Timer();
        this.setFactory(this);  //设置动画
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_animation));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_animation));
    }

    /**
     * 提供对外设置集合的接口
     *
     * @param res
     */
    public void setResources(List<String> res) {
        this.resources = res;
    }

    public void setTextStillTime(long time) {
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.scheduleAtFixedRate(new MyTask(), 1, time);//每3秒更新
        }
    }

    /**
     * 定时器 定时发送消息
     */
    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }

    /**
     * 不断显示新的消息
     *
     * @return
     */
    private int next() {
        int flag = index + 1;
        /*if (flag > resources.size() - 1) {
            flag = flag - resources.size();
        }*/
        return flag;
    }


    private void updateText() {
        this.setText(resources.get(index));
    }

    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);
        return tv;
    }

    public void setVisibilityCallBack(OnVisibilityCallBack onVisibilityCallBack) {
        this.onVisibilityCallBack = onVisibilityCallBack;
    }

    /**
     * 接口回调设置隐藏时间
     */
    public interface OnVisibilityCallBack {
        void viewHide();
    }
}
