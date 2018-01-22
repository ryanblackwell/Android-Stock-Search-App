package com.blackwell.blackwell_hw9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class NewsTabFragment extends Fragment
{
    private static final String TAG = "NewsTabFragment";
    private String stockTicker;
    private JSONObject newsJSONObj;
    private ArrayList<NewsRow> newsRowArrayList;
    private ListView listView;
    private NewsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "News tab created");

        final TabbedStockActivity parentActivity=(TabbedStockActivity)getActivity();
        stockTicker=parentActivity.getStockTicker();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.news_tab_fragment,container,false);

        listView=(ListView) view.findViewById(R.id.news_list_view);
        this.newsRowArrayList=new ArrayList<NewsRow>();
        this.adapter = new NewsAdapter(getActivity(), newsRowArrayList);
        listView.setAdapter(adapter);

        return view;
    }

    public void setJSON(JSONObject newsJSONObj)
    {
        this.newsJSONObj=newsJSONObj;
        try
        {
            String errorMessage=this.newsJSONObj.getString("Error");
            if(errorMessage.equals("false")){ this.parseNewsJSON(); }
            else if(errorMessage.equals("true")){ this.handleServerError(); }
            else { throw new Exception("true/false tag not equal to true or false"); }
        }
        catch (Exception e)
        {
            System.err.println("Error: Malformed news JSON: "+e.getMessage());
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.newsTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) getView().findViewById(R.id.newsTabErrorMessage);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void parseNewsJSON()
    {
        try
        {
            JSONArray newsArray=newsJSONObj.getJSONArray("NewsArray");
            int newsArrayLen=newsArray.length();
            for(int i=0; i<newsArrayLen; i++)
            {
                JSONObject newsRow=newsArray.getJSONObject(i);
                String articleTitle=newsRow.getString("title");
                String hyperLink=newsRow.getString("link");
                String author=newsRow.getString("authorName");
                String date=newsRow.getString("pubDate");
                NewsRow row=new NewsRow(articleTitle, hyperLink, author, date);
                newsRowArrayList.add(row);
            }
            adapter.notifyDataSetChanged();
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.newsTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {
            System.err.println("Error: Malformed news JSON: "+e.getMessage());
            ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.newsTabProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) getView().findViewById(R.id.newsTabErrorMessage);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void handleServerError()
    {
        ProgressBar progressBar=(ProgressBar) getView().findViewById(R.id.newsTabProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        TextView textView=(TextView) getView().findViewById(R.id.newsTabErrorMessage);
        textView.setVisibility(View.VISIBLE);
    }
}
