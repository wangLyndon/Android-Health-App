package com.example.healthapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.activities.ChangeFrag;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class StockFragment extends Fragment {
    private String Api = "???";


    private EditText inputStock;
    private TextView searchStock;
    private TextView stockPrice;
    private TextView companyName;
    private TextView change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        inputStock = view.findViewById(R.id.inputStock);
        searchStock = view.findViewById(R.id.searchStock);
        stockPrice = view.findViewById(R.id.stockPrice);
        companyName = view.findViewById(R.id.companyName);
        change = view.findViewById(R.id.change);

        searchStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stockName = inputStock.getText().toString();
                if (!stockName.isEmpty()){
                    searchStock(stockName);
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "Stock");
                startActivity(intent);
            }
        });

        return view;
    }

    private void searchStock(String stockName) {
        AsyncHttpClient client = new AsyncHttpClient();
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stockName + "&interval=1min&apikey=" + Api;
        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject jsonObject = response.optJSONObject("Time Series (1min)");

                if (jsonObject == null){
                    stockPrice.setText("Stock code error");
                    companyName.setText("Stock code error");
                    return;
                }

                /*
                    "Time Series (1min)": {
                        "2024-11-01 20:00:00": {
                            "1. open": "150.2300",
                            "2. high": "150.5000",
                            "3. low": "150.2200",
                            "4. close": "150.4000",
                            "5. volume": "50000"
                        },
                        .....
                 */

                String latestTime = jsonObject.keys().next();
                try {
                    String stockPrice = jsonObject.getJSONObject(latestTime).getString("1. open");
                    double stockPriceDouble = Double.parseDouble(stockPrice);
                    updateUI(stockPriceDouble, stockName);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateUI(double price, String name){
        stockPrice.setText(String.format("%.2f", price));
        companyName.setText(name);
    }
}