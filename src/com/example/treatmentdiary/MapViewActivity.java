package com.example.treatmentdiary;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
 
public class MapViewActivity extends Activity {
 
	private WebView webView;
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("https://www.google.no/maps/search/Pharmacy/@59.9192167,10.7350791,14z?hl=no/");
 
	}
 
}