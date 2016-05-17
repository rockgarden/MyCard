package com.rockgarden.myapp.demo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.rockgarden.AdvancedWebView;
import com.rockgarden.myapp.R;

public class WebViewDemo extends AppCompatActivity implements AdvancedWebView.Listener {
    private static final String TAG = WebViewDemo.class.getSimpleName();
    private AdvancedWebView mWebView;
    private Context context;
    private ProgressDialog progressDialog;
    private ImageView webImage;
    private static final String TAG_PAGE_URL = "https://github.com/";

    /**
     * @return
     */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = WebViewDemo.this;
        FrameLayout rootLayout = new FrameLayout(this);
        // 根布局参数
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT); //相当于直接加Layout不带layoutParams
        setContentView(rootLayout, layoutParams);
        webImage = new ImageView(this);
        webImage.setImageResource(R.mipmap.ic_launcher);
        webImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.addView(webImage);
        mWebView = new AdvancedWebView(this);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.addView(mWebView);

        mWebView.setListener(this, this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.setBackgroundColor(getResources().getColor(R.color.blue_light));
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(context, "Finished loading", Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.addHttpHeader("X-Requested-With", "");

        mWebView.loadUrl(TAG_PAGE_URL);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("数据加载中，请稍后。。。");
            progressDialog.show();
            mWebView.setVisibility(View.INVISIBLE); //当加载网页的时候将网页进行隐藏
        }
    }

    @Override
    public void onPageFinished(String url) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(context, "onPageError(errorCode = " + errorCode + ",  description = " + description + ",  failingUrl = " + failingUrl + ")", Toast.LENGTH_SHORT).show();
        mWebView.loadUrl("file:///android_asset/error.html");
    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Toast.makeText(context, "onDownloadRequested(url = " + url + ",  userAgent = " + userAgent + ",  contentDisposition = " + contentDisposition + ",  mimetype = " + mimetype + ",  contentLength = " + contentLength + ")", Toast.LENGTH_LONG).show();

		/*final String filename = UUID.randomUUID().toString();

		if (AdvancedWebView.handleDownload(this, url, filename)) {
			// download successfully handled
		}
		else {
			// download couldn't be handled because user has disabled download manager app on the device
		}*/
    }

    @Override
    public void onExternalPageRequest(String url) {
        Toast.makeText(context, "onExternalPageRequest(url = " + url + ")", Toast.LENGTH_SHORT).show();
    }
}
