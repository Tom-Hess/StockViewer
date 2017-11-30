package edu.temple.stockviewer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


public class PortfolioFragment extends Fragment {
    private static ArrayList<Stock> stocks;

    GridView gvStockNames;
    TextView msgNoStocks;
    private OnStockClickListener mListener;
    CustomAdapter adapter;
    public PortfolioFragment() {
        // Required empty public constructor
    }

    public static PortfolioFragment newInstance(ArrayList<Stock> stocks) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putSerializable("stocks", stocks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_portfolio, container, false);
        gvStockNames = v.findViewById(R.id.gvStockNames);
        msgNoStocks = v.findViewById(R.id.msgNoStocks);
        Bundle args = getArguments();
        stocks = (ArrayList<Stock>) args.getSerializable("stocks");
        if(stocks.size() > 0)
            msgNoStocks.setVisibility(View.INVISIBLE);
        this.adapter = new CustomAdapter(getActivity(), R.layout.activity_background,
                R.id.textView, stocks);

        gvStockNames.setAdapter(adapter);
        gvStockNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.displayStock(position);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStockClickListener) {
            mListener = (OnStockClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnStockClickListener {
        void displayStock(int position);
    }
}
