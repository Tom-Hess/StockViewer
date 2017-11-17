package edu.temple.stockviewer;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Tom on 11/16/2017.
 */

public class Stock implements Serializable {
    private String name;
    private String symbol;
    private String imageURL;
    private Double stockPrice;
    public Stock(String name, String symbol, String imageURL) {
        this.name = name;
        this.symbol = symbol;
        this.imageURL = imageURL;
    }
    public Stock(String name) {
        this.name = name;
    }

    public Stock(String name, String symbol, Double stockPrice, String imageURL) {
        this.name = name;
        this.symbol = symbol;
        this.stockPrice = stockPrice;
        this.imageURL = imageURL;
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

}
