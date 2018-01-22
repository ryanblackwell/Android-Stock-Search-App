package com.blackwell.blackwell_hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrentTabFragment extends Fragment
{
    private static final String TAG = "CurrentTabFragment";
    private JSONObject historicalPriceJSONObj;
    private JSONArray historicalPriceJSONArr;
    private ArrayList<PriceRow> priceRowArrayList;
    private ListView listView;
    private String stockTickerSymbol, lastPrice, previousClose, timestamp, open, change;
    private String changePercent, low, high, volume;
    private StockDetailsAdapter adapter;
    public static final String MY_PREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Current tab created");

        this.sharedPreferences=getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.current_tab_fragment,container,false);

        listView=(ListView) view.findViewById(R.id.price_list_view);
        this.priceRowArrayList=new ArrayList<PriceRow>();
        this.adapter = new StockDetailsAdapter(getActivity(), priceRowArrayList);
        listView.setAdapter(adapter);

        //Setup favorite and facebook buttons
        this.setupButtons(view);

        return view;
    }

    public void setJSON(JSONObject priceJSONObj)
    {
        this.historicalPriceJSONObj=priceJSONObj;
        try
        {
            String errorMessage=this.historicalPriceJSONObj.getString("Error");
            if(errorMessage.equals("false")){ this.parseHistoricalPriceJSON(); }
            else if(errorMessage.equals("true")){ this.handleServerError(); }
            else { throw new Exception("true/false tag not equal to true or false"); }
        }
        catch (Exception e)
        {
            System.err.println("Error: Malformed price JSON: "+e.getMessage());
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.currentTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) getView().findViewById(R.id.currentTabErrorMessage);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void parseHistoricalPriceJSON()
    {
        try
        {
            DecimalFormat df = new DecimalFormat("0.00");
            this.stockTickerSymbol=historicalPriceJSONObj.getString("StockTickerSymbol");
            this.timestamp=historicalPriceJSONObj.getString("TimeStamp");
            this.volume=Integer.toString(historicalPriceJSONObj.getInt("Volume"));
            this.historicalPriceJSONArr=historicalPriceJSONObj.getJSONArray("PriceSeries");
            this.lastPrice=df.format(historicalPriceJSONObj.getDouble("LastPrice"));
            this.previousClose=df.format(historicalPriceJSONObj.getDouble("PreviousClose"));
            this.open=df.format(historicalPriceJSONObj.getDouble("Open"));
            this.change=df.format(historicalPriceJSONObj.getDouble("Change"));
            this.changePercent=df.format(historicalPriceJSONObj.getDouble("ChangePercent"));
            this.low=df.format(historicalPriceJSONObj.getDouble("Low"));
            this.high=df.format(historicalPriceJSONObj.getDouble("High"));

            this.setupDetailsTable();
            this.enableButtons();
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.currentTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
        catch(Exception e)
        {
            System.err.println("Error: Malformed price JSON: "+e.getMessage());
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.currentTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) getView().findViewById(R.id.currentTabErrorMessage);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void handleServerError()
    {
        ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.currentTabProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        TextView textView=(TextView) getView().findViewById(R.id.currentTabErrorMessage);
        textView.setVisibility(View.VISIBLE);
    }

    private void setupDetailsTable()
    {
        priceRowArrayList.add(new PriceRow("Stock Symbol", this.stockTickerSymbol, false));
        priceRowArrayList.add(new PriceRow("Last Price", this.lastPrice, false));
        priceRowArrayList.add(new PriceRow("Change",
                this.change+" ("+this.changePercent+"%)", true));
        priceRowArrayList.add(new PriceRow("Timestamp", this.timestamp+" 16:00:00 EST",
                false));
        priceRowArrayList.add(new PriceRow("Open", this.open, false));
        priceRowArrayList.add(new PriceRow("Close", this.lastPrice, false));
        priceRowArrayList.add(new PriceRow("Day's Range",
                this.low+" - "+this.high, false));
        priceRowArrayList.add(new PriceRow("Volume", this.volume, false));
        adapter.notifyDataSetChanged();
    }

    private void setupButtons(View startupView)
    {
        final ImageButton starButton=(ImageButton)startupView.findViewById(R.id.starButton);
        final TabbedStockActivity parentActivity=(TabbedStockActivity)getActivity();
        String ticker=parentActivity.getStockTicker();
        if(this.sharedPreferences.contains(ticker))
        {
            starButton.setImageResource(R.drawable.filled);
        }
        else
        {
            starButton.setImageResource(R.drawable.empty);
        }
        starButton.setEnabled(false);
    }

    private void enableButtons()
    {
        final ImageButton starButton=(ImageButton)getView().findViewById(R.id.starButton);
        final String favStockTickerSymbol=this.stockTickerSymbol;
        final String favStockPrice=this.lastPrice;
        final String favChange=this.change;
        final String favChangePercent=this.changePercent;
        starButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean filled=sharedPreferences.contains(favStockTickerSymbol);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(filled==true)
                {
                    editor.remove(favStockTickerSymbol);
                    editor.commit();
                    starButton.setImageResource(R.drawable.empty);
                }
                else
                {
                    Gson gson = new Gson();
                    FavoriteRow favoriteRow=new FavoriteRow(favStockTickerSymbol, favStockPrice,
                            favChange, favChangePercent);
                    String json = gson.toJson(favoriteRow);
                    editor.putString(favStockTickerSymbol, json);
                    editor.commit();
                    starButton.setImageResource(R.drawable.filled);
                }
            }
        });
        starButton.setEnabled(true);
    }

}
