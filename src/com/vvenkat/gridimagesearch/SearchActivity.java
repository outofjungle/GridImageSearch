package com.vvenkat.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.outofjungle.gridimagesearch.R;

public class SearchActivity extends Activity {
	
	private static final int SETTINGS = 0;
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	String size = "";
	String color = "";
	String type = "";
	String site = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingActivity.class);
			i.putExtra("size", size);
			i.putExtra("color", color);
			i.putExtra("type", type);
			i.putExtra("site", site);
			startActivityForResult(i, SETTINGS);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == SETTINGS) {
			size = data.getExtras().getString("size");
			color = data.getExtras().getString("color");
			type = data.getExtras().getString("type");
			site = data.getExtras().getString("site");
		}
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
	
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(
				"https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"
						+ "start=" + 0 + "&v=1.0&q=" + Uri.encode(query)
						+ "&imgsz=" + Uri.encode(size)
						+ "&imgcolor=" + Uri.encode(color)
						+ "&imgtype=" + Uri.encode(type)
						+ "&as_sitesearch=" + Uri.encode(site),
				new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageResults.toString());
						} catch(JSONException e) {
							e.printStackTrace();
						}
					}
				}
		);
	}
}
