package edu.temple.stockviewer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class StockDetailsFragment extends Fragment {

    Stock stock;
    TextView companyName;
    TextView stockPrice;
    ImageView stockImage;
    public StockDetailsFragment() {
        // Required empty public constructor
    }

    public static StockDetailsFragment newInstance(Stock stock) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("stock", stock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stock = (Stock) getArguments().getSerializable("stock");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stock_details, container, false);
        companyName = v.findViewById(R.id.companyName);
        stockPrice = v.findViewById(R.id.stockPrice);
        stockImage = v.findViewById(R.id.stockImage);
        companyName.setText(stock.getName());
        stockPrice.setText("$" + stock.getStockPrice().toString());
        Picasso.with(getActivity()).load(stock.getImageURL()).into(stockImage);
        return v;
    }

}
