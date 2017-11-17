package edu.temple.stockviewer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tom on 11/17/2017.
 */
public class UpdateDataTask extends AsyncTask<ArrayList<Stock>, Void, ArrayList<Stock>> {


    protected ArrayList<Stock> doInBackground(ArrayList<Stock>... stocks) {
        ArrayList<Stock> updatedStocks = new ArrayList<Stock>();
        for(int i = 0; i < stocks[0].size(); i++) {
            Stock s = stocks[0].get(i);
            String symbol = s.getSymbol();
            String data;
            Double lastPrice;
            try {
                data = new StockDataTask().execute(symbol).get();
                JSONObject stockInfo = new JSONObject(data);
                lastPrice = stockInfo.getDouble("LastPrice");
                s.setStockPrice(lastPrice);
                updatedStocks.add(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updatedStocks;
    }

    protected void onPostExecute(ArrayList<Stock> stocks) {
    }

}