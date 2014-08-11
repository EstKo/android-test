package com.estsoft.volleytest;

import java.util.ArrayList;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.estsoft.volleytest.rest.RestTest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private Button sendBtn;
	private EditText inputTextField;
	private ListView listView;
	private ArcodeItemListAdapter adapter;
	private RestTest restApi;
	private ArrayList<ArcodeModel> arcodeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sendBtn = (Button) findViewById(R.id.button1);
		inputTextField = (EditText) findViewById(R.id.editText1);
		listView = (ListView)findViewById(R.id.listView);
		arcodeList = new ArrayList<ArcodeModel>();
		adapter = new ArcodeItemListAdapter();
		listView.setAdapter(adapter);
		
		restApi = RestTest.getSharedApi(getApplicationContext());
		sendBtn.setOnClickListener(sendBtnClickListener);
	}
	
	OnClickListener sendBtnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String arname = inputTextField.getText().toString();
			
			if (arname.length() == 0) {
				return;
			}
			
			ErrorListener fail = new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Log.d("Error", arg0.getLocalizedMessage());
				}
			};
			Listener<ArrayList<ArcodeModel>> success = new Listener<ArrayList<ArcodeModel>>() {

				@Override
				public void onResponse(ArrayList<ArcodeModel> arg0) {
					Log.d("Success", "성공");
					arcodeList = arg0;
					adapter.notifyDataSetChanged();
				}
			};
			restApi.requestCode(arname, success, fail);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public class ArcodeItemListAdapter extends BaseAdapter {


		public ArcodeItemListAdapter() {
			super();
		}

		@Override
		public int getCount() {
			
			return arcodeList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arcodeList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) { 
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ArcodeModel item = arcodeList.get(position);
			convertView =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.arcode_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			textView.setText(item.sidoname + " : " + item.sigunname + " : " + item.arcode);

			return convertView;
		}

	}
}
