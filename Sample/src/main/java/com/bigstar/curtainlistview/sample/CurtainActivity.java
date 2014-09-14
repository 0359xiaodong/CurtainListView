package com.bigstar.curtainlistview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bigstar.curtainlistview.CurtainListView;

import java.util.ArrayList;

/**
 * Created by BigStarK on 2014. 9. 11..
 */
public class CurtainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_curtain);

    FragmentCurtain curtain = new FragmentCurtain();
    FragmentHandle handle = new FragmentHandle();

    CurtainListView curtainListView = (CurtainListView) findViewById(R.id.curtain_listview);
    curtainListView.setFragmentManager(getFragmentManager());
    curtainListView.setCurtainFragment(curtain);
    curtainListView.setHandleFragment(handle);
    curtainListView.initialize();

    curtainListView.setAdapter(new ListAdapter());
  }

  private class ListAdapter extends BaseAdapter {

    private ArrayList<String> datas;

    private ListAdapter() {
      datas = new ArrayList<String>();
      for(int i = 0; i < 100; i++) {
        datas.add("댓글 " + (i + 1) + "번째ㅋㅋㅋ");
      }
    }

    @Override
    public int getCount() {
      return datas.size();
    }

    @Override
    public Object getItem(int position) {
      return position;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if(convertView == null) {
        convertView = getLayoutInflater().inflate(R.layout.row, null);
      }

      TextView tvTest = (TextView) convertView.findViewById(R.id.tv_test);
      tvTest.setText("" + datas.get(position));
      return convertView;
    }
  }
}
