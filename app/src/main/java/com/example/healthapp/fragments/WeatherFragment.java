package com.example.healthapp.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.activities.ChangeFrag;
import com.example.healthapp.adapter.SportAdapter;
import com.example.healthapp.entities.Sport;
import com.example.healthapp.entities.Weather;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherFragment extends Fragment {
    private String Api = "???";
    private String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private long MIN_TIME = 5000;
    private float MIN_DISTANCE = 1000;
    private int REQUEST_CODE = 101;
    private LocationManager locationManager;
    private LocationListener locationListener;


    private String LocationProvider = LocationManager.GPS_PROVIDER;

    private ImageView weatherImg;
    private TextView weatherInfo;
    private TextView temperature;
    private TextView exerciseHint;
    private TextView change;

    private RecyclerView recyclerView;
    private SportAdapter sportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherImg = view.findViewById(R.id.weatherImg);
        weatherInfo = view.findViewById(R.id.weatherInfo);
        temperature = view.findViewById(R.id.temperature);
        exerciseHint = view.findViewById(R.id.exerciseHint);
        change = view.findViewById(R.id.change);

        recyclerView = view.findViewById(R.id.exerciseList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        sportAdapter = new SportAdapter(getActivity(), initSportList());
        recyclerView.setAdapter(sportAdapter);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "Weather");
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", Api);
                searchWeather(params);
            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationProvider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherForCurrentLocation();
            }
        }
    }

    private void searchWeather(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Weather weather = Weather.fromJson(response);
                updateUI(weather);
            }
        });
    }

    private void getHintByAI(Weather weather) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"Today's weather is: " + weather.getInfo() + ", temperature is: " + weather.getTemperature() + ", please give me some exercise advice, in 30 words\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"stream\": false,\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"temperature\": 0.5,\n" +
                "    \"presence_penalty\": 0,\n" +
                "    \"frequency_penalty\": 0,\n" +
                "    \"top_p\": 1\n" +
                "}");
        Request request = new Request.Builder()
                .url("https://xiaoai.plus/v1/chat/completions")
                .method("POST", body)
                .addHeader("Authorization", "Bearer sk-???")
                .addHeader("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                exerciseHint.setText("request Ai meets errors");
            }

            /*
                {
                    "choices": [
                        {
                            "finish_reason": "stop",
                            "index": 0,
                            "logprobs": null,
                            "message": {
                                "content": "今天是个晴天，温度适中，建议您出门运动，可以选择慢跑、散步或者骑自行车，享受户外的阳光和清新空气。祝您运动愉快！",
                                "refusal": null,
                                "role": "assistant"
                            }
                        }
                    ],
                    "created": 1731750619,
                    "id": "chatcmpl-AU9jXAAqZh6Jg8X1O3lZa4zFO8Wqq",
                    "model": "gpt-3.5-turbo",
                    "object": "chat.completion",
                    "system_fingerprint": null,
                    "usage": {
                        "completion_tokens": 69,
                        "completion_tokens_details": {
                            "reasoning_tokens": 0
                        },
                        "prompt_tokens": 47,
                        "prompt_tokens_details": {
                            "cached_tokens": 0
                        },
                        "total_tokens": 116
                    }
                }
             */

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String content = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    exerciseHint.setText(content);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateUI(Weather weather) {
        temperature.setText(weather.getTemperature());
        weatherInfo.setText(weather.getInfo());
        int resourceID = getResources().getIdentifier(weather.getImg(), "drawable", getActivity().getPackageName());
        weatherImg.setImageResource(resourceID);
        getHintByAI(weather);
    }

    private List<Sport> initSportList() {
        List<Sport> sports = new ArrayList<>();

        sports.add(new Sport("Run", R.drawable.run, "Running is a classic aerobic exercise that is suitable for all weather conditions, especially sunny and cloudy days, and helps to enhance cardiopulmonary function and overall endurance."));
        sports.add(new Sport("Cycling", R.drawable.cycling, "Cycling is an outdoor sport suitable for sunny weather. It not only strengthens leg muscles, but also allows you to experience natural scenery and fresh air."));
        sports.add(new Sport("Aerobics", R.drawable.fitness, "Aerobics is suitable for indoor exercise and is not affected by weather. It can effectively burn calories through aerobic exercise and enhance body coordination and physical fitness."));
        sports.add(new Sport("Jump Rope", R.drawable.rope, "Rope skipping is a simple and efficient aerobic exercise that is suitable for any venue. It can quickly improve cardiopulmonary endurance while exercising lower limb strength."));
        sports.add(new Sport("Ski", R.drawable.ski, "Skiing is an outdoor sport in winter, suitable for snowy days. It is not only exciting and fun, but also improves balance and coordination, while exercising the whole body."));

        return sports;
    }
}