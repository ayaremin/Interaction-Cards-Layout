package eminayar.com.cardhelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import eminayar.com.cardhelper.models.CardItem;
import java.util.List;

/**
 * Created by EminAyar on 3.09.2017.
 */

public class HelperCardsLayout extends FrameLayout
    implements ObservableScrollViewCallbacks, View.OnClickListener {

  LayoutInflater mInflater;
  private View mFlexibleSpaceView;
  private String mToolbarTitle;
  private TextView mTitleView;
  private int mFlexibleSpaceHeight;
  private int mFlexibleSpaceWidth;
  private Toolbar mToolBarView;
  private View mFakeToolbar;
  private DisabledScrollView scrollView;
  private View mRootBackground;
  private View emptyView;
  private View mScrollStarter;
  private View mBackButton;
  private View mTopInterceptor;
  private LinearLayout mCardContainer;
  private CardClickListener cardClickListener;
  private CardClickLongListener cardClickLongListener;
  private int mOverlayColor;
  private Drawable mBackButtonImage;
  private View rootView;

  public HelperCardsLayout(Context context) {
    super(context);
    mInflater = LayoutInflater.from(context);
    init();
  }

  public HelperCardsLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HelperCardsLayout, 0, 0);
    try {
      mToolbarTitle = ta.getString(R.styleable.HelperCardsLayout_toolbar_title);
      mOverlayColor = ta.getColor(R.styleable.HelperCardsLayout_overlay_color, Color.BLACK);
      mBackButtonImage =
          ta.getDrawable(R.styleable.HelperCardsLayout_back_icon);
    } finally {
      ta.recycle();
    }

    mInflater = LayoutInflater.from(context);
    init();
  }

  public HelperCardsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HelperCardsLayout, 0, 0);
    try {
      mToolbarTitle = ta.getString(R.styleable.HelperCardsLayout_toolbar_title);
      mOverlayColor = ta.getColor(R.styleable.HelperCardsLayout_overlay_color, Color.BLACK);
      mBackButtonImage =
          ta.getDrawable(R.styleable.HelperCardsLayout_back_icon);
    } finally {
      ta.recycle();
    }

    mInflater = LayoutInflater.from(context);
    init();
  }

  public void setOnCardClickListener(CardClickListener listener) {
    this.cardClickListener = listener;
  }

  public void setOnCardLongClickListener(CardClickLongListener listener) {
    this.cardClickLongListener = listener;
  }

  private OnTouchListener touchListener = new OnTouchListener() {
    @Override public boolean onTouch(View v, MotionEvent event) {
      switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
          scrollView.setDisableStatus(true);
          break;
        case MotionEvent.ACTION_UP:
          scrollView.setDisableStatus(false);
          break;
      }
      return false;
    }
  };

  public void init() {
    View v = mInflater.inflate(R.layout.layout_helper_card, this, true);
    mFlexibleSpaceView = findViewById(R.id.flexible_space);
    mToolBarView = (Toolbar) findViewById(R.id.toolbar_helper);
    mTitleView = (TextView) findViewById(R.id.title);
    mFakeToolbar = findViewById(R.id.fake_toolbar);
    mCardContainer = (LinearLayout) findViewById(R.id.card_container);
    mRootBackground = findViewById(R.id.root_layout);
    emptyView = findViewById(R.id.empty_area);
    scrollView = (DisabledScrollView) findViewById(R.id.scroll);
    mBackButton = findViewById(R.id.back_button);
    mScrollStarter = findViewById(R.id.scroll_starter);
    mTopInterceptor = findViewById(R.id.top_interceptor);
    rootView = findViewById(R.id.root_view);

    mTitleView.setText(mToolbarTitle);
    mToolBarView.setTitle(null);
    // Set background color of overlay view
    mRootBackground.setBackgroundColor(mOverlayColor);
    mToolBarView.setBackgroundColor(mOverlayColor);

    //Set back button image resource
    ((ImageView)mBackButton).setImageDrawable(mBackButtonImage);

    Typeface boldFont =
        Typeface.createFromAsset(getContext().getAssets(), "fonts/GT_Walsheim_Bold.ttf");

    mTitleView.setTypeface(boldFont);

    mBackButton.setOnClickListener(this);

    mRootBackground.setOnTouchListener(touchListener);
    emptyView.setOnTouchListener(touchListener);
    mFakeToolbar.setOnTouchListener(touchListener);
    mToolBarView.setOnTouchListener(touchListener);
    mTopInterceptor.setOnTouchListener(touchListener);
    mFlexibleSpaceView.setOnTouchListener(touchListener);

    mScrollStarter.setOnTouchListener(new OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        scrollView.setDisableStatus(false);
        return false;
      }
    });

    scrollView.setScrollViewCallbacks(HelperCardsLayout.this);
    mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
    int flexibleSpaceAndToolbarHeight = mFlexibleSpaceHeight + getActionBarSize();

    mFlexibleSpaceWidth =
        getResources().getDimensionPixelSize(R.dimen.toolbar_margin_start) + mTitleView.getWidth();

    findViewById(R.id.body).setPadding(0, flexibleSpaceAndToolbarHeight, 0, 0);
    mFlexibleSpaceView.getLayoutParams().height = flexibleSpaceAndToolbarHeight;

    ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
      @Override public void run() {
        updateFlexibleSpaceText(scrollView.getCurrentScrollY());
        updateAlpha(scrollView.getCurrentScrollY());
      }
    });
  }

  @Override public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    updateFlexibleSpaceText(scrollY);
    updateAlpha(scrollY);
    updateCLickableArea(scrollY);
  }

  @Override public void onDownMotionEvent() {

  }

  @Override public void onUpOrCancelMotionEvent(
      ScrollState scrollState
  ) {

  }

  private void updateCLickableArea(int scrollY) {
    if (scrollY < 50) {
      rootView.setOnClickListener(null);
      rootView.setClickable(false);
      rootView.setFocusable(false);
    } else {
      rootView.setClickable(true);
      rootView.setFocusable(true);
      rootView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          scrollView.smoothScrollTo(0, 0);
        }
      });
    }
  }

  private void updateAlpha(final int scrollY) {
    if (scrollY < 50) {
      ViewHelper.setAlpha(mFakeToolbar, 0);
    } else {
      ViewHelper.setAlpha(mFakeToolbar, (float) scrollY / 200);
    }

    if (scrollY < 20) {
      ViewHelper.setAlpha(mRootBackground, 0);
      mToolBarView.setVisibility(View.GONE);
      mFakeToolbar.setVisibility(View.GONE);
      ViewHelper.setAlpha(mToolBarView, 0);
    } else {
      mToolBarView.setVisibility(View.VISIBLE);
      mFakeToolbar.setVisibility(View.VISIBLE);
      if ((float) scrollY / 150 >= 0.9) {
        ViewHelper.setAlpha(mRootBackground, 0.9f);
        ViewHelper.setAlpha(mToolBarView, 0.9f);
      } else {
        ViewHelper.setAlpha(mRootBackground, (float) scrollY / 150);
        ViewHelper.setAlpha(mToolBarView, (float) scrollY / 150);
      }
    }
  }

  private void updateFlexibleSpaceText(int scrollY) {
    int temp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
        getContext().getResources().getDisplayMetrics());
    int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getScreenHeight() - temp,
        getContext().getResources().getDisplayMetrics());
    scrollY = scrollY - top;
    ViewHelper.setTranslationY(mFlexibleSpaceView, -scrollY);
    int adjustedScrollY = (int) ScrollUtils.getFloat(scrollY, 0, mFlexibleSpaceHeight);
    float maxScale =
        (float) (mFlexibleSpaceHeight - mToolBarView.getHeight()) / mToolBarView.getHeight();
    float scale =
        maxScale * ((float) mFlexibleSpaceHeight - adjustedScrollY) / mFlexibleSpaceHeight;

    ViewHelper.setPivotX(mTitleView, 0);
    ViewHelper.setPivotY(mTitleView, 0);
    ViewHelper.setScaleX(mTitleView, 1 + scale);
    ViewHelper.setScaleY(mTitleView, 1 + scale);
    int maxTitleTranslationY =
        mToolBarView.getHeight() + mFlexibleSpaceHeight - (int) (mTitleView.getHeight() * (1
            + scale));
    int titleTranslationY =
        (int) (maxTitleTranslationY * ((float) mFlexibleSpaceHeight - adjustedScrollY)
            / mFlexibleSpaceHeight);

    int maxTitleTranslationX =
        mToolBarView.getHeight() + mFlexibleSpaceHeight - (int) (mTitleView.getHeight() * (1
            + scale));
    int titleTranslationX =
        (int) (maxTitleTranslationX * ((float) mFlexibleSpaceWidth - adjustedScrollY)
            / mFlexibleSpaceHeight);

    ViewHelper.setTranslationY(mTitleView, titleTranslationY);
    if (titleTranslationX > -50) {
      ViewHelper.setTranslationX(mTitleView, 50);
    } else {
      ViewHelper.setTranslationX(mTitleView, -titleTranslationX);
    }
  }

  protected int getActionBarSize() {
    TypedValue typedValue = new TypedValue();
    int[] textSizeAttr = new int[] { R.attr.actionBarSize };
    int indexOfAttrTextSize = 0;
    TypedArray a = getContext().obtainStyledAttributes(typedValue.data, textSizeAttr);
    int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
    a.recycle();
    return actionBarSize;
  }

  protected int getScreenHeight() {
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int height = size.y;
    return height;
  }

  public void setItems(List<CardItem> items) {
    LinearLayout.LayoutParams cardContainerParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    int temp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
        getContext().getResources().getDisplayMetrics());

    int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getScreenHeight() - temp,
        getContext().getResources().getDisplayMetrics());

    LinearLayout.LayoutParams emptyViewParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, top);

    int side = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
        getContext().getResources().getDisplayMetrics());
    emptyView.setLayoutParams(emptyViewParams);
    cardContainerParams.setMargins(side, 0, side, 0);
    mCardContainer.removeAllViews();
    mCardContainer.setLayoutParams(cardContainerParams);
    int index = 0;

    for (final CardItem card : items) {
      View child = mInflater.inflate(R.layout.card_view, null);

      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.WRAP_CONTENT);

      int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (4),
          getContext().getResources().getDisplayMetrics());

      params.setMargins(0, topMargin, 0, topMargin);

      Typeface regularFont =
          Typeface.createFromAsset(getContext().getAssets(), "fonts/GT_Walsheim_Regular.ttf");

      TextView title = (TextView) child.findViewById(R.id.title);
      TextView description = (TextView) child.findViewById(R.id.description);
      ImageView image = (ImageView) child.findViewById(R.id.image);
      title.setText(card.title);
      description.setText(card.description);

      title.setTypeface(regularFont);
      description.setTypeface(regularFont);

      if (card.image == CardItem.NO_IMAGE) {

        ViewGroup.MarginLayoutParams titleLayoutParams =
            (ViewGroup.MarginLayoutParams) title.getLayoutParams();

        ViewGroup.MarginLayoutParams descLayoutParams =
            (ViewGroup.MarginLayoutParams) description.getLayoutParams();

        int top_ = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (10),
            getContext().getResources().getDisplayMetrics());

        titleLayoutParams.setMargins(0, 0, 0, 0);
        descLayoutParams.setMargins(0, top_, 0, 0);

        title.setLayoutParams(titleLayoutParams);
        description.setLayoutParams(descLayoutParams);

        image.setVisibility(View.GONE);
      } else {
        image.setImageResource(card.image);
      }

      // Set margins and paddings to layout params
      child.setLayoutParams(params);

      // Add card to card container view
      mCardContainer.addView(child);

      final int tempIndex = index;

      //Set click listener
      child.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          if (cardClickListener != null) {
            cardClickListener.onCardClicked(tempIndex, card);
          }
        }
      });

      //Set long click listener
      child.setOnLongClickListener(new OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          if (cardClickLongListener != null) {
            cardClickLongListener.onCardLongClicked(tempIndex, card);
          }
          return true;
        }
      });
      child.setOnTouchListener(new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
          scrollView.setDisableStatus(false);
          return false;
        }
      });
      index++;
    }
  }

  public boolean canScrollToTop() {
    if (scrollView.getScrollY() != 0) {
      scrollView.smoothScrollTo(0, 0);
      return true;
    }
    return false;
  }

  @Override public void onClick(View v) {
    scrollView.smoothScrollTo(0, 0);
  }
}