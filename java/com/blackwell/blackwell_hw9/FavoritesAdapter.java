package com.blackwell.blackwell_hw9;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<FavoriteRow> dataSource;

    public FavoritesAdapter(Context context, ArrayList<FavoriteRow> items)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = inflater.inflate(R.layout.favorite_row_item, parent, false);
        TextView symbolTextView =
                (TextView) rowView.findViewById(R.id.tickerSymbol);
        TextView priceTextView =
                (TextView) rowView.findViewById(R.id.priceValue);
        TextView changeTextView =
                (TextView) rowView.findViewById(R.id.changeChangePercent);

        final FavoriteRow favoriteRow=(FavoriteRow)getItem(position);

        symbolTextView.setText(favoriteRow.getStockTicker());
        priceTextView.setText(favoriteRow.getStockPrice());
        changeTextView.setText(favoriteRow.getChangeChangePercent());

        if(favoriteRow.getChangeChangePercent().contains("-"))
        {
            changeTextView.setTextColor(Color.RED);
        }
        else
        {
            changeTextView.setTextColor(context.getColor(R.color.positive_green));
        }

        return rowView;
    }
}
