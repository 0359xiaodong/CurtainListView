package com.bigstar.curtainlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

/**
 * Created by BigStarK on 2014. 9. 7..
 */
public class SupportCurtainListView extends FrameLayout {
    private final int DEFAULT_CURTAIN_FRAGMENT_HEIGHT = 300;
    private final int DEFAULT_HANDLE_FRAGMENT_HEIGHT = 300;

    private BaseCurtainListView baseCurtainListView;

    private FragmentManager fm;
    private Fragment curtainFragment;
    private Fragment handleFragment;
    private int curtainFragmentHeight;
    private int handleFragmentHeight;

    public SupportCurtainListView(Context context) {
      this(context, null);
    }

    public SupportCurtainListView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
    }

    public SupportCurtainListView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      initAttrs(attrs);
      inflate(context, R.layout.curtain_base, this);
      baseCurtainListView = (BaseCurtainListView) findViewById(R.id.base_curtain_listview);
    }

    private void initAttrs(AttributeSet attrs) {
      TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CurtainListView);
      curtainFragmentHeight = ta.getDimensionPixelSize(R.styleable.CurtainListView_curtain_fragment_height, DEFAULT_CURTAIN_FRAGMENT_HEIGHT);
      handleFragmentHeight = ta.getDimensionPixelSize(R.styleable.CurtainListView_handle_fragment_height, DEFAULT_HANDLE_FRAGMENT_HEIGHT);
      ta.recycle();
    }

    /**
     * Set android.app.FragmentManager
     * @param fm
     */
    public void setFragmentManager(FragmentManager fm) {
      this.fm = fm;
    }

    /**
     * Set CurtainView as Fragment
     *
     * @param curtainFragment
     */
    public void setCurtainFragment(Fragment curtainFragment) {
      this.curtainFragment = curtainFragment;
    }

    /**
     * Set CurtainFragmentHeight
     *
     * @param curtainFragmentHeight
     */
    public void setCurtainFragmentHeight(int curtainFragmentHeight) {
      this.curtainFragmentHeight = curtainFragmentHeight;
    }

    /**
     * Set HandleView as Fragment
     *
     * @param handleFragment
     */
    public void setHandleFragment(Fragment handleFragment) {
      this.handleFragment = handleFragment;
    }

    /**
     * Set HandleFragmentHeight
     *
     * @param handleFragmentHeight
     */
    public void setHandleFragmentHeight(int handleFragmentHeight) {
      this.handleFragmentHeight = handleFragmentHeight;
    }

    /**
     * Attatch Fragments, set Height after setFragment, setFragmentManager
     */
    public void initialize() {
      if(fm == null) {
        throw new NullPointerException("FragmentManager must not be null");
      }

      if(curtainFragment == null) {
        throw new NullPointerException("CurtainFragment must not be null");
      }

      if(handleFragment == null) {
        throw new NullPointerException("HandleFragment must not be null");
      }

      FragmentTransaction ft = fm.beginTransaction();
      ft.replace(R.id.curtain_view, curtainFragment);
      ft.replace(R.id.handle_view, handleFragment);
      ft.commit();

      baseCurtainListView.setCurtainViewHeight(curtainFragmentHeight);
      baseCurtainListView.setHandleViewHeight(handleFragmentHeight);
    }

    public void addHeaderView(View header) {
      baseCurtainListView.addHeaderView(header);
    }

    public void addFooterView(View footer) {
      baseCurtainListView.addFooterView(footer);
    }

    public void setAdapter(BaseAdapter adapter) {
      baseCurtainListView.setAdapter(adapter);
    }

    /**
     * Curtain up by scroll
     */
    public void minimize() {
      baseCurtainListView.minimize();
    }

    /**
     * Curtain down by scroll
     */
    public void maximize() {
      baseCurtainListView.maximize();
    }

    /**
     * Curtain up by handle
     */
    public void forceMinimize() {
      baseCurtainListView.forceMinimize();
    }

    /**
     * Curtain up by handle
     */
    public void forceMaximize() {
      baseCurtainListView.forceMaximize();
    }

    /**
     * Lock Curtain Up, Down
     */
    public void lockCurtain() {
      baseCurtainListView.lockCurtain();
    }

    /**
     * Unlock Curtain Up, Down
     */
    public void unlockCurtain() {
      baseCurtainListView.unlockCurtain();
    }

    public boolean isMaximized() {
      return baseCurtainListView.isMaximized();
    }

    public boolean isForceMaximized() {
      return baseCurtainListView.isForceMaximized();
    }

    public boolean isScrolling() {
      return baseCurtainListView.isScrolling();
    }

    public boolean isLocked() {
      return baseCurtainListView.isLocked();
    }
}
