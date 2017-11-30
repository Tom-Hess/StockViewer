package edu.temple.stockviewer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity implements PortfolioFragment.OnStockClickListener {

    boolean twoPane;
    ArrayList<Stock> stockArray;
    Fragment portfolioFragment;
    StockDetailsFragment stockDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();

        stockArray = getStocksFromFile();

        twoPane = (findViewById(R.id.fragment2) != null);

        //load the portfolio fragment with the stocks from the saved file (if applicable)
        portfolioFragment = PortfolioFragment.newInstance(stockArray);
        loadFragment(R.id.fragment1, portfolioFragment, false);

    }


    // Retrieves the list of stocks from the saved file
    public ArrayList<Stock> getStocksFromFile() {
        try {
            FileInputStream fis = this.openFileInput("savedStocks");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<Stock> stockArray = (ArrayList<Stock>) is.readObject();
            is.close();
            fis.close();
            return stockArray;
        } catch (Exception ex) {
            return new ArrayList<Stock>();
        }
    }


    //Display the StockDetailsFragment when a user clicks on the stock in the gridview
    public void displayStock(int position) {
        stockDetails = StockDetailsFragment.newInstance(getStocksFromFile().get(position));
        loadFragment(twoPane ? R.id.fragment2 : R.id.fragment1, stockDetails, !twoPane);
    }


    //Starts the service to update stocks every 60 seconds
    private void startService() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            UpdateService service = new UpdateService();
                            stockArray = service.getUpdate(getStocksFromFile());
                            saveToFile();
//                            Toast toast = Toast.makeText(MainActivity.this, R.string.stock_prices_updated, Toast.LENGTH_SHORT);
//                            toast.show();
                            portfolioFragment = PortfolioFragment.newInstance(stockArray);
                            loadFragment(R.id.fragment1, portfolioFragment, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        //Time the update to execute every 60 seconds
        timer.schedule(task, 0, 60 * 1000);  // interval of one minute
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean isDuplicate = false;
                stockArray = getStocksFromFile();
                for (Stock s : stockArray) {
                    //Check for duplicate stock symbol
                    if (s.getSymbol().equalsIgnoreCase(query)) {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.stock_already_added, Toast.LENGTH_SHORT);
                        toast.show();
                        isDuplicate = true;
                    }
                }

                if (!isDuplicate) {
                    String data;
                    String name;
                    String symbol;
                    Double lastPrice;
                    String imageURL;
                    Double change;
                    try {
                        //Retrieve the JSON stock data and add the stock to the ArrayList
                        data = new StockDataTask().execute(query).get();
                        JSONObject stockInfo = new JSONObject(data);
                        name = stockInfo.getString("Name");
                        symbol = stockInfo.getString("Symbol");
                        lastPrice = stockInfo.getDouble("LastPrice");
                        change = stockInfo.getDouble("Change");
                        imageURL = "https://finance.google.com/finance/getchart?q=" + symbol + "&p=5d";
                        if (!symbol.equals("")) {
                            //If the symbol does not come back empty, add the stock to the user's portfolio
                            Stock stock = new Stock(name, symbol, lastPrice, imageURL, change);
                            stockArray.add(stock);
                            saveToFile();
                            portfolioFragment = PortfolioFragment.newInstance(stockArray);
                            loadFragment(R.id.fragment1, portfolioFragment, false);
                            Toast toast = Toast.makeText(MainActivity.this, "Added " + name, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    } catch (Exception e) {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.invalid_stock_symbol, Toast.LENGTH_SHORT);
                        toast.show();
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    //Loads a new fragment to the UI
    private void loadFragment(int id, Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft;
        ft = fm.beginTransaction().replace(id, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    protected void onStop() {
        saveToFile();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        saveToFile();

        super.onDestroy();
    }

    //Saves the user's portfolio of stocks to a file
    public void saveToFile() {
        try {
            FileOutputStream fos = this.openFileOutput("SavedStocks", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(stockArray);
            os.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
