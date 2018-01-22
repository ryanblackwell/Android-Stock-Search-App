package com.blackwell.blackwell_hw9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

public class HistoricalTabFragment extends Fragment
{
    private static final String TAG = "HistoricalTabFragment";
    private String stockTicker;
    private WebView webview;
    private JSONObject priceJSONObj;
    private JSONArray priceSeries;
    private JSONArray timeSeries;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Historical tab created");

        final TabbedStockActivity parentActivity=(TabbedStockActivity)getActivity();
        stockTicker=parentActivity.getStockTicker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.historical_tab_fragment,container,false);

        webview=(WebView) view.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:CreateHistoricalChart("+"'"+stockTicker+"', "+
                        priceSeries.toString()+", "+timeSeries.toString()+")");
            }
        });
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        return view;
    }

    public void setJSON(JSONObject priceJSONObj)
    {
        this.priceJSONObj=priceJSONObj;
        try
        {
            String errorMessage=this.priceJSONObj.getString("Error");
            if(errorMessage.equals("false"))
            {
                this.priceSeries=priceJSONObj.getJSONArray("PriceSeries");
                this.timeSeries=priceJSONObj.getJSONArray("TimeSeries");
                this.webview.loadUrl("file:///android_asset/historical.html");
                ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.historicalTabProgressBar);
                progressBar.setVisibility(View.INVISIBLE);
            }
            else if(errorMessage.equals("true")){ this.handleServerError(); }
            else { throw new Exception("true/false tag not equal to true or false"); }
        }
        catch(Exception e)
        {
            System.err.println("Error: Malformed price JSON: "+e.getMessage());
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.historicalTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) getView().findViewById(R.id.historicalTabErrorMessage);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void handleServerError()
    {
        ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.historicalTabProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        TextView textView=(TextView) getView().findViewById(R.id.historicalTabErrorMessage);
        textView.setVisibility(View.VISIBLE);
    }
}
