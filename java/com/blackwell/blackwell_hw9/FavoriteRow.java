package com.blackwell.blackwell_hw9;

public class FavoriteRow
{
    private String stockTicker;
    private String stockPrice;
    private String changeChangePercent;

    public FavoriteRow(String stockTicker, String stockPrice, String change, String changePercent)
    {
        this.stockTicker=stockTicker;
        this.stockPrice=stockPrice;
        this.changeChangePercent=change+" ("+changePercent+"%)";
    }

    public String getStockTicker()
    {
        return this.stockTicker;
    }

    public String getStockPrice()
    {
        return this.stockPrice;
    }

    public String getChangeChangePercent()
    {
        return this.changeChangePercent;
    }
}
