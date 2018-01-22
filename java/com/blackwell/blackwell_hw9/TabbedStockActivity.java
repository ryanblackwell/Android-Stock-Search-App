package com.blackwell.blackwell_hw9;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class TabbedStockActivity extends AppCompatActivity
{
    private static final String TAG="TabbedStockActivity";
    private TabsPageAdapter tabsPageAdapter;
    private ViewPager viewPager;
    private RequestQueue requestQueue;
    private String stockTicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_stock);

        Log.d(TAG, "onCreate: Starting.");

        this.setupViewPager();
        this.setupToolbar();

        //Create network requests
        requestQueue= Volley.newRequestQueue(this);
        this.createNewsRequest();
        this.createPriceRequest();
    }

    @Override
    protected void onPause()
    {
        if (this.requestQueue!=null) { this.requestQueue.cancelAll(TAG); }
        super.onPause();
    }

    private void createNewsRequest()
    {
        String baseUrl="http://blackwellcsci571hw7.us-west-1.elasticbeanstalk.com/news.php?symbol=";
        String url=baseUrl+stockTicker;
        Log.i(TAG, url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NewsTabFragment newsTabFragment = (NewsTabFragment)tabsPageAdapter.getItem(2);
                        newsTabFragment.setJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressBar progressBar=(ProgressBar) findViewById(R.id.newsTabProgressBar);
                        progressBar.setVisibility(View.INVISIBLE);
                        TextView textView=(TextView) findViewById(R.id.newsTabErrorMessage);
                        textView.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void createPriceRequest()
    {
        String baseUrl="http://blackwellcsci571hw7.us-west-1.elasticbeanstalk.com/price.php?symbol=";
        String url=baseUrl+stockTicker;
        Log.i(TAG, url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CurrentTabFragment currentTabFragment = (CurrentTabFragment) tabsPageAdapter.getItem(0);
                        currentTabFragment.setJSON(response);
                        HistoricalTabFragment historicalTabFragment = (HistoricalTabFragment) tabsPageAdapter.getItem(1);
                        historicalTabFragment.setJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressBar currentProgressBar=(ProgressBar) findViewById(R.id.currentTabProgressBar);
                        currentProgressBar.setVisibility(View.INVISIBLE);
                        TextView currentTextView=(TextView) findViewById(R.id.currentTabErrorMessage);
                        currentTextView.setVisibility(View.VISIBLE);
                        ProgressBar historicalProgressBar=(ProgressBar) findViewById(R.id.historicalTabProgressBar);
                        historicalProgressBar.setVisibility(View.INVISIBLE);
                        TextView historicalTextView=(TextView) findViewById(R.id.historicalTabErrorMessage);
                        historicalTextView.setVisibility(View.VISIBLE);
                        error.printStackTrace();
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void setupViewPager()
    {
        tabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        tabsPageAdapter.addFragment(new CurrentTabFragment(), "CURRENT");
        tabsPageAdapter.addFragment(new HistoricalTabFragment(), "HISTORICAL");
        tabsPageAdapter.addFragment(new NewsTabFragment(), "NEWS");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(tabsPageAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar()
    {
        // Display the back button for the toolbar and display the stock ticker
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent=getIntent();
        stockTicker=intent.getExtras().getString("stockticker");
        stockTicker=stockTicker.toUpperCase();
        getSupportActionBar().setTitle(stockTicker);
    }

    public String getStockTicker()
    {
        return stockTicker;
    }


}
