package com.bigstar.curtainlistview.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BigStarK on 2014. 9. 11..
 */
public class FragmentHandle extends Fragment {
  public FragmentHandle() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View handle = inflater.inflate(R.layout.fragment_handle, container, false);
    return handle;
  }
}
