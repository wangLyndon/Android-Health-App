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

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class CryptoFragment extends Fragment {

    private EditText inputCoin;
    private TextView searchCoin;
    private TextView coinPrice;
    private TextView bitcoinPrice;
    private TextView ethPrice;
    private TextView change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crypto, container, false);

        inputCoin = view.findViewById(R.id.inputCoin);
        searchCoin = view.findViewById(R.id.searchCoin);
        coinPrice = view.findViewById(R.id.coinPrice);
        bitcoinPrice = view.findViewById(R.id.bitcoinPrice);
        ethPrice = view.findViewById(R.id.ethPrice);
        change = view.findViewById(R.id.change);

        searchCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputCoin.getText().toString();
                if (!name.isEmpty()){
                    searchCoin(name);
                }
            }
        });

        searchCoin("bitcoin");
        searchCoin("ethereum");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "Crypto");
                startActivity(intent);
            }
        });

        return view;
    }

    private void searchCoin(String name) {
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + name + "&vs_currencies=usd";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject jsonObject = response.optJSONObject(name);


                    /*
                    {
                      "ethereum": {
                        "usd": 2983.42
                      }
                    }
                     */


                    if (jsonObject == null){
                        coinPrice.setText("Coin code error");
                        return;
                    }

                    double coinPrice = jsonObject.getDouble("usd");

                    updateUI(name, coinPrice);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateUI(String name, double price){
        switch (name){
            case "bitcoin":
                bitcoinPrice.setText(String.format("%.2f", price));
                break;
            case  "ethereum":
                ethPrice.setText(String.format("%.2f", price));
                break;
            default:
                coinPrice.setText(String.format("%.2f", price));
        }
    }
}