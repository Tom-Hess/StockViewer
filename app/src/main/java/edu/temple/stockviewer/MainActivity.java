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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();

        try {
            FileInputStream fis = this.openFileInput("savedStocks");
            ObjectInputStream is = new ObjectInputStream(fis);
            stockArray = (ArrayList<Stock>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception ex) {
            stockArray = new ArrayList<Stock>();
            ex.printStackTrace();
        }


        twoPane = (findViewById(R.id.fragment2) != null);

        portfolioFragment = PortfolioFragment.newInstance(stockArray);
        loadFragment(R.id.fragment1, portfolioFragment, false);

    }

    public void displayStock(int position) {
        StockDetailsFragment stockDetails = StockDetailsFragment.newInstance(stockArray.get(position));
        loadFragment(twoPane ? R.id.fragment2 : R.id.fragment1, stockDetails, !twoPane);
    }

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
                            setCurrentStockList(service.getUpdate(getCurrentStockList()));
                            if(stockArray.size() > 0) {
                                Toast toast = Toast.makeText(MainActivity.this, "Stock prices updated", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60*1000);  // interval of one minute

    }

    public ArrayList<Stock> getCurrentStockList() {
        return this.stockArray;
    }
    public void setCurrentStockList(ArrayList<Stock> stocks) {
        this.stockArray = stocks;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String data;
                String name;
                String symbol;
                Double lastPrice;
                String imageURL;
                try {
                    data = new StockDataTask().execute(query).get();
                    JSONObject stockInfo = new JSONObject(data);
                    name = stockInfo.getString("Name");
                    symbol = stockInfo.getString("Symbol");
                    lastPrice = stockInfo.getDouble("LastPrice");
                    imageURL = "https://finance.google.com/finance/getchart?q=" + symbol + "&p=5d";
                    if(!symbol.equals("")) {
                        Stock stock = new Stock(name, symbol, lastPrice, imageURL);
                        stockArray.add(stock);
                        portfolioFragment = PortfolioFragment.newInstance(stockArray);
                        loadFragment(R.id.fragment1, portfolioFragment, false);
                        Toast toast = Toast.makeText(MainActivity.this, "Added " + name, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } catch (Exception e) {
                    Toast toast = Toast.makeText(MainActivity.this, "Invalid stock symbol!", Toast.LENGTH_SHORT);
                    toast.show();
                    e.printStackTrace();
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

    private void loadFragment(int id, Fragment fragment, boolean addToBackStack){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft;
        ft = fm.beginTransaction().replace(id, fragment);
        if(addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    protected void onStop() {
        try {
            FileOutputStream fos = this.openFileOutput("savedStocks", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(stockArray);
            os.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onStop();
    }
    @Override
    protected void onDestroy() {
        try {
            FileOutputStream fos = this.openFileOutput("savedStocks", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(stockArray);
            os.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onDestroy();
    }
}
