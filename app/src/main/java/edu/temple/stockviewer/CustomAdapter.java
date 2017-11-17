package edu.temple.stockviewer;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 11/16/2017.
 */

public class CustomAdapter extends ArrayAdapter {
    LayoutInflater inflater;
    ArrayList<Stock> stocks;

    public CustomAdapter(Activity context, int resource, int textViewId, ArrayList<Stock> stocks) {
        super(context, resource, textViewId, stocks);
        inflater = context.getLayoutInflater();
        this.stocks = stocks;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View items = inflater.inflate(R.layout.activity_background, null, true);

        TextView txtStockName = (TextView) items.findViewById(R.id.textView);
        txtStockName.setText(stocks.get(position).getSymbol().toString());
        txtStockName.getLayoutParams().width = 2000;
        txtStockName.getLayoutParams().height= 200;
        txtStockName.setGravity(Gravity.CENTER);
        txtStockName.setTextSize(30);
        return items;
    }

}
