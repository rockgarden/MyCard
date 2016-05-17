package com.rockgarden.WebViewJavaScriptBridge;

public interface WebViewJavascriptBridge {
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
}
