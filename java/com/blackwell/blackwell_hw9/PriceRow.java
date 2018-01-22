package com.blackwell.blackwell_hw9;

public class PriceRow
{
    private String header;
    private String data;
    private boolean hasImage;

    public PriceRow(String header, String data, boolean hasImage)
    {
        this.header=header;
        this.data=data;
        this.hasImage=hasImage;
    }

    public String getHeader()
    {
        return header;
    }

    public String getData()
    {
        return data;
    }

    public boolean hasImage()
    {
        return hasImage;
    }

}
