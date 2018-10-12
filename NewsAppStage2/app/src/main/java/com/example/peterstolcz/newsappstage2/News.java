package com.example.peterstolcz.newsappstage2;


public class News {
    private String mTitle;
    private String mSectionname;
    private String mDate;
    private String mUrl;
    private String mAuthor;

    public News(String title, String sectionname, String date, String url, String authortag) {

        mTitle = title;
        mSectionname = sectionname;
        mDate = date;
        mUrl = url;
        mAuthor = authortag;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSectionname() {
        return mSectionname;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }

}

