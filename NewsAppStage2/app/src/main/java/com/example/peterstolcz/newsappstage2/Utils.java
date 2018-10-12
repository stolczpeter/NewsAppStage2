package com.example.peterstolcz.newsappstage2;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();


    private Utils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> newslist = extractFeatureFromJson(jsonResponse);

        return newslist;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newslist = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            JSONArray newsResults = newsObject.getJSONArray("results");

            for (int i = 0; i < newsResults.length(); i++) {

                JSONObject currentNews = newsResults.getJSONObject(i);

                String title = currentNews.getString("webTitle");

                String section = currentNews.getString("sectionName");

                String date = "";
                try {

                    String longdate = currentNews.getString("webPublicationDate");
                    date = longdate.substring(0, Math.min(longdate.length(), 10));
                } catch (Exception e) {

                    date = "No author";
                }

                String url = currentNews.getString("webUrl");

                String authortag = "";

                try {
                    JSONArray tagsArray = currentNews.getJSONArray("tags");
                    JSONObject tag = tagsArray.getJSONObject(0);
                    authortag = tag.getString("webTitle");
                } catch (Exception e) {

                    authortag = "No author";
                }
                News news = new News(title, section, date, url, authortag);

                newslist.add(news);
            }


        } catch (JSONException e) {

            Log.e("Utils", "Problem parsing JSON results", e);
        }

        return newslist;
    }


}