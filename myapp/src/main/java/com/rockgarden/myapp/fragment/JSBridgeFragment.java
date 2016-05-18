package com.rockgarden.myapp.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rockgarden.WebViewJavaScriptBridge.BridgeHandler;
import com.rockgarden.WebViewJavaScriptBridge.CallBackFunction;
import com.rockgarden.WebViewJavaScriptBridge.DefaultHandler;
import com.rockgarden.WebViewJavaScriptBridge.JSWebView;
import com.rockgarden.myapp.R;

import java.io.IOException;
import java.io.StringReader;

import static com.litesuits.android.Log.i;

/**
 * Created by rockgarden on 16/5/18.
 */
public class JSBridgeFragment extends Fragment{
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    private final String TAG = "JSBridgeFragment";
    JSWebView webView;

    Button button;

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    public JSBridgeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view_js_bridge, container, false);

        // Get reference of WebView from layout/activity_main.xml
        webView = (JSWebView) rootView.findViewById(R.id.JSWebView);
        button = (Button) rootView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.equals(v)) {
                    webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

                        @Override
                        public void onCallBack(String data) {
                            // TODO Auto-generated method stub
                            i(TAG, "reponse data from js " + data);
                        }

                    });
                }
            }
        });
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
                com.litesuits.android.Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
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



        setUpWebViewDefaults(webView);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            webView.restoreState(savedInstanceState);
        }

        // Prepare the WebView and get the appropriate URL
        String url = prepareWebView(webView.getUrl());

        // Load the local index.html file
        if(webView.getUrl() == null) {
            webView.loadUrl(url);
        }

        return rootView;
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    /**
     * Convenience method to set some generic defaults for a
     * given WebView
     *
     * @param webView
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * This is method where specific logic for this application is going to live
     * @return url to load
     */
    private String prepareWebView(String currentUrl) {
        String hash = "";
        int bgColor;

        if(currentUrl != null) {
            String[] hashSplit = currentUrl.split("#");
            if(hashSplit.length == 2) {
                hash = hashSplit[1];
            }
        } else {
            Intent intent = getActivity().getIntent();
            if(intent != null && intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)) {
                hash = "notification-launch";
            }
        }

        if(hash.equals("notification-launch")) {
            bgColor = Color.parseColor("#1abc9c");
        } else if(hash.equals("notification-shown")) {
            bgColor = Color.parseColor("#3498db");
        } else if(hash.equals("secret")) {
            bgColor = Color.parseColor("#34495e");
        } else {
            bgColor = Color.parseColor("#f1c40f");
        }

        preventBGColorFlicker(bgColor);

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        webView.setWebViewClient(new WebViewClient());

        return "file:///android_asset/www/index.html#"+hash;
    }

    /**
     * This is a little bit of trickery to make the background color of the UI
     * the same as the anticipated UI background color of the web-app.
     *
     * @param bgColor
     */
    private void preventBGColorFlicker(int bgColor) {
        ((ViewGroup) getActivity().findViewById(R.id.activity_main_container)).setBackgroundColor(bgColor);
        webView.setBackgroundColor(bgColor);
    }

    /**
     * This method is designed to hide how Javascript is injected into
     * the WebView.
     *
     * In KitKat the new evaluateJavascript method has the ability to
     * give you access to any return values via the ValueCallback object.
     *
     * The String passed into onReceiveValue() is a JSON string, so if you
     * execute a javascript method which return a javascript object, you can
     * parse it as valid JSON. If the method returns a primitive value, it
     * will be a valid JSON object, but you should use the setLenient method
     * to true and then you can use peek() to test what kind of object it is,
     *
     * @param javascript
     */
    public void loadJavascript(String javascript) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            webView.evaluateJavascript(javascript, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            if(reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if(msg != null) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            });
        } else {
            /**
             * For pre-KitKat+ you should use loadUrl("javascript:<JS Code Here>");
             * To then call back to Java you would need to use addJavascriptInterface()
             * and have your JS call the interface
             **/
            webView.loadUrl("javascript:"+javascript);
        }
    }

    public boolean goBack() {
        if(!webView.canGoBack()) {
            return false;
        }

        webView.goBack();
        return true;
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
