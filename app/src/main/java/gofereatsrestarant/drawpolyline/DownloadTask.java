package gofereatsrestarant.drawpolyline;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage drawpolyline
 * @category DownloadTask
 * @author Trioangle Product Team
 * @version 1.5
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*************************************************************
 Get data form direction API
 *************************************************************** */
public class DownloadTask extends AsyncTask<String, Void, String> {

    public PolylineOptionsInterface polylineOptionsInterface = null;

    public DownloadTask(PolylineOptionsInterface polylineOptionsInterface) {
        this.polylineOptionsInterface = polylineOptionsInterface;
    }

    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(this.polylineOptionsInterface);
        parserTask.execute(result);
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
