package com.bigstar.curtainlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class BaseCurtainListView extends RelativeLayout {
  private final String TAG = "BaseCurtainListView";

  public enum SCROLL_STATE {
    SCROLL_TO_TOP, SCROLL_TO_BOTTOM, SCROLL_SLOWLY
  }

  private final int SCROLL_MIN_VELOCITY = 15;

  private int TRANSFER_DURATION = 200;

  private View curtainView;
  private View handleView;
  private int curtainViewId;
  private int handleViewId;

  private int curtainHeight = 0;
  private int handleHeight = 0;

  private ListView listView = null;
  private View curtainHeaderView;
  private View handleHeaderView;

  private float distanceHandle;
  private int distanceScroll;
  private float dyHandle;
  private int dyScroll;

  private boolean isLocked = false;
  private boolean isForceMaximized = false;
  private boolean isMaximized = false;
  private boolean isScrolling = false;

  private SCROLL_STATE scrollState = SCROLL_STATE.SCROLL_SLOWLY;

  public BaseCurtainListView(Context context) {
    this(context, null);
  }

  public BaseCurtainListView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaseCurtainListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    listView = new ListView(context);
    listView.setLayoutParams(
      new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    listView.setOnTouchListener(listTouchListener);
    listView.setOnScrollListener(scrollListener);
    curtainHeaderView = new View(context);
    handleHeaderView = new View(context);
    listView.addHeaderView(curtainHeaderView);
    listView.addHeaderView(handleHeaderView);
    initAttrs(attrs);
  }

  private void initAttrs(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BaseCurtainListView);
    curtainViewId = ta.getResourceId(R.styleable.BaseCurtainListView_curtain_view_id, R.id.curtain_view);
    handleViewId = ta.getResourceId(R.styleable.BaseCurtainListView_handle_view_id, R.id.handle_view);
    curtainHeight = ta.getDimensionPixelSize(R.styleable.BaseCurtainListView_curtain_view_height, 0);
    handleHeight = ta.getDimensionPixelSize(R.styleable.BaseCurtainListView_handle_view_height, 0);
    ta.recycle();
  }

  @Override
  protected void onFinishInflate() {
    Log.v(TAG, "onFinishInflate");
    super.onFinishInflate();
    curtainView = findViewById(curtainViewId);
    handleView = findViewById(handleViewId);

    if(curtainHeight != 0) {
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) curtainView.getLayoutParams();
      params.height = curtainHeight;
      curtainView.setLayoutParams(params);
    }

    if(handleHeight != 0) {
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) handleView.getLayoutParams();
      params.height = handleHeight;
      handleView.setLayoutParams(params);
    }

    handleView.setOnTouchListener(handleTouchListener);
    addView(listView, 0);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    Log.v(TAG, "onMeasure");
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if(curtainHeaderView.getHeight() != curtainView.getHeight()) {
      AbsListView.LayoutParams params = new AbsListView.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, curtainView.getHeight());
      curtainHeaderView.setLayoutParams(params);
    }

    if(handleHeaderView.getHeight() != handleView.getHeight()) {
      AbsListView.LayoutParams params = new AbsListView.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, handleView.getHeight());
      handleHeaderView.setLayoutParams(params);
    }

    curtainView.layout(0, 0, right, curtainView.getHeight());
    handleView.layout(0, curtainView.getHeight(), right, curtainView.getHeight() + handleView.getHeight());
  }

  private void setScrolling() {
    isScrolling = true;
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        isScrolling = false;
      }
    }, TRANSFER_DURATION);
  }

  private void scrollStateChanged(int newScrollOffset, int oldScrollOffset) {
    if (newScrollOffset > oldScrollOffset) {
      scrollState = SCROLL_STATE.SCROLL_TO_BOTTOM;
    } else {
      scrollState = SCROLL_STATE.SCROLL_TO_TOP;
    }
  }

  /**
   * Curtain up by scroll
   */
  public void minimize() {
    isMaximized = false;
    isForceMaximized = false;
    listView.post(new Runnable() {
      @Override
      public void run() {
        listView.clearFocus();
        listView.requestFocusFromTouch();
        listView.smoothScrollToPositionFromTop(0, -curtainHeaderView.getHeight());
        listView.requestFocus();
      }
    });
  }

  /**
   * Curtain down by scroll
   */
  public void maximize() {
    isMaximized = true;
    isForceMaximized = false;
    listView.post(new Runnable() {
      @Override
      public void run() {
        listView.clearFocus();
        listView.requestFocusFromTouch();
        listView.smoothScrollToPositionFromTop(0, 0);
        listView.requestFocus();
      }
    });
  }

  /**
   * Curtain up by handle
   */
  public void forceMinimize() {
    isMaximized = false;
    isForceMaximized = false;
    ViewPropertyAnimator.animate(curtainView).setDuration(TRANSFER_DURATION).translationY(-curtainHeaderView.getHeight());
    ViewPropertyAnimator.animate(handleView).setDuration(TRANSFER_DURATION).translationY(-curtainHeaderView.getHeight());
    setScrolling();
  }

  /**
   * Curtain up by handle
   */
  public void forceMaximize() {
    isMaximized = true;
    isForceMaximized = true;
    ViewPropertyAnimator.animate(curtainView).setDuration(TRANSFER_DURATION).translationY(0);
    ViewPropertyAnimator.animate(handleView).setDuration(TRANSFER_DURATION).translationY(0);
    setScrolling();
  }

  /**
   * Lock Curtain Up, Down
   */
  public void lockCurtain() {
    isLocked = true;
    if(isForceMaximized) {
      actionUpInMaximized();
    } else {
      actionUpInMinimized();
    }

    distanceHandle = 0;
    dyHandle = 0;
  }

  /**
   * Unlock Curtain Up, Down
   */
  public void unlockCurtain() {
    isLocked = false;
  }

  public boolean isMaximized() {
    return isMaximized;
  }

  public boolean isForceMaximized() {
    return isForceMaximized;
  }

  public boolean isScrolling() {
    return isScrolling;
  }

  public boolean isLocked() {
    return isLocked;
  }

  /**
   * set CurtainViewHeight
   */
  public void setCurtainViewHeight(int curtainViewHeight) {
    curtainHeight = curtainViewHeight;
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) curtainView.getLayoutParams();
    params.height = curtainHeight;
    curtainView.setLayoutParams(params);
  }

  /**
   * set HandleViewHeight
   */
  public void setHandleViewHeight(int handleViewHeight) {
    handleHeight = handleViewHeight;
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) handleView.getLayoutParams();
    params.height = handleHeight;
    handleView.setLayoutParams(params);
  }

  public void addHeaderView(View header) {
    if (header == null) {
      throw new NullPointerException("header must not be null");
    }

    if(listView.getAdapter() != null) {
      throw new IllegalStateException("addHeaderView must use before setAdapter");
    }

    listView.addHeaderView(header);
  }

  public void addFooterView(View footer) {
    if (footer == null) {
      throw new NullPointerException("footer must not be null");
    }

    if(listView.getAdapter() != null) {
      throw new IllegalStateException("addFooterView must use before setAdapter");
    }

    listView.addFooterView(footer);
  }

  public void setAdapter(BaseAdapter adapter) {
    listView.setAdapter(adapter);
  }

  private void setCurtainTranslationY(float translationY) {
    if(curtainView == null) {
      return;
    }

    curtainView.setTranslationY(translationY);
  }

  private void setHandleTranslationY(float translationY) {
    if(handleView == null) {
      return;
    }

    handleView.setTranslationY(translationY);
  }

  private void setBothTranslationY(float translationY) {
    setCurtainTranslationY(translationY);
    setHandleTranslationY(translationY);
  }

  private View.OnTouchListener handleTouchListener = new View.OnTouchListener() {

    private float previousRawY = 0f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      if ((isMaximized && !isForceMaximized) || isLocked || isScrolling) {
        return false;
      }

      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        previousRawY = event.getRawY();
        distanceHandle = 0f;
      }

      if (event.getAction() == MotionEvent.ACTION_MOVE) {
        float newDistance = event.getRawY() - previousRawY;
        dyHandle = newDistance - distanceHandle;
        distanceHandle = newDistance;

        return isForceMaximized ? moveHandleInMaximized() : moveHandleInMinimized();
      }

      if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
        Log.v(TAG, "dyHandle : " + dyHandle);
        return isForceMaximized ? actionUpInMaximized() : actionUpInMinimized();
      }

      return true;
    }

    private boolean moveHandleInMaximized() {
      if(distanceHandle > 0) {
        return true;
      }

      int curtainHeaderHeight = curtainHeaderView.getHeight();
      if(Math.abs(distanceHandle) > curtainHeaderHeight) {
        return true;
      }

      float handleBottom = curtainHeaderView.getHeight() + handleHeaderView.getHeight() + distanceHandle;
      float handleHeaderBottom = handleHeaderView.getBottom();

      if(handleBottom < handleHeaderBottom) {
        minimize();
        isForceMaximized = false;
        return false;
      }

      setBothTranslationY(distanceHandle);
      return true;
    }

    private boolean moveHandleInMinimized() {
      if(distanceHandle < 0) {
        return true;
      }

      int curtainHeaderHeight = curtainHeaderView.getHeight();
      if(distanceHandle - curtainHeaderHeight > 0) {
        return true;
      }

      setBothTranslationY(distanceHandle - curtainHeaderHeight);
      return true;
    }
  };

  public boolean actionUpInMaximized() {
    if(distanceHandle > 0) {
      forceMaximize();
      return true;
    }

    if(dyHandle < -SCROLL_MIN_VELOCITY) {
      if(listView.getFirstVisiblePosition() == 0 && handleHeaderView.getBottom() > handleHeaderView.getHeight()) {
        minimize();
      }

      forceMinimize();
      dyHandle = 0;
      return true;
    }

    distanceHandle = Math.abs(distanceHandle);
    int curtainHeaderHeight = curtainHeaderView.getHeight();
    if(distanceHandle < curtainHeaderHeight / 2) {
      forceMaximize();
      return true;
    }

    if(listView.getFirstVisiblePosition() == 0 && handleHeaderView.getBottom() > handleHeaderView.getHeight()) {
      minimize();
    }

    forceMinimize();
    return true;
  }

  public boolean actionUpInMinimized() {
    if(distanceHandle  < 0) {
      forceMinimize();
      return true;
    }

    if(dyHandle > SCROLL_MIN_VELOCITY) {
      forceMaximize();
      dyHandle = 0;
      return true;
    }

    distanceHandle = Math.abs(distanceHandle);
    int curtainHeaderHeight = curtainHeaderView.getHeight();
    if(distanceHandle < curtainHeaderHeight / 2) {
      forceMinimize();
      return true;
    }

    forceMaximize();
    return true;
  }

  private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

    private int previousCurtainHeaderTop = 0;
    private int previousTopOffset = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
      if(curtainView == null || curtainHeaderView == null) {
        Log.v(TAG, "curtainView or curtainHeaderView is null");
        return;
      }
      View topChild = view.getChildAt(0);

      int topOffset = 0;
      if (topChild == null) {
        topOffset = 0;
      } else {
        topOffset = -topChild.getTop() + view.getFirstVisiblePosition() * topChild.getHeight();
      }

      if (Math.abs(topOffset - previousTopOffset) >= SCROLL_MIN_VELOCITY * 2) {
        scrollStateChanged(topOffset, previousTopOffset);
      } else {
        scrollState = SCROLL_STATE.SCROLL_SLOWLY;
      }

      previousTopOffset = topOffset;

      if((isMaximized && isLocked) || isScrolling) {
        return;
      }

      int curtainHeaderTop = Math.abs(curtainHeaderView.getTop());
      dyScroll = curtainHeaderTop - previousCurtainHeaderTop;
      previousCurtainHeaderTop = curtainHeaderTop;

      if(isForceMaximized) {
        if(curtainHeaderTop == 0) {
          isForceMaximized = false;
          isMaximized = true;
          return;
        }

        if(firstVisibleItem <= 1) {
          return;
        }

        if(scrollState.equals(SCROLL_STATE.SCROLL_TO_BOTTOM)) {
          forceMinimize();
          return;
        }
        return;
      }

      if(firstVisibleItem == 0) {
        isForceMaximized = false;
        unlockCurtain();
        setBothTranslationY(- curtainHeaderTop);
        return;
      }

      isMaximized = false;
      int curtainHeaderHeight = curtainHeaderView.getHeight();
      setBothTranslationY(-curtainHeaderHeight);
    }
  };

  private View.OnTouchListener listTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      if(event.getAction() != MotionEvent.ACTION_UP) {
        return false;
      }

      if(isLocked || isScrolling || isForceMaximized) {
        return false;
      }

      if(listView.getFirstVisiblePosition() > 0) {
        return false;
      }

      if(dyScroll < -SCROLL_MIN_VELOCITY) {
        Log.v(TAG, "Maximize by velocity");
        maximize();
        return false;
      }

      if(dyScroll > SCROLL_MIN_VELOCITY) {
        Log.v(TAG, "Minimize by velocity");
        minimize();
        return false;
      }

      int curtainHeaderTop = Math.abs(curtainHeaderView.getTop());
      int curtainHeaderHeight = curtainHeaderView.getHeight();

      if(curtainHeaderTop < curtainHeaderHeight / 2) {
        Log.v(TAG, "Maximize");
        maximize();
      } else {
        Log.v(TAG, "Minimize");
        minimize();
      }

      return false;
    }
  };


}
