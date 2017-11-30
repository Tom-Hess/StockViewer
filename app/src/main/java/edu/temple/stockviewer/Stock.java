package edu.temple.stockviewer;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Tom on 11/16/2017.
 */

// A stock object with name, symbol, imageURL, stockPrice, and last change in price
public class Stock implements Serializable {
    private String name;
    private String symbol;
    private String imageURL;
    private Double stockPrice;
    private Double change;

    public Stock(String name, String symbol, Double stockPrice, String imageURL, Double change) {
        this.name = name;
        this.symbol = symbol;
        this.stockPrice = stockPrice;
        this.imageURL = imageURL;
        this.change = change;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getStockPrice() {
        return this.stockPrice;
    }

    public void setStockPrice(Double sp) {
        this.stockPrice = sp;
    }

    public Double getChange() {
        return this.change;
    }

    public void setChange(Double change) {
        this.change = change;
    }


}
