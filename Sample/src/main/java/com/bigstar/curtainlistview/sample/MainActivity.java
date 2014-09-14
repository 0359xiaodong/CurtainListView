package com.bigstar.curtainlistview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bigstar.curtainlistview.BaseCurtainListView;

import java.util.ArrayList;


public class MainActivity extends Activity {

  private BaseCurtainListView curtainListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.v("TAG", "onCreate");
    Log.v("TAG", "onCreate");
    Log.v("TAG", "onCreate");
    Log.v("TAG", "onCreate");

    curtainListView = (BaseCurtainListView) findViewById(R.id.clv_main);
    curtainListView.setCurtainViewHeight(500);
    curtainListView.setHandleViewHeight(100);
    curtainListView.setAdapter(new ListAdapter());

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        curtainListView.lockCurtain();
      }
    }, 5000);
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
