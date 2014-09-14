package com.bigstar.curtainlistview.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BigStarK on 2014. 9. 11..
 */
public class FragmentCurtain extends Fragment {
  public FragmentCurtain() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View curtain = inflater.inflate(R.layout.fragment_curtain, container, false);
    return curtain;
  }
}
