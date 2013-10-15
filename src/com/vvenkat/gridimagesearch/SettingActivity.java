package com.vvenkat.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.outofjungle.gridimagesearch.R;

public class SettingActivity extends Activity {

	Spinner spSize;
	Spinner spColor;
	Spinner spType;
	EditText etSite;
	Button btnSave;
	
	String size;
	String color;
	String type;
	String site;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setupViews();
		
		size = getIntent().getExtras().getString("size");
		color = getIntent().getExtras().getString("color");
		type = getIntent().getExtras().getString("type");
		site = getIntent().getExtras().getString("site");

		spSize.setSelection(getIndex(spSize, size));
		spColor.setSelection(getIndex(spColor, color));
		spType.setSelection(getIndex(spType, type));
		etSite.setText(site);
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				size = String.valueOf(spSize.getSelectedItem());
				color = String.valueOf(spColor.getSelectedItem());
				type = String.valueOf(spType.getSelectedItem());
				site = etSite.getText().toString();

				size = (size == "none") ? "" : size;
				color = (color == "none") ? "" : color;
				type = (type == "none") ? "" : type;
				
				Intent data = new Intent();
				data.putExtra("size", size);
				data.putExtra("color", color);
				data.putExtra("type", type);
				data.putExtra("site", site);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	
	public void setupViews(){
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		etSite = (EditText) findViewById(R.id.etSite);
		btnSave = (Button) findViewById(R.id.btnSave);
	}
	
	private int getIndex(Spinner spinner, String value) {
		int index = 0;
		for(int i = 0; i < spinner.getCount(); i++ ) {
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value))
				index = i;
		}
		return index;
	}
}
