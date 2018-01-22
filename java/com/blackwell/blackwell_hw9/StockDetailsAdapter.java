package com.blackwell.blackwell_hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class StockDetailsAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PriceRow> dataSource;

    public StockDetailsAdapter(Context context, ArrayList<PriceRow> items)
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
        View rowView = inflater.inflate(R.layout.price_row_item, parent, false);
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.priceRowHeader);
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.priceRowData);
        ImageView imageView =
                (ImageView) rowView.findViewById(R.id.priceRowImage);

        final PriceRow priceRow=(PriceRow)getItem(position);

        titleTextView.setText(priceRow.getHeader());
        subtitleTextView.setText(priceRow.getData());
        if(priceRow.hasImage()==true)
        {
            if(priceRow.getData().contains("-"))
            {
                imageView.setImageResource(R.drawable.down_arrow);
            }
            else
            {
                imageView.setImageResource(R.drawable.up_arrow);
            }
        }

        return rowView;
    }

}
