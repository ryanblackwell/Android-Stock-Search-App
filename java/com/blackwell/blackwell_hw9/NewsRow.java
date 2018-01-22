package com.blackwell.blackwell_hw9;

public class NewsRow
{
    private String articleTitle;
    private String hyperlink;
    private String author;
    private String date;

    public NewsRow(String articleTitle, String hyperlink, String author, String date)
    {
        this.articleTitle=articleTitle;
        this.hyperlink=hyperlink;
        this.author="Author: "+author;
        this.date="Date: "+date+" EDT";
    }

    public String getArticleTitle()
    {
        return articleTitle;
    }

    public String getHyperlink()
    {
        return hyperlink;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getDate()
    {
        return date;
    }
}
