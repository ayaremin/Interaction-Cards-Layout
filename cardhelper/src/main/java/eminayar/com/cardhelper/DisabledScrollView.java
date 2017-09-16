package eminayar.com.cardhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class DisabledScrollView extends ObservableScrollView {

    private boolean mIsDisable = false;

    public DisabledScrollView(Context context) {
        super(context);
    }

    public DisabledScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisabledScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

   // if status is true, disable the ScrollView
    public void setDisableStatus(boolean status) {
        mIsDisable = status;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // no more tocuh events for this ScrollView
        if (mIsDisable) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        // although the ScrollView doesn't get touch events , its children will get them so intercept them.
        if (mIsDisable) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

}