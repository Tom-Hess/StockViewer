package edu.temple.stockviewer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //getQuote(intent.getStringExtra("stock_symbol"));
    }

    public ArrayList<Stock> getUpdate(ArrayList<Stock> stocks) {
        ArrayList<Stock> updatedStocks = new ArrayList<Stock>();
        for(int i = 0; i < stocks.size(); i++) {
            Stock s = stocks.get(i);
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
                updatedStocks.add(s);
                e.printStackTrace();
            }
        }
        return updatedStocks;
    }





}
