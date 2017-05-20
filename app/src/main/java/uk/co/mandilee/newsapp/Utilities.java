package uk.co.mandilee.newsapp;

import android.content.res.Resources;
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

class Utilities {
    private static final String LOG_TAG = Utilities.class.getSimpleName();

    private Utilities() {
        // empty on purpose!
    }

    static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.http_request_failed), e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.url_build_failure), e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnect = null;
        InputStream inputStream = null;

        try {
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setReadTimeout(10000);
            urlConnect.setConnectTimeout(15000);
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();

            if (urlConnect.getResponseCode() == 200) {
                inputStream = urlConnect.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, Resources.getSystem().getString(R.string.error_code) + urlConnect.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.problem_getting_json), e);

        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
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

        List<News> articles = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject newsResponse = baseJsonResponse.getJSONObject("response");

            JSONArray newsArray = newsResponse.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);

                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String published = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                News news = new News(title, section, published, url);

                articles.add(news);
            }
        } catch (JSONException e) {
            Log.e("Utilities", Resources.getSystem().getString(R.string.problem_parsing_json), e);
        }
        return articles;
    }
}