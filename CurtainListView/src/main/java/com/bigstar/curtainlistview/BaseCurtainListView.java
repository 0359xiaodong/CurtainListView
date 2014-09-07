package com.bigstar.curtainlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


public class BaseCurtainListView extends FrameLayout {

  public enum SCROLL_STATE {
    SCROLL_TO_TOP, SCROLL_TO_BOTTOM, SCROLL_SLOWLY
  }

  private View curtainView;
  private View handleView;

  private ListView listView = null;
  private View curtainHeaderView;
  private View handleHeaderView;

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

  }

  public void setCurtainView(View curtainView) {

  }

  public void setHandleView(View handleView) {

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

  public void addHeaderView(View header) {
    if(header == null) {

    }
  }

  public void addFooterView(View footer) {
    if(footer == null) {

    }
  }

  public void setAdapter(BaseAdapter adapter) {
    listView.setAdapter(adapter);
  }


}
