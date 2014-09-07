package com.bigstar.curtainlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class BaseCurtainListView extends RelativeLayout {

  public enum SCROLL_STATE {
    SCROLL_TO_TOP, SCROLL_TO_BOTTOM, SCROLL_SLOWLY
  }

  private final int SCROLL_MIN_VELOCITY = 30;

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

  private int distanceHandle;
  private int dyHandle;
  private int distanceScroll;
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
    initAttrs(attrs);
  }

  private void initAttrs(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CurtainListView);
    curtainViewId = ta.getResourceId(R.styleable.CurtainListView_curtain_view_id, R.id.curtain_view);
    handleViewId = ta.getResourceId(R.styleable.CurtainListView_handle_view_id, R.id.handle_view);
    ta.recycle();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    curtainView = findViewById(curtainViewId);
    handleView = findViewById(handleViewId);
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
    if(newScrollOffset > oldScrollOffset) {
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
  }

  /**
   * Curtain down by scroll
   */
  public void maximize() {
    isMaximized = true;
    isForceMaximized = false;
  }

  /**
   * Curtain up by handle
   */
  public void forceMinimize() {
    isMaximized = false;
    isForceMaximized = false;
    setScrolling();
  }

  /**
   * Curtain up by handle
   */
  public void forceMaximize() {
    isMaximized = true;
    isForceMaximized = true;
    setScrolling();
  }

  /**
   * Lock Curtain Up, Down
   */
  public void lockCurtain() {
    isLocked = true;
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
   * set CurtainView
   */
  public void setCurtainView(View curtainView) {
    //TODO update curtainView
  }

  /**
   * set CurtainViewHeight
   */
  public void setCurtainViewHeight(int curtainViewHeight) {

  }

  /**
   * set HandleView
   */
  public void setHandleView(View handleView) {
    //TODO update handleView
  }

  /**
   * set HandleViewHeight
   */
  public void setHandleViewHeight(int handleViewHeight) {

  }

  public void addHeaderView(View header) {
    if (header == null) {

    }
  }

  public void addFooterView(View footer) {
    if (footer == null) {

    }
  }

  public void setAdapter(BaseAdapter adapter) {
    listView.setAdapter(adapter);
  }

  private View.OnTouchListener handleTouchListener = new View.OnTouchListener() {

    private float previousRawY = 0f;
    private float distance = 0f;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      if(isForceMaximized || isLocked) {
        return false;
      }

      if(event.getAction() == MotionEvent.ACTION_DOWN) {
        previousRawY = event.getRawY();
      }

      if(event.getAction() == MotionEvent.ACTION_MOVE) {
        distance = event.getRawY() - previousRawY;
      }

      if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {

      }

      return true;
    }
  };

  private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
  };



}
