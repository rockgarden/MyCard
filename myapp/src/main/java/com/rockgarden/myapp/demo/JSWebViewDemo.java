package com.rockgarden.myapp.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.google.gson.Gson;
import com.rockgarden.WebViewJavaScriptBridge.BridgeHandler;
import com.rockgarden.WebViewJavaScriptBridge.CallBackFunction;
import com.rockgarden.WebViewJavaScriptBridge.DefaultHandler;
import com.rockgarden.WebViewJavaScriptBridge.JSWebView;
import com.rockgarden.myapp.R;

import static com.litesuits.android.Log.*;

public class JSWebViewDemo extends Activity implements OnClickListener {

	private final String TAG = "JSWebViewDemo";

	JSWebView webView;

	Button button;

	int RESULT_CODE = 0;

	ValueCallback<Uri> mUploadMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_js_web_view_demo);
		webView = (JSWebView) findViewById(R.id.webView);
		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(this);
		// Set a default handler in Java, so that js can send message to Java without assigned handlerName
		webView.setDefaultHandler(new DefaultHandler());

		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				pickFile();
			}
		});

		webView.loadUrl("file:///android_asset/js_demo.html");

		// Register a Java handler function(method) so that js can call it
		webView.registerHandler("submitFromWeb", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				i(TAG, "handler = submitFromWeb, data from web = " + data);
				function.onCallBack("submitFromWeb exe, response data 中文 from Java");
			}
		});

		User user = new User();
		Location location = new Location();
		location.address = "SDU";
		user.location = location;
		user.name = "大头鬼";

		// Java call this js handler function "functionInJs"
		webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
			@Override
			public void onCallBack(String data) {

			}
		});

		// 通过默认的handler Java can send message to js without assigned handlerName
		webView.send("hello");

	}

	public void pickFile() {
		Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
		chooserIntent.setType("image/*");
		startActivityForResult(chooserIntent, RESULT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage){
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (button.equals(v)) {
			webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// TODO Auto-generated method stub
					Log.i(TAG, "reponse data from js " + data);
				}

			});
		}

	}

	static class Location {
		String address;
	}

	static class User {
		String name;
		Location location;
		String testStr;
	}

}
