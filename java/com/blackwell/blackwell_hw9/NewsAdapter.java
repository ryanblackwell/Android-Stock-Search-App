package com.blackwell.blackwell_hw9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<NewsRow> dataSource;

    public NewsAdapter(Context context, ArrayList<NewsRow> items)
    {
        this.context=context;
        this.dataSource=items;
        this.inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position)
    {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.news_row_item, parent, false);
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.news_title);
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.news_subtitle);
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.news_date);

        final NewsRow newsRow=(NewsRow)getItem(position);

        titleTextView.setText(newsRow.getArticleTitle());
        subtitleTextView.setText(newsRow.getAuthor());
        detailTextView.setText(newsRow.getDate());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String hyperLink=newsRow.getHyperlink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hyperLink));
                context.startActivity(browserIntent);
            }
        });

        return rowView;
    }
}
