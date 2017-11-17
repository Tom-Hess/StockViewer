package edu.temple.stockviewer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Tom on 11/16/2017.
 */

public class StockDataTask extends AsyncTask<String, Void, String> {

    StringBuilder sb;
    protected String doInBackground(String... params) {
        HttpURLConnection con = null;
        try {
            URL u = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + params[0]);
            con = (HttpURLConnection) u.openConnection();

            con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
    protected void onPostExecute(String response) {
        Log.i("async task", "the response = " + sb.toString());
    }

}
