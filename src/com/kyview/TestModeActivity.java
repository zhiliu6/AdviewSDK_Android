package com.kyview;

import java.util.List;

import com.kyview.adapters.AdViewAdapter;
import com.kyview.obj.Ration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TestModeActivity extends Activity {
	private static List<Ration>rationList;
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testmain);

		TestAsyn testAsyn=new TestAsyn();
		testAsyn.execute();
	}
	
	class TestAsyn extends AsyncTask<String, String,String>{

		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog=new ProgressDialog(TestModeActivity.this);
			
		}
		@Override
		protected String doInBackground(String... params) {
			
			while(null==Invoker.adViewLayout||null==Invoker.adViewLayout.adViewManager.getRationList()){
			}
			rationList=Invoker.adViewLayout.adViewManager.getRationList();
			publishProgress();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			listView=(ListView) findViewById(R.id.test_lv);
			listView.setAdapter(new BaseAdapter() {	
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					TextView textView=new TextView(TestModeActivity.this);
					textView.setTextSize(28);
					textView.setText(rationList.get(position).name);
					return textView;
				}
				
				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return position;
				}
				
				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return rationList.get(position);
				}
				
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return rationList.size();
				}
			});
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Invoker.adViewLayout.nextRation=(Ration)arg0.getItemAtPosition(arg2);
					AdViewAdapter.handleOne(Invoker.adViewLayout, (Ration)arg0.getItemAtPosition(arg2));
					
					Invoker.adViewLayout.extra.cycleTime=9000000;
					finish();
				}
				
			});
		}
		
	}
}
